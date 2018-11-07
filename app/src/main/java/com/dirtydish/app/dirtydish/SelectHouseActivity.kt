package com.dirtydish.app.dirtydish

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;

import kotlinx.android.synthetic.main.content_select_house.*

class SelectHouseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_house)

        btnNewHouse.setOnClickListener {
            startActivity(Intent(this, SetupHouseActivity::class.java))
        }

        btnJoinHouse.setOnClickListener{
            startActivity(Intent(this, JoinHouse::class.java))
            finish()
        }
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}