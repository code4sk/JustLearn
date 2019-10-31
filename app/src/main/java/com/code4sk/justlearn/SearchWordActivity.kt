package com.code4sk.justlearn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView

class SearchWordActivity : AppCompatActivity() {

    var searchView: SearchView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_search_word)
        searchView = findViewById(R.id.searchView)
        searchView?.isIconified = false
        searchView?.isIconifiedByDefault = false
    }
}
