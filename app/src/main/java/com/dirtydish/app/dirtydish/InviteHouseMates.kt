package com.dirtydish.app.dirtydish

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_invite_house_mates.*
import kotlinx.android.synthetic.main.content_invite_house_mates.*

class InviteHouseMates : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_house_mates)
        setSupportActionBar(toolbar)
        val housematesCount = intent.getIntExtra("housemates_count", 0)
        val house_name = intent.getStringExtra("house_name")
        val house_address = intent.getStringExtra("house_address")

        var housematesArray : MutableList<HouseMate> = mutableListOf<HouseMate>()

        for (i in 0 until housematesCount) {
            housematesArray.add(HouseMate())
        }

        val adapter = InviteHouseMatesInputAdapter(this, housematesArray)
        inputList.adapter = adapter

        btnCreateHouse.setOnClickListener {
            var validInput = true

            for (i in 0 until housematesCount)
                validInput = (Utilities.isEmailValid(housematesArray[i].email) && housematesArray[i].name.isNotEmpty())

            if (validInput){
                //TODO: actually create the house in the database
            }else{
                Toast.makeText(this, "Please input all housemates information.", Toast.LENGTH_SHORT).show()
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
