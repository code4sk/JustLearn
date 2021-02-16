package com.code4sk.justlearn

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_search_word.*


class SearchWordActivity : AppCompatActivity() {

    var searchView: SearchView? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_search_word)
//        searchView = findViewById(R.id.searchView)
//        searchView?.isIconified = false
//        searchView?.isIconifiedByDefault = false
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            val w = window
//            w.setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//            )
//        }
        findViewById<EditText>(R.id.addWord).setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    val text = findViewById<EditText>(R.id.addWord).text
                    addWord(text.toString())
//                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

    }

    fun addWordBtn(view: View){
        val text = findViewById<EditText>(R.id.addWord).text
        addWord(text.toString())
    }

    fun addWord(word: String){
        val dbHelper = WordsActivity.FeedReaderDbHelper(this)
        val db = dbHelper.writableDatabase
        val selectionArgs = arrayOf(word)
        val selection = "${WordsActivity.FeedReaderContract.FeedEntry.COLUMN_NAME} LIKE ?"
        val deletedRows = db.delete(WordsActivity.FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs)
        val values = ContentValues().apply {
            put(WordsActivity.FeedReaderContract.FeedEntry.COLUMN_NAME, word)
        }


        val newRowId = db?.insert(
            WordsActivity.FeedReaderContract.FeedEntry.TABLE_NAME,
            null,
            values
        )
        Log.d("checkShubham", newRowId.toString())
        intent = Intent(this, WordsActivity::class.java)
        startActivity(intent)
        finish()
    }

}
