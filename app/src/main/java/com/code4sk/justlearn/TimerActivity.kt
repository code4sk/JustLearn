package com.code4sk.justlearn


import RecordDialogFragment
import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class TimerActivity : AppCompatActivity(), RecyclerTouchListener.OnRecyclerTouchListener, NavigationView.OnNavigationItemSelectedListener{

    private lateinit var dialog: Dialog
//    private val randomFileName = "lkjdsfdsafl12"
    private val adapter = RecordingsAdapter(ArrayList())
    private var selectedRecItems = ArrayList<RecItem>()
    private var selectMode = false
    private val recorder = MediaRecorder()
    private var isRecording = false
    private var isPause = false
    private val path = Environment.getExternalStorageDirectory().toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        setSupportActionBar(findViewById(R.id.toolbar))
        val nv = findViewById<NavigationView>(R.id.navigationTimer)
        nv.setNavigationItemSelectedListener(this)
        findViewById<ImageView>(R.id.deleteRec).visibility = View.GONE
        findViewById<FloatingActionButton>(R.id.fab_pause).visibility = View.GONE
        val recList: ArrayList<RecItem> = ArrayList()
        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_rec)
        dialog.setCancelable(false)
        dialog.findViewById<Button>(R.id.saveRec).setOnClickListener {

            Toast.makeText(this, dialog.findViewById<EditText>(R.id.file_name).text.toString(), Toast.LENGTH_SHORT).show()
            val name = "Recordings/${dialog.findViewById<EditText>(R.id.file_name).text.toString()}.m4a"
            Log.d("checkShubham", "lets see${name}")



            File(path, "Recordings/just_learn.m4a").renameTo(File(path, name))
            finish()
            startActivity(intent)
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.cancelRec).setOnClickListener {

            finish()
            startActivity(intent)
            dialog.dismiss()
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(this, permissions, 0)
            Log.d(tag, "perm wrong")
        }
        else{
            val dir = File(path, "Recordings").walk(FileWalkDirection.BOTTOM_UP)
            dir.forEach {
                Log.d(tag, it.toString())
                if(!it.isDirectory) {

                    recList.add(RecItem(it))
                }

            }
        }
        adapter.loadNewData(recList)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addOnItemTouchListener(RecyclerTouchListener(this, recyclerView, this))
        recyclerView.adapter = adapter

    }





    override fun onSingleTap(view: View, position: Int) {
        Toast.makeText(this, "tap", Toast.LENGTH_SHORT).show()
        val newView = view.findViewById<CheckedTextView>(R.id.recordingText)

        if(selectMode){
            if(adapter.getRecording(position) in selectedRecItems){
                selectedRecItems.remove(adapter.getRecording(position))
                newView.isChecked = false
                newView.checkMarkDrawable = null
            } else {
                selectedRecItems.add(adapter.getRecording(position))

                newView.isChecked = true
                newView.setCheckMarkDrawable(R.drawable.our_checkbox)
            }
            adapter.notifyDataSetChanged()

        } else {
                    val uri: Uri = Uri.parse(adapter.getRecording(position).file.absolutePath)
                    playMedia(uri)
        }

    }



    override fun onLongTap(view: View, position: Int) {
        val newView = view.findViewById<CheckedTextView>(R.id.recordingText)
        if(!selectMode){
            selectMode = true
            findViewById<ImageView>(R.id.deleteRec).visibility = View.VISIBLE
            selectedRecItems.add(adapter.getRecording(position))
            newView.isChecked = true
            newView.setCheckMarkDrawable(R.drawable.our_checkbox)
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "long tap", Toast.LENGTH_SHORT).show()
        } else {
            onSingleTap(view, position)
        }

    }

//    fun showRecordDialog() {
//        // Create an instance of the dialog fragment and show it
//        val dialog = RecordDialogFragment()
//        dialog.show(supportFragmentManager, "NoticeDialogFragment")
//    }

    private fun playMedia(file: Uri) {
        Log.d(tag, file.toString())
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(file, "audio/*")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
        else{
            Log.d(tag, "activity not found")
        }
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
        } else if(selectMode){
            selectMode = false
            selectedRecItems.clear()
            finish()
            startActivity(intent)
        }
        else  {
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

    fun onDeleteRec(view: View) {
        selectedRecItems.forEach {
            it.file.delete()

        }
        selectedRecItems.clear()
        finish()
        startActivity(intent)
    }

    fun startRecording(view: View) {
        val newView = view.findViewById<FloatingActionButton>(R.id.fab_record)
        findViewById<FloatingActionButton>(R.id.fab_pause).visibility = View.VISIBLE
        findViewById<FloatingActionButton>(R.id.fab_pause).setImageResource(R.drawable.ic_baseline_pause_24)
        if(!isRecording){

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            recorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
            recorder.setAudioEncodingBitRate(16*44100);
            recorder.setAudioSamplingRate(44100);
            recorder.setOutputFile(File(path, "Recordings/just_learn.m4a").absolutePath)
            recorder.prepare()
            isRecording = true
            newView.setImageResource(R.drawable.ic_baseline_stop_24)
            recorder.start()

        } else {
//            showRecordDialog()

            isRecording = false
            recorder.stop()
            recorder.reset()
//            val recordDialogFragment = RecordDialogFragment()
//            recordDialogFragment.show(supportFragmentManager, "what is this now!!")
            dialog.show()

        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun pauseRecording(view: View){
        val newView = view.findViewById<FloatingActionButton>(R.id.fab_pause)
        if(!isRecording){
            return
        }

        if(isPause){
//            showRecordDialog()

            isPause = false
            newView.setImageResource(R.drawable.ic_baseline_pause_24)
            recorder.resume()
        } else {
            isPause = true

            newView.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            recorder.pause()
        }
    }

//    override fun onDialogPositiveClick(dialog: DialogFragment) {
////        Log.d("checkShubham", dialog.findViewById)
//        val name = "Recordings/${findViewById<EditText>(R.id.file_name).text}.mp3"
//        recorder.setOutputFile(File(path, name).absolutePath)
//        recorder.stop()
//        recorder.reset()
//        dialog.dismiss()
//        finish()
//        startActivity(intent)
//    }
//
//    override fun onDialogNegativeClick(dialog: DialogFragment) {
//        recorder.stop()
//        recorder.reset()
//        dialog.dismiss()
//        finish()
//        startActivity(intent)
//    }
}


