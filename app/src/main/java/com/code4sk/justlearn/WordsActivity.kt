package com.code4sk.justlearn

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.media.MediaRecorder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.BaseColumns
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class WordsActivity : AppCompatActivity(), RecyclerTouchListener.OnRecyclerTouchListener, NavigationView.OnNavigationItemSelectedListener {

    //    private val randomFileName = "lkjdsfdsafl12"
    class Duo(v1:String, v2:Boolean){
        var name = v1
        var check = v2

    }
    private var selectedWordItems = ArrayList<Duo>()
    private var selectMode = false
    private var sortMode = false

    private val path = Environment.getExternalStorageDirectory().toString()
    private val adapter = WordsAdapter(ArrayList())

    object FeedReaderContract {
        // Table contents are grouped together in an anonymous object.
        object FeedEntry : BaseColumns {
            const val TABLE_NAME = "words"
            const val COLUMN_NAME = "name"
        }
    }



    class FeedReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${FeedReaderContract.FeedEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${FeedReaderContract.FeedEntry.COLUMN_NAME} TEXT)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FeedReaderContract.FeedEntry.TABLE_NAME}"
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            onUpgrade(db, oldVersion, newVersion)
        }
        companion object {
            // If you change the database schema, you must increment the database version.
            const val DATABASE_VERSION = 1
            const val DATABASE_NAME = "FeedReader.db"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_words)
        supportActionBar?.hide()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            val w = window
//            w.setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//            )
//        }
        findViewById<ImageView>(R.id.deleteWord).visibility = View.GONE
        val nv = findViewById<NavigationView>(R.id.navigationWords)
        nv.setNavigationItemSelectedListener(this)

        val recList = ArrayList<Duo>()

        val dbHelper = FeedReaderDbHelper(this)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID, FeedReaderContract.FeedEntry.COLUMN_NAME)


        val sortOrder = "${BaseColumns._ID} DESC"
        val cursor = db.query(
            FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME))
                val item = Duo(name, false)
                recList.add(item)
            }
        }
        db.close()
//        Log.d("checkShubham", recList[0])
        adapter.loadNewData(recList)
        val recyclerView = findViewById<RecyclerView>(R.id.wordRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addOnItemTouchListener(RecyclerTouchListener(this, recyclerView, this))
        recyclerView.adapter = adapter
    }
    override fun onSingleTap(view: View, position: Int) {
//        Toast.makeText(this, "tap", Toast.LENGTH_SHORT).show()
//        val newView = view.findViewById<CheckedTextView>(R.id.wordText)
//        Log.d("checkShubham", newView.text.toString())
        if(selectMode){

            if(adapter.getWord(position) in selectedWordItems){

                selectedWordItems.remove(adapter.getWord(position))
                adapter.getWord(position).check = false
                adapter.notifyDataSetChanged()
            } else {
                Log.d("checkShubham", "1")
                selectedWordItems.add(adapter.getWord(position))
                adapter.getWord(position).check = true
                adapter.notifyDataSetChanged()
//                Log.d("checkShubham", "${newView.isChecked} ${newView.checkMarkDrawable}")
            }


        } else {

        }

    }



    override fun onLongTap(view: View, position: Int) {
        val newView = view.findViewById<CheckedTextView>(R.id.wordText)
        Log.d("checkShubham", newView.text.toString())
        if(!selectMode){
            selectMode = true
            findViewById<ImageView>(R.id.deleteWord).visibility = View.VISIBLE
            selectedWordItems.add(adapter.getWord(position))
//            newView.isChecked = true
//            newView.setCheckMarkDrawable(R.drawable.our_checkbox)
            adapter.getWord(position).check = true
            adapter.notifyDataSetChanged()
//            Toast.makeText(this, "long tap", Toast.LENGTH_SHORT).show()
        } else {
            onSingleTap(view, position)
        }

    }

    override fun onBackPressed() {
        Log.d(tag, "${drawerLayout.isDrawerOpen(GravityCompat.START)}")
        if(this.drawerLayout.isDrawerOpen(GravityCompat.START)){
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else if(selectMode){
            selectMode = false
            selectedWordItems.clear()
            finish()
            startActivity(intent)
        }
        else  {
            super.onBackPressed()
        }
    }

    fun onDeleteWord(view: View) {
        val dbHelper = FeedReaderDbHelper(this)
        val db = dbHelper.writableDatabase
        val selection = "${FeedReaderContract.FeedEntry.COLUMN_NAME} LIKE ?"
// Specify arguments in placeholder order.

// Issue SQL statement.

        selectedWordItems.forEach {
            val selectionArgs = arrayOf(it.name)
            val deletedRows = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs)
        }

        selectedWordItems.clear()
        finish()
        startActivity(intent)
    }
    fun onSortWords(view: View){
        val recList = ArrayList<Duo>()
        selectedWordItems.clear()
        val dbHelper = FeedReaderDbHelper(this)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID, FeedReaderContract.FeedEntry.COLUMN_NAME)
        var sortOrder = ""
        if(!sortMode){
            sortOrder = "${FeedReaderContract.FeedEntry.COLUMN_NAME} DESC"
        } else {
            sortOrder = "${BaseColumns._ID} DESC"
        }
        sortMode = !sortMode
        val cursor = db.query(
            FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME))
                val item = Duo(name, false)
                recList.add(item)
            }
        }

        db.close()
        if(!sortMode){
            val file = File(path, "just_learn.txt")
            FileOutputStream(file).use{
                it.write("just_learn\n".toByteArray())
            }
            for(key in recList){
                file.appendText("${key.name}\n")
            }
        } else {
            recList.reverse()
        }



//        Log.d("checkShubham", recList[0])
        adapter.loadNewData(recList)
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
    fun openAddWord(view: View){
        intent = Intent(this, SearchWordActivity::class.java)
        startActivity(intent)
        finish()
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
}


