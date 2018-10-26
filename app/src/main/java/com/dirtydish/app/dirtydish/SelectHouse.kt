package com.dirtydish.app.dirtydish

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_select_house.*
import kotlinx.android.synthetic.main.content_select_house.*

class SelectHouse : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_house)
        setSupportActionBar(toolbar)

        btnNewHouse.setOnClickListener {
            startActivity(Intent(this, SetupHouse::class.java))
        }

        btnJoinHouse.setOnClickListener{
            finish()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
