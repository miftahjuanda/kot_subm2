package com.kelaspemula.submission1.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.kelaspemula.submission1.Activity.MainActivity
import com.kelaspemula.submission1.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmSetting : BroadcastReceiver() {
    companion object {
        const val REMINDER = "reminder"
        const val MESSAGE = "message"
        const val TYPE = "type"

        private const val ID = 100

        // private const val TIME = "10:14"
        private const val TIME_FORMAT = "HH:mm"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(MESSAGE)
        showNotification(context, message)
    }

    private fun showNotification(context: Context, message: String) {
        val id = "id"
        val name = "name"

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notifManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, id)
            .setSmallIcon(R.drawable.ic_access_alarm)
            .setContentTitle("Daily Reminder Github")
            .setContentText(message)
            .setVibrate(longArrayOf(1000, 1000, 1000))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelNotif = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
            builder.setChannelId(id)
            channelNotif.enableVibration(true)
            channelNotif.vibrationPattern = longArrayOf(1000, 1000, 1000)
            notifManager.createNotificationChannel(channelNotif)
        }
        val notification = builder.build()
        notifManager.notify(100, notification)
    }

    fun cancelReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmSetting::class.java)
        val request = ID
        val pendingIntent = PendingIntent.getBroadcast(context, request, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Reminder is cancelled", Toast.LENGTH_SHORT).show()
    }

    fun setNotification(context: Context, type: String, alarm: String, message: String) {
        if (isDateInvalid(alarm, TIME_FORMAT)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmSetting::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(MESSAGE, message)
        intent.putExtra(TYPE, type)
        val timeArray = alarm.split(":").toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)
        val pendingIntent =
            PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_ONE_SHOT)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(context, "Reminder set up", Toast.LENGTH_SHORT).show()
    }

    private fun isDateInvalid(time: String, format: String): Boolean {
        return try {
            val dateFormat = SimpleDateFormat(format, Locale.getDefault())
            dateFormat.isLenient = false
            dateFormat.parse(time)
            false
        } catch (e: ParseException) {
            true
        }
    }
}