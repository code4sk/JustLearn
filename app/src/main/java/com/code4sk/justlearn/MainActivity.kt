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

const val logTag = "checkShubham"
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
        Log.d(logTag, "${drawerLayout.isDrawerOpen(GravityCompat.START)}")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(logTag, "$requestCode ----- $resultCode $data")
    }

    fun recordFabListener(view: View){
//        val fileUri = File(Environment.getExternalStorageDirectory().absolutePath,
//            "myVoice.mp3")
//        val pathUri = File(fileUri.path)
//        val path = Uri.fromFile(pathUri)
//        Log.d(logTag, path.toString())
//        val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, path)
//
//        startActivityForResult(intent, 200)
        val transaction = manager.beginTransaction()
        val fragment = RecorderFragment()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack("fragment_backstack")
        transaction.commit()
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
