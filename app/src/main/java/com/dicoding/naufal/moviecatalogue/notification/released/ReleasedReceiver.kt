package com.dicoding.naufal.moviecatalogue.notification.released

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dicoding.naufal.moviecatalogue.R
import com.dicoding.naufal.moviecatalogue.data.remote.network.MovieCatalogDataSource
import com.dicoding.naufal.moviecatalogue.data.remote.network.Result
import com.dicoding.naufal.moviecatalogue.ui.detail.movie.DetailMovieActivity
import com.dicoding.naufal.moviecatalogue.ui.detail.tv.DetailTvShowActivity
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.text.SimpleDateFormat
import java.util.*


class ReleasedReceiver : BroadcastReceiver(), KoinComponent {

    private val mDataSource: MovieCatalogDataSource by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        val s = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val curDate = s.format(Date())
        CoroutineScope(Dispatchers.Main).launch{
            val movieResponse = withContext(Dispatchers.IO) {
                mDataSource.fetchReleaseMovie(curDate)
            }
            val tvResponse = withContext(Dispatchers.IO) {
                mDataSource.fetchReleaseTv(curDate)
            }

            when(movieResponse){
                is Result.Success -> {
                    for (f in movieResponse.data){
                        showReleaseReminder(context, f.id, f.title.toString(), 1, NOTIF_ID)
                        NOTIF_ID++
                    }
                }
            }

            when(tvResponse){
                is Result.Success -> {
                    for (f in tvResponse.data){
                        showReleaseReminder(context, f.id, f.title.toString(), 2, NOTIF_ID)
                        NOTIF_ID++
                    }
                }
            }

            NOTIF_ID = 1000
        }


    }

    fun setReleasedReminder(context: Context?) {


        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReleasedReceiver::class.java)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASED_REMINDER, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelReleasedReminder(context: Context?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReleasedReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASED_REMINDER, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
    }

    private fun showReleaseReminder(
        context: Context?,
        id: Int,
        title: String,
        type: Int,
        notifId: Int
    ) {
        val notificationManagerCompat = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val intent: Intent
        val notifTitle: String
        val notifMessage: String
        if (type == 1) {
            intent = DetailMovieActivity.newIntent(context, id)
            notifTitle = context.getString(R.string.new_movie_notification)
            notifMessage = context.getString(R.string.new_movie, title)
        } else {
            intent = DetailTvShowActivity.newIntent(context, id)
            notifTitle = context.getString(R.string.new_tv_notification)
            notifMessage = context.getString(R.string.new_tv, title)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notifId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_movie_black_24dp)
            .setContentTitle(notifTitle)
            .setContentText(notifMessage)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            channel.enableVibration(true)
            channel.lightColor = Color.WHITE
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)

    }

    companion object {
        private const val CHANNEL_ID = "CHANNEL_2"
        private const val CHANNEL_NAME = "Released Reminder Notification"
        private const val ID_RELEASED_REMINDER = 101
        private var NOTIF_ID = 1000
    }
}