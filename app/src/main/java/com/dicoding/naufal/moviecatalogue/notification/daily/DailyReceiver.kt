package com.dicoding.naufal.moviecatalogue.notification.daily

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dicoding.naufal.moviecatalogue.R
import com.dicoding.naufal.moviecatalogue.ui.main.MainActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class DailyReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        showDailyReminder(context)
    }

    fun setDailyReminder(context: Context?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReceiver::class.java)


        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        if(System.currentTimeMillis() > calendar.timeInMillis)
        {
            //jika jam saat ini sudah melebihi jam settingan pada saat notifikasi pertama kali dibuat maka
            calendar.add(Calendar.DATE, 1)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY_REMINDER, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelDailyReminder(context: Context?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY_REMINDER, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
    }

    private fun showDailyReminder(context: Context?) {
        val notificationManagerCompat = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_movie_black_24dp)
            .setContentTitle(context.getString(R.string.daily_reminder))
            .setContentText(context.getString(R.string.daily_reminder_message))
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(ID_DAILY_REMINDER, notification)

    }

    companion object {
        private const val CHANNEL_ID = "CHANNEL_1"
        private const val CHANNEL_NAME = "Daily Reminder Notification"
        private const val ID_DAILY_REMINDER = 100
    }
}