package com.example.multishoppinglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.multishoppinglist.fragments.OfflineFragment
import com.example.multishoppinglist.fragments.OnlineFragment
import com.example.multishoppinglist.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val database = Firebase.database
    private val myRef = database.getReference("message")

    private val offlineFragment = OfflineFragment()
    private val onlineFragment = OnlineFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //toolbar replace with own
        setSupportActionBar(findViewById(R.id.toolbar))

        //open the fragment while loading
        setCurrentFragment(offlineFragment)


        //for navigating fragments from the bottom navigation bar
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_nav_bar)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_offline -> {
                    setCurrentFragment(offlineFragment)
                    Toast.makeText(applicationContext, "this is offline", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_online -> {
                    setCurrentFragment(onlineFragment)
                    Toast.makeText(applicationContext, "this is online", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_profile -> {
                    setCurrentFragment(profileFragment)
                    Toast.makeText(applicationContext, "this is profile", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

    }


    //function to change fragment from activity
    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainFragment, fragment)
            commit()
        }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == R.id.setting)
            Toast.makeText(this,"setting clicked", Toast.LENGTH_SHORT).show()

        return super.onOptionsItemSelected(item)
    }

}
