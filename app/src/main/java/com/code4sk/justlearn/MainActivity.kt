package com.code4sk.justlearn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

const val tag = "checkShubham"
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val nv = findViewById<NavigationView>(R.id.navigation)
        nv.setNavigationItemSelectedListener(this)
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

}
