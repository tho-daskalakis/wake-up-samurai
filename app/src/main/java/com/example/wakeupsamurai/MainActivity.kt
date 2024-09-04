package com.example.wakeupsamurai

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import android.widget.Toast
import android.content.pm.PackageManager
import android.content.Intent
import android.provider.Settings

class MainActivity : AppCompatActivity() {

    private lateinit var wakeLock: PowerManager.WakeLock
    private val PERMISSIONS_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Request necessary permissions
        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.WAKE_LOCK
                ),
                PERMISSIONS_REQUEST_CODE
            )
        }

        // Initialize WakeLock
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WakeupSamurai::HotspotWakeLock")

        // Find buttons by their IDs
        val startButton: Button = findViewById(R.id.startHotspotButton)
        val stopButton: Button = findViewById(R.id.stopHotspotButton)

        // Set click listeners
        startButton.setOnClickListener {
            startHotspot()
        }

        stopButton.setOnClickListener {
            stopHotspot()
        }
    }

    private fun hasPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startHotspot() {
        // Guide user to hotspot settings
        val intent = Intent()
        intent.action = "android.settings.TETHER_SETTINGS"
        startActivity(intent)
        wakeLock.acquire()
        Toast.makeText(this, "Please enable hotspot manually", Toast.LENGTH_LONG).show()
    }

    private fun stopHotspot() {
        // Guide user to hotspot settings
        val intent = Intent()
        intent.action = "android.settings.TETHER_SETTINGS"
        startActivity(intent)
        if (wakeLock.isHeld) {
            wakeLock.release()
        }
        Toast.makeText(this, "Please disable hotspot manually", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (wakeLock.isHeld) {
            wakeLock.release()
        }
    }
}
