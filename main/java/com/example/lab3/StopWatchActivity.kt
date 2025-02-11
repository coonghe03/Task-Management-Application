package com.example.lab3

import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class StopWatchActivity : AppCompatActivity() {

    private lateinit var chronometer: Chronometer
    private var isRunning = false
    private var timeWhenStopped: Long = 0
    private var savedTimes: MutableList<String> = mutableListOf()
    private lateinit var savedTimesTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stop_watch)

        // Apply window insets listener to adjust the view padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the UI elements
        chronometer = findViewById(R.id.chronometer)
        val startButton: Button = findViewById(R.id.btnStart)
        val stopButton: Button = findViewById(R.id.btnStop)
        val saveButton: Button = findViewById(R.id.btnSave)
        val resetButton: Button = findViewById(R.id.btnReset)
        savedTimesTextView = findViewById(R.id.savedTimesTextView)

        // Start Button
        startButton.setOnClickListener {
            if (!isRunning) {
                chronometer.base = SystemClock.elapsedRealtime() - timeWhenStopped
                chronometer.start()
                isRunning = true
            }
        }

        // Stop Button
        stopButton.setOnClickListener {
            if (isRunning) {
                timeWhenStopped = SystemClock.elapsedRealtime() - chronometer.base
                chronometer.stop()
                isRunning = false
            }
        }

        // Save Button
        saveButton.setOnClickListener {
            if (!isRunning) {
                val elapsedMillis = SystemClock.elapsedRealtime() - chronometer.base
                val seconds = (elapsedMillis / 1000) % 60
                val minutes = (elapsedMillis / 60000) % 60
                val hours = (elapsedMillis / 3600000)

                val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                savedTimes.add(formattedTime)
                updateSavedTimesDisplay()
            }
        }

        // Reset Button
        resetButton.setOnClickListener {
            chronometer.base = SystemClock.elapsedRealtime()
            timeWhenStopped = 0
            chronometer.stop()
            isRunning = false
            savedTimes.clear()
            updateSavedTimesDisplay()
        }
    }

    // Function to update saved times TextView
    private fun updateSavedTimesDisplay() {
        val savedTimesString = savedTimes.joinToString("\n")
        savedTimesTextView.text = savedTimesString
    }
}
