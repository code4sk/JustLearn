package com.code4sk.justlearn

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


const val tag = "checkShubham"
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            val w = window
//            w.setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//            )
//        }
        val nv = findViewById<NavigationView>(R.id.navigation)
        nv.setNavigationItemSelectedListener(this)
        val builder = NotificationCompat.Builder(this, getString(R.string.CHANNEL_ID))
            .setSmallIcon(R.drawable.rounded_search)
            .setContentTitle("new notification")
            .setContentText("text")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        createNotificationChannel()
//        val intent = Intent(this, SearchWordActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
//        builder.setContentIntent(pendingIntent)
//        with(NotificationManagerCompat.from(this)) {
//            // notificationId is a unique int for each notification that you must define
//            notify(1, builder.build())
//        }
        setAlarm()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        Log.d(tag, "${item.itemId} ----- ${R.id.menuTimer} ")
        return when(item.itemId){
            R.id.menuTimer -> {
                launchTimerActivity()
                true
            }
            R.id.menuWords -> {
                launchWordsActivity()
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onBackPressed() {
        Log.d(tag, "${drawerLayout.isDrawerOpen(GravityCompat.START)}")
        if(this.drawerLayout.isDrawerOpen(GravityCompat.START)){
            this.drawerLayout.closeDrawer(GravityCompat.START)
        }
        else {
            super.onBackPressed()
        }
    }

    fun launchSearchWordActivity(view: View) {
        startActivity(Intent(this, SearchWordActivity::class.java))
    }

    fun openDrawer(view: View) {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun launchTimerActivity(){
        startActivity(Intent(this, TimerActivity::class.java))
    }
    private fun launchWordsActivity(){
        startActivity(Intent(this, WordsActivity::class.java))
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(getString(R.string.CHANNEL_ID), name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun setAlarm(){
        val manager:AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.HOUR_OF_DAY, 1)
//        calendar.set(Calendar.MINUTE, 23)

        val myIntent = Intent(this, NotificationBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0)
        Log.d("checkShubham", AlarmManager.INTERVAL_FIFTEEN_MINUTES.toString())
//        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 1000, pendingIntent)
//        manager.setInexactRepeating(
//            AlarmManager.ELAPSED_REALTIME_WAKEUP,
//            SystemClock.elapsedRealtime() + 1000,
//            AlarmManager.INTERVAL_HALF_HOUR,
//            pendingIntent
//        )
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 1)
            set(Calendar.MINUTE, 30)
        }

// setRepeating() lets you specify a precise custom interval--in this case,
// 20 minutes.
//        manager.setRepeating(
//            AlarmManager.ELAPSED_REALTIME_WAKEUP,
//            calendar.timeInMillis,
//            1000 ,
//            pendingIntent
//        )
        manager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 1000,
            1000,
            pendingIntent
        )
    }
}
