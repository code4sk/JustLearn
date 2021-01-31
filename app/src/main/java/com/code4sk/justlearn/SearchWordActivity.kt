package com.code4sk.justlearn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast

class SearchWordActivity : AppCompatActivity() {

    var searchView: SearchView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_search_word)
        searchView = findViewById(R.id.searchView)
        searchView?.isIconified = false
        searchView?.isIconifiedByDefault = false
        findViewById<EditText>(R.id.addWord).setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    val  text = findViewById<EditText>(R.id.addWord).text
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

}
