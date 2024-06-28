package com.example.plantparenthood.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.plantparenthood.Plant
import com.example.plantparenthood.R
import com.google.firebase.auth.FirebaseAuth

object NotificationUtils {

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(context: Context, plant: Plant, daysBetweenWatering: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val notificationIntent = Intent(context, NotificationReceiver::class.java)
        notificationIntent.putExtra("plantName", plant.name)
        notificationIntent.putExtra("plantOwner", plant.ownerId)

        val pendingIntent = PendingIntent.getBroadcast(context, plant.documentId.hashCode(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val daysInMillis = 1000 * 60 * 60 * 24
        val alarmTime = plant.wateringTime.toDate().time + daysBetweenWatering * daysInMillis
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
    }

    fun cancelNotification(context: Context, plantId: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(context, NotificationReceiver::class.java)
        val requestCode = plantId.hashCode()
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, notificationIntent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE)

        pendingIntent?.let {
            alarmManager.cancel(it)
            it.cancel()
        }
    }
}

class NotificationReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        val plantName = intent.getStringExtra("plantName")
        val plantOwner = intent.getStringExtra("plantOwner")
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUser == plantOwner) {
            showNotification(context, plantName!!)
        }
    }

    private fun showNotification(context: Context, plantName: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("plant_notifications", "Plant Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, "plant_notifications")
            .setContentTitle(plantName)
            .setContentText("Water me!")
            .setSmallIcon(R.drawable.logo_transparent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}
