package com.dirtydish.app.dirtydish

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_setup_house.*
import kotlinx.android.synthetic.main.content_setup_house.*


class SetupHouse : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_house)
        setSupportActionBar(toolbar)
        toolbar.title = "New House"

        btnNewHouse.setOnClickListener { }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
