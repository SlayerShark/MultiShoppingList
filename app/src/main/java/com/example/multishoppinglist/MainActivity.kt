package com.example.multishoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.multishoppinglist.databinding.ActivityMainBinding
import com.example.multishoppinglist.fragments.OfflineFragment
import com.example.multishoppinglist.fragments.OnlineFragment
import com.example.multishoppinglist.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth;

    private val offlineFragment = OfflineFragment()
    private val onlineFragment = OnlineFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = Firebase.auth

        //toolbar replace with own
        setSupportActionBar(findViewById(R.id.toolbar))

        //open the fragment while loading
        setCurrentFragment(offlineFragment)


        binding.bottomNavBar.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_offline -> {
                    setCurrentFragment(offlineFragment)
                }
                R.id.nav_online -> {
                    setCurrentFragment(onlineFragment)
                }
                R.id.nav_profile -> {
                    setCurrentFragment(profileFragment)
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
        if (item.itemId == R.id.logout)
            logout()
            Toast.makeText(this,"setting clicked", Toast.LENGTH_SHORT).show()

        return true
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            println("1")
        }

        else{
            val intent = Intent(this, LoginActivity::class.java)
            this.startActivity(intent)
            finish()

        }
    }




 
    fun logout() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
    }
}
