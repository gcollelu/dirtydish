package com.dirtydish.app.dirtydish

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dirtydish.app.dirtydish.databinding.ActivityMainMenuBinding
import com.google.firebase.auth.FirebaseAuth

class MainMenu : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkLoggedIn()
        checkHasHouse()
        Session.init()
        setupNav()

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupNav() {
        val binding: ActivityMainMenuBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_main_menu)
        drawerLayout = binding.drawerLayout
        navController = Navigation.findNavController(this, R.id.content_frame)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        // Set up ActionBar
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.navView.setupWithNavController(navController)
    }

    private fun checkLoggedIn() {
        if (FirebaseAuth.getInstance().currentUser == null)
            startActivity(Intent(this, AuthActivity::class.java))
    }

    private fun checkHasHouse() {
        //        if (!Session.hasHouse())
//            startActivity(Intent(this, SelectHouseActivity::class.java))
    }


}

