package com.code4sk.justlearn

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast




class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
//        val i = Intent(context, MyService::class.java)
//        context!!.startService(i)

        Toast.makeText(context, "Booting Completed", Toast.LENGTH_LONG).show()

    }
}