package com.example.eventreminderapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        val eventNameInput = findViewById<EditText>(R.id.eventNameInput)
        val eventDateInput = findViewById<EditText>(R.id.eventDateInput)
        val eventTimeInput = findViewById<EditText>(R.id.eventTimeInput)
        val submitButton = findViewById<Button>(R.id.submitButton)

        submitButton.setOnClickListener {
            val eventName = eventNameInput.text.toString()
            val eventDate = eventDateInput.text.toString()
            val eventTime = eventTimeInput.text.toString()

            val intent = Intent(this, ReminderActivity::class.java).apply {
                putExtra("eventName", eventName)
                putExtra("eventDate", eventDate)
                putExtra("eventTime", eventTime)
            }
            startActivity(intent)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "EventReminderChannel"
            val descriptionText = "Channel for event reminder notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("eventReminderChannelID", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
