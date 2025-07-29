package com.permissionlesstech.bitchat

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import kotlin.concurrent.thread

class MeshtasticBluetoothConnector {

    companion object {
        private const val TAG = "MeshtasticBT"
        private val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        private const val DEVICE_NAME_FILTER = "Meshtastic"
    }

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var socket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null
    private var inputStream: InputStream? = null

    private var readThread: Thread? = null
    private var running = false

    var onMessageReceived: ((String) -> Unit)? = null
    var onConnectionStateChanged: ((Boolean) -> Unit)? = null

    fun connect() {
        if (bluetoothAdapter == null) {
            Log.e(TAG, "Bluetooth no soportado en este dispositivo")
            onConnectionStateChanged?.invoke(false)
            return
        }

        if (!bluetoothAdapter.isEnabled) {
            Log.e(TAG, "Bluetooth está deshabilitado")
            onConnectionStateChanged?.invoke(false)
            return
        }

        // Buscar dispositivos emparejados que contengan "Meshtastic"
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
        val meshtasticDevice = pairedDevices?.firstOrNull {
            it.name.contains(DEVICE_NAME_FILTER, ignoreCase = true)
        }

        if (meshtasticDevice == null) {
            Log.e(TAG, "No se encontró dispositivo Meshtastic emparejado")
            onConnectionStateChanged?.invoke(false)
            return
        }

        Log.i(TAG, "Intentando conectar a: ${meshtasticDevice.name} - ${meshtasticDevice.address}")

        thread {
            try {
                socket = meshtasticDevice.createRfcommSocketToServiceRecord(SPP_UUID)
                bluetoothAdapter.cancelDiscovery()
                socket?.connect()

                outputStream = socket?.outputStream
                inputStream = socket?.inputStream

                running = true
                onConnectionStateChanged?.invoke(true)

                listenForMessages()
            } catch (e: Exception) {
                Log.e(TAG, "Error conectando: ${e.message}")
                onConnectionStateChanged?.invoke(false)
                close()
            }
        }
    }

    private fun listenForMessages() {
        readThread = thread {
            val buffer = ByteArray(1024)
            while (running) {
                try {
                    val bytesRead = inputStream?.read(buffer) ?: 0
                    if (bytesRead > 0) {
                        val msg = String(buffer, 0, bytesRead)
                        Log.i(TAG, "Mensaje recibido: $msg")
                        onMessageReceived?.invoke(msg)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error leyendo: ${e.message}")
                    running = false
                    onConnectionStateChanged?.invoke(false)
                    close()
                }
            }
        }
    }

    fun sendMessage(message: String) {
        try {
            val msgJson = "{\"text\":\"$message\"}"
            outputStream?.write(msgJson.toByteArray())
            Log.i(TAG, "Mensaje enviado: $msgJson")
        } catch (e: Exception) {
            Log.e(TAG, "Error enviando mensaje: ${e.message}")
        }
    }

    fun close() {
        running = false
        readThread?.interrupt()
        try {
            socket?.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error cerrando socket: ${e.message}")
        }
        socket = null
        outputStream = null
        inputStream = null
    }
}
