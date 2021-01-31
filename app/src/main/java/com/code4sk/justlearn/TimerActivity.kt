package com.code4sk.justlearn


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.view.ActionMode
import android.view.View
import android.view.MenuItem
import android.view.MenuInflater
import android.widget.LinearLayout
import android.widget.Toast
import android.view.Menu
import android.widget.CheckedTextView
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class TimerActivity : AppCompatActivity(), RecyclerTouchListener.OnRecyclerTouchListener, NavigationView.OnNavigationItemSelectedListener {

    private val adapter = RecordingsAdapter(ArrayList())
    private var selectedRecItems = ArrayList<RecItem>()
    private var actionMode: ActionMode? = null
    private var tracker: SelectionTracker<RecItem>? = null
    private var selectMode = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        setSupportActionBar(findViewById(R.id.toolbar))
        val nv = findViewById<NavigationView>(R.id.navigationTimer)
        nv.setNavigationItemSelectedListener(this)
        findViewById<ImageView>(R.id.deleteRec).visibility = View.GONE
        val recList: ArrayList <RecItem> = ArrayList()
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
            //        val uri: Uri = Uri.parse(adapter.getRecording(position).file.absolutePath)
            //        playMedia(uri)
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
        finish()
        startActivity(intent)
    }
}


