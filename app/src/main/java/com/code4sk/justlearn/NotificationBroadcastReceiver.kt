package com.code4sk.justlearn

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.content.res.Resources.getSystem
import androidx.core.content.ContextCompat.getSystemService

class NotificationBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val builder = NotificationCompat.Builder(context!!, getSystem().getString(R.string.CHANNEL_ID))
            .setSmallIcon(R.drawable.rounded_search)
            .setContentTitle("finish")
            .setContentText("done")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//        createNotificationChannel()
//        val intent = Intent(context, SearchWordActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        builder.setContentIntent(pendingIntent)
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }
    }
//    private fun createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = getSystem().getString(R.string.channel_name)
//            val descriptionText = getSystem().getString(R.string.channel_description)
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(getSystem().getString(R.string.CHANNEL_ID), name, importance).apply {
//                description = descriptionText
//            }
//            // Register the channel with the system
//            val notificationManager: NotificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
}