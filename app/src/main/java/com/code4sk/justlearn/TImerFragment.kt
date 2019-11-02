package com.code4sk.justlearn

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class TimerFragment(private val ctx: Activity): Fragment(), RecyclerTouchListener.OnRecyclerTouchListener {
    private val adapter = RecordingsAdapter(ArrayList())
    private val cont = context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recList: ArrayList <File> = ArrayList()
//        toolbar.background = R.color.emerald
        val path = Environment.getExternalStorageDirectory().toString()
        if (ContextCompat.checkSelfPermission(ctx,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(ctx,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(ctx, permissions,0)
            Log.d(com.code4sk.justlearn.logTag, "perm wrong")
        }
        else{
            val dir = File(path, "Recordings").walk(FileWalkDirection.BOTTOM_UP)
            dir.forEach {
                if(!it.isDirectory)
                    recList.add(it)
            }
        }
        adapter.loadNewData(recList)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(ctx)
        recyclerView.addOnItemTouchListener(RecyclerTouchListener(ctx, recyclerView, this))
        recyclerView.adapter = adapter
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timer, container, false)
//        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onSingleTap(view: View, position: Int) {
        Toast.makeText(this.ctx, "tap", Toast.LENGTH_SHORT).show()
        val uri: Uri = Uri.parse(adapter.getRecording(position).absolutePath)
        playMedia(uri)
    }

    override fun onLongTap(view: View, position: Int) {
        Toast.makeText(this.ctx, "long tap", Toast.LENGTH_SHORT).show()
    }

    private fun playMedia(file: Uri) {
        Log.d(com.code4sk.justlearn.logTag, file.toString())
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(file, "audio/*")
        }
        if (intent.resolveActivity(this.ctx.packageManager) != null) {
            startActivity(intent)
        }
        else{
            Log.d(com.code4sk.justlearn.logTag,"activity not found")
        }
    }
}
