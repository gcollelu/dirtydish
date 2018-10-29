package com.dirtydish.app.dirtydish

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v7.app.ActionBarDrawerToggle
import kotlinx.android.synthetic.main.activity_chore_menu.*
import kotlinx.android.synthetic.main.activity_main_menu.*
import kotlinx.android.synthetic.main.app_bar_main_menu.*

class ChoreMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chore_menu)
        setupUIOnCreate()
    }

    // Changes the layout UI when the activity is created
    private fun setupUIOnCreate() {
        btnAddNewChore.setOnClickListener {
            val intent = Intent(this, AddChoreActivity::class.java)
            startActivity(intent)
        }

        btnViewChoreSchedule.setOnClickListener {
            val intent = Intent(this, ViewChoresActivity::class.java)
            startActivity(intent)
        }
    }
}
