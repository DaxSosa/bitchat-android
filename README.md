<b>I used AI to do this experiment, I'm learning</>

<p align="center">
  <img src="https://github.com/user-attachments/assets/188c42f8-d249-4a72-b27a-e2b4f10a00a8" alt="Bitchat Android Logo" width="480">
</p>

> [!WARNING]
> This software is under active development and has not been externally audited. Please do not use it in critical situations until thoroughly reviewed.

# BitChat for Android (with experimental Meshtastic integration)

BitChat is a decentralized, end-to-end encrypted messaging app that works without internet using mesh networks via Bluetooth. This is the Android port with full protocol compatibility with the iOS version.

> **🧪 New Experimental Feature**  
> We've integrated **classic Bluetooth (SPP)** support to communicate with **[Meshtastic](https://github.com/meshtastic/meshtastic)** devices, allowing BitChat to act as a client or repeater.  
> - The app automatically scans for Bluetooth devices with the name `Meshtastic`.  
> - It connects and exchanges messages over the serial channel.  
> - You can use BitChat as a messaging interface or to relay packets into the Meshtastic mesh network.

---

## 🔧 How to use Meshtastic integration

1. Turn on and pair your Meshtastic node via Bluetooth.
2. Install the modified BitChat APK.
3. Open the app — it will automatically search for and connect to `Meshtastic` devices.
4. When sending messages, they will also be relayed through the connected Meshtastic node.
5. Incoming messages from Meshtastic are displayed in real-time.

---

## 🧪 Meshtastic Integration Status

| Feature                               | Status       |
|--------------------------------------|--------------|
| Automatic Bluetooth scan             | ✅ Done       |
| Serial Bluetooth (SPP) connection    | ✅ Done       |
| Sending messages                     | ✅ Done       |
| Receiving messages into UI           | ✅ Basic working |
| UI to show connection status         | 🔜 In progress |
| Channel configuration support        | 🔜 Not yet     |
| BLE Mesh relay passthrough           | 🧪 In testing  |

---

## Build Instructions

👉 Please refer to the original **“Android Setup”** section for build instructions with Android Studio.

---

## 📌 Additional Notes

- Recommended for Android 8.0+ with classic Bluetooth support.
- Tested with ESP32-based Meshtastic nodes (T-Echo, T-Beam, etc.).
- This relay setup does not alter the Meshtastic protocol; it simply transmits plain text via the serial port.
- The integration is independent of BitChat’s internal BLE Mesh protocol.

---

## 🙏 Can you help?

If you’re experienced with Android or Meshtastic development, I’d greatly appreciate your feedback or code review on this integration. Feel free to open issues or PRs!

---

## Credits

- Base project: [BitChat Android](https://github.com/permissionlesstech/bitchat-android)
- Bluetooth mesh devices: [Meshtastic](https://github.com/meshtastic/meshtastic)
