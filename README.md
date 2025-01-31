# GuardianCare App

A **Jetpack Compose** Android application for **fall detection**, **GPS tracking**, and **elderly care**. Designed with role-based interfaces (Elderly vs. Member/Caretaker), it leverages **BLE** for real-time fall detection from an M5StickC-Plus smartwatch and **notifications** to alert caregivers.

---

## Overview

GuardianCare aims to improve the safety and well-being of elderly individuals by integrating:
- **Real-Time Fall Detection** via Bluetooth Low Energy (BLE).
- **Notifications** to caretakers in case of falls or false alarms.
- **Role-Based UI** with an intuitive navigation drawer (Home, Profile, Settings, Fall Detection).
- Planned **Chatbot** and medication queries (placeholder).

Built with **Kotlin**, **Jetpack Compose** for UI, and **Material 3** components.

---

## Features

1. **Login & Role-Based Drawer**  
   - Distinct UI flows for Elderly vs. Member roles.  
   - Drawer items such as Home, Profile, Settings, and Fall Detection.

2. **BLE Fall Detection**  
   - Automatically scans for an M5StickC-Plus device.
   - Receives characteristic data (Fall, Recover, False Alarm).
   - Triggers an in-app **notification** and text update on fall events.

3. **Notifications**  
   - Requires runtime permissions (`POST_NOTIFICATIONS`) on Android 13+.
   - High-priority alerts linking back to the app.

4. **Profile & Settings**  
   - Placeholder screens for editing user details and caretaker management.

---

## Permissions

On newer Android versions (12+ or 13+), the user will be prompted to grant:
- **BLUETOOTH_SCAN** & **BLUETOOTH_CONNECT** for BLE detection.
- **POST_NOTIFICATIONS** for showing system alerts.

Ensure these permissions are granted for fall detection and notifications to work properly.

---
