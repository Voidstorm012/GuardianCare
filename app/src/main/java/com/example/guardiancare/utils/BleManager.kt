package com.example.guardiancare.utils

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothStatusCodes
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.ParcelUuid
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import java.util.UUID

class BleManager private constructor(context: Context) {
    private val context: Context = context.applicationContext
    private var bluetoothAdapter: BluetoothAdapter
    private var bluetoothLeScanner: BluetoothLeScanner
    @RequiresApi(Build.VERSION_CODES.O)
    private val notificationHelper: NotificationHelper = NotificationHelper.getInstance(context)
    private var deviceAddress: String? = null
    private var bluetoothGatt: BluetoothGatt? = null
    private var onDataReceived: ((String) -> Unit)? = null
    private var onConnectionStateChange: ((Boolean) -> Unit)? = null

    companion object {
        private const val TAG = "BleManager"
        private const val REQUEST_ENABLE_BT = 1

        private const val DEVICE_NAME = "M5StickC-Plus"
        private val FALL_SERVICE_UUID = UUID.fromString("12345678-1234-5678-1234-56789abcdef0")
        private val FALL_CHARACTERISTIC_UUID = UUID.fromString("87654321-4321-6789-4321-abcdef987654")

        private const val DELIMITER = "&"
        private const val BLE_FALL = "ble_ono"
        private const val BLE_FALSE_ALARM = "ble_fal"
        private const val BLE_RECOVER = "ble_rcv"

        @Volatile
        private var instance: BleManager? = null

        fun getInstance(context: Context): BleManager {
            return instance ?: synchronized(this) {
                instance ?: BleManager(context.applicationContext).also { instance = it }
            }
        }
    }

    init {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
    }

    fun setDataCallback(callback: (String) -> Unit) {
        onDataReceived = callback
    }

    fun setConnectionCallback(callback: (Boolean) -> Unit) {
        onConnectionStateChange = callback
    }

    fun startScanning(activity: Activity) {
        if (!checkPermissions(activity)) {
            return
        }

        val scanFilter = ScanFilter.Builder()
            .setServiceUuid(ParcelUuid(FALL_SERVICE_UUID))
            .build()

        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        try {
            bluetoothLeScanner.startScan(
                listOf(scanFilter),
                scanSettings,
                scanCallback
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error starting scan: ${e.message}")
        }
    }

    fun stopScanning() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        bluetoothLeScanner.stopScan(scanCallback)
    }

    private fun checkPermissions(activity: Activity): Boolean {
        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN
        )

        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, permissions, REQUEST_ENABLE_BT)
                return false
            }
        }
        return true
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if (result.device.name == DEVICE_NAME) {
                deviceAddress = result.device.address
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }

                bluetoothGatt = result.device.connectGatt(
                    context,
                    false,
                    gattCallback
                )
                stopScanning()
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e(TAG, "Scan failed with error: $errorCode")
        }
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    onConnectionStateChange?.invoke(true)
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    gatt.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    onConnectionStateChange?.invoke(false)
                    startScanning(context as Activity)
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val characteristic = gatt
                    .getService(FALL_SERVICE_UUID)
                    ?.getCharacteristic(FALL_CHARACTERISTIC_UUID)

                if (characteristic != null) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    gatt.setCharacteristicNotification(characteristic, true)
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            if (characteristic.uuid == FALL_CHARACTERISTIC_UUID) {
                val data = String(value)
                val parts = data.split(DELIMITER)

                if (parts.isNotEmpty()) {
                    when (parts[0]) {
                        BLE_FALL -> {
                            val userId = if (parts.size > 1) parts[1] else ""
                            val message = if (userId.isNotEmpty()) "Fall detected for user $userId!"
                            else "Fall detected!"
                            onDataReceived?.invoke(message)
                            notificationHelper.showFallDetectionNotification(userId)
                        }
                        BLE_RECOVER -> {
                            val userId = if (parts.size > 1) parts[1] else ""
                            val message = if (userId.isNotEmpty()) "User $userId recovered from fall."
                            else "User recovered from fall."
                            onDataReceived?.invoke(message)
                        }
                        BLE_FALSE_ALARM -> {
                            val userId = if (parts.size > 1) parts[1] else ""
                            val message = if (userId.isNotEmpty()) "User $userId sent out a false alarm!"
                            else "User sent out a false alarm!"
                            onDataReceived?.invoke(message)
                        }
                        else -> onDataReceived?.invoke("Connected to M5StickC-Plus")
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun sendMessage(message: String): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }

        val characteristic = bluetoothGatt
            ?.getService(FALL_SERVICE_UUID)
            ?.getCharacteristic(FALL_CHARACTERISTIC_UUID)

        return if (characteristic != null) {
            try {
                bluetoothGatt?.writeCharacteristic(
                    characteristic,
                    message.toByteArray(),
                    BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                ) == BluetoothStatusCodes.SUCCESS
            } catch (e: Exception) {
                Log.e(TAG, "Error sending message: ${e.message}")
                false
            }
        } else {
            Log.e(TAG, "Characteristic not found")
            false
        }
    }

    fun disconnect() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        bluetoothGatt?.close()
        bluetoothGatt = null
    }

    fun isConnected(): Boolean {
        return bluetoothGatt != null && bluetoothGatt?.services != null
    }

}

