package com.code4sk.justlearn

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class TimerActivity : AppCompatActivity(), RecyclerTouchListener.OnRecyclerTouchListener {
    val adapter = RecordingsAdapter(ArrayList())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        val recList: ArrayList <File> = ArrayList()
        val path = Environment.getExternalStorageDirectory().toString()
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, permissions,0)
            Log.d(tag, "perm wrong")
        }
        else{
            val dir = File(path, "Recordings").walk(FileWalkDirection.BOTTOM_UP)
            dir.forEach {
                Log.d(tag, it.toString())
                if(!it.isDirectory)
                recList.add(it)
            }
        }
        adapter.loadNewData(recList)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addOnItemTouchListener(RecyclerTouchListener(this, recyclerView, this))
        recyclerView.adapter = adapter
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

    override fun onSingleTap(view: View, position: Int) {
        Toast.makeText(this, "tap", Toast.LENGTH_SHORT).show()
        val uri: Uri = Uri.parse(adapter.getRecording(position).absolutePath)
        playMedia(uri)
    }

    override fun onLongTap(view: View, position: Int) {
        Toast.makeText(this, "long tap", Toast.LENGTH_SHORT).show()
    }

    private fun playMedia(file: Uri) {
        Log.d(tag, file.toString())
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(file, "audio/*")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
        else{
            Log.d(tag,"activity not found")
        }
    }

}
