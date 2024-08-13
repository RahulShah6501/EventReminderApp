package com.example.eventreminderapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class ReminderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        val eventName = intent.getStringExtra("eventName")
        val eventDate = intent.getStringExtra("eventDate")
        val eventTime = intent.getStringExtra("eventTime")

        if (eventName != null && eventDate != null && eventTime != null) {
            val countdownFragment = CountdownFragment()
            val bundle = Bundle()
            bundle.putString("eventName", eventName)
            bundle.putString("eventDate", eventDate)
            bundle.putString("eventTime", eventTime)
            countdownFragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(R.id.countdownFragment, countdownFragment)
                .commit()
        }
    }
}
