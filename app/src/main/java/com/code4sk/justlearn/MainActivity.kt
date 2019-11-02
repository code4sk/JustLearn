package com.code4sk.justlearn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

const val tag = "checkShubham"
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val manager = supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawerLayout)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toggle.syncState()
        val nv = findViewById<NavigationView>(R.id.navigation)
        nv.setNavigationItemSelectedListener(this)
        showFragment()
    }

    private fun showFragment(){
        val transaction = manager.beginTransaction()
        val fragment = NavigationFragment()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack("fragment_backstack")
        transaction.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menuTimer -> {
                launchTimerActivity()
                item.isChecked = true
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
        val transaction = manager.beginTransaction()
        val fragment = TimerFragment(this)
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack("fragment_backstack")
        transaction.commit()
        this.drawerLayout.closeDrawer(GravityCompat.START)
    }

}
