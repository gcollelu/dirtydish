package com.dirtydish.app.dirtydish

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main_menu.*
import kotlinx.android.synthetic.main.app_bar_main_menu.*

class MainMenu : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        setSupportActionBar(toolbar)

        if (FirebaseAuth.getInstance().currentUser == null)
            startActivity(Intent(this, AuthActivity::class.java))

//        if (!Session.hasHouse())
//            startActivity(Intent(this, SelectHouseActivity::class.java))

        addFragment(HomeFragment(), R.id.content_frame)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
//        when (item.itemId) {
//            R.id.nav_home -> {
//            }
//            R.id.nav_house -> {
////                val intent = Intent(this, ViewHouseFragment::class.java)
////                startActivity(intent)
//                val viewHouse = ViewHouseFragment()
//                replaceFragment(viewHouse, R.id.content_frame)
//            }
//            R.id.nav_chores -> {
//                val intent = Intent(this, ChoresFragment::class.java)
//                startActivity(intent)
//            }
//            R.id.nav_shared_supplies -> {
//                //for testing purposes
//                val intent = Intent(this, SetupHouseActivity::class.java)
//                startActivity(intent)
//            }
//            R.id.nav_logout -> {
//                FirebaseAuth.getInstance().signOut()
//                val intent = Intent(this, AuthActivity::class.java)
//                startActivity(intent)
//            }
//        }

        if (item.itemId == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }


        val fragment: Fragment = when (item.itemId) {
            R.id.nav_home -> HomeFragment()
            R.id.nav_house -> ViewHouseFragment()
            R.id.nav_chores -> ChoresFragment()
            R.id.nav_shared_supplies -> SharedSuppliesFragment()
            else -> HomeFragment()
        }


        replaceFragment(fragment, R.id.content_frame)
        item.isChecked = true
        item.title

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction { add(frameId, fragment) }
    }

    fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction { replace(frameId, fragment) }
    }


}

