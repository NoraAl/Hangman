package edu.umsl.hangman

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

import android.app.NotificationManager.IMPORTANCE_DEFAULT
import edu.umsl.hangman.R.mipmap.ic_launcher


class NotificationReceiver : BroadcastReceiver() {
    companion object {
        private val CHANNEL = "HangmanChannelId"
        const private val NAME = "Hangman"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.e("AlarmReceiver", "onReceive()")
        val notificationIntent = Intent(context, MainView::class.java)
        val launchPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = Notification.Builder(context)
        val notification = builder.setContentTitle("Hangman App")
                .setAutoCancel(true)
                .setContentText("Do you want to play again?")
                .setTicker("New Notification")
                .setSmallIcon(ic_launcher)
                .setContentIntent(launchPendingIntent).build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL)
            val channel = NotificationChannel(CHANNEL, NAME, IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notification)
    }

}
