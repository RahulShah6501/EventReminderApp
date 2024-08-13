package com.example.eventreminderapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class CountdownFragment : Fragment() {

    private lateinit var eventNameTextView: TextView
    private lateinit var countdownTextView: TextView
    private var eventTimeInMillis: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_countdown, container, false)
        eventNameTextView = view.findViewById(R.id.eventNameTextView)
        countdownTextView = view.findViewById(R.id.countdownTextView)

        val eventName = arguments?.getString("eventName") ?: "Event"
        val eventDate = arguments?.getString("eventDate") ?: ""
        val eventTime = arguments?.getString("eventTime") ?: ""

        // Set the event name
        eventNameTextView.text = eventName

        if (eventDate.isNotEmpty() && eventTime.isNotEmpty()) {
            try {
                val sdf = SimpleDateFormat("MMddyyyy HH:mm", Locale.getDefault())
                val eventDateTime = "$eventDate $eventTime"
                val eventDateTimeInMillis = sdf.parse(eventDateTime)?.time ?: 0L

                val currentTimeInMillis = System.currentTimeMillis()
                eventTimeInMillis = eventDateTimeInMillis - currentTimeInMillis

                startCountdown(eventTimeInMillis)

                val earlyReminderTimeInMillis = eventTimeInMillis - 10 * 60 * 1000 // 10 minutes in milliseconds
                startEarlyReminderCountdown(earlyReminderTimeInMillis)

            } catch (e: Exception) {
                countdownTextView.text = "Invalid Date/Time format"
            }
        } else {
            countdownTextView.text = "Date/Time not provided"
        }

        return view
    }

    private fun startCountdown(timeInMillis: Long) {
        object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = millisUntilFinished / (1000 * 60 * 60)
                val minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60)
                val seconds = (millisUntilFinished % (1000 * 60)) / 1000

                countdownTextView.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            }

            override fun onFinish() {
                countdownTextView.text = "Event Started"
                showNotification(eventNameTextView.text.toString(), "The event '${eventNameTextView.text}' is starting now!")
            }
        }.start()
    }

    private fun startEarlyReminderCountdown(timeInMillis: Long) {
        if (timeInMillis > 0) {
            object : CountDownTimer(timeInMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                }

                override fun onFinish() {
                    showNotification(eventNameTextView.text.toString(), "Reminder: The event '${eventNameTextView.text}' starts in 10 minutes!")
                }
            }.start()
        }
    }

    private fun showNotification(eventName: String, contentText: String) {
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(requireContext(), 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(requireContext(), "eventReminderChannelID")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Event Reminder")
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify((System.currentTimeMillis() / 1000).toInt(), builder.build())
    }
}
