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
import android.provider.BaseColumns
import android.util.Log

import androidx.core.content.ContextCompat.getSystemService

class NotificationBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val cid = "1"
        Log.d("checkShubham", "now")
        Log.d("checkShubham", "${cid} ok ")
        val dbHelper = WordsActivity.FeedReaderDbHelper(context!!)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID, WordsActivity.FeedReaderContract.FeedEntry.COLUMN_NAME)

        val temp:ArrayList<String> = ArrayList()
        val sortOrder = "${BaseColumns._ID} DESC"
        val cursor = db.query(
            WordsActivity.FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )

        var n = 0
        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(WordsActivity.FeedReaderContract.FeedEntry.COLUMN_NAME))
                temp.add(name.toString())
                n += 1
            }
        }
        n -= 1
        val rand = (0..n).random()
        if(n > 0){
            val builder = NotificationCompat.Builder(context, cid)
                .setSmallIcon(R.drawable.rounded_search)
                .setContentTitle(temp[rand])
                .setContentText("learning never stops.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//        createNotificationChannel()
//        val intent = Intent(context, SearchWordActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
            val intent = Intent(context, WordsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            builder.setContentIntent(pendingIntent)
            builder.setAutoCancel(true)
            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(1, builder.build())
            }
        }
        temp.clear()

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