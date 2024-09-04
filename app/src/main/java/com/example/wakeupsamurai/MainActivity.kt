package com.example.wakeupsamurai

import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

    private fun startHotspot() {
        // Code to start the hotspot (to be implemented)
        wakeLock.acquire()
    }

    private fun stopHotspot() {
        // Code to stop the hotspot (to be implemented)
        if (wakeLock.isHeld) {
            wakeLock.release()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (wakeLock.isHeld) {
            wakeLock.release()
        }
    }
}
