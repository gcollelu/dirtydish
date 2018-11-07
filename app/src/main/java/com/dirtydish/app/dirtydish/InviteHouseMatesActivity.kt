package com.dirtydish.app.dirtydish

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_invite_house_mates.*
import kotlinx.android.synthetic.main.content_invite_house_mates.*
import android.content.Intent


class InviteHouseMatesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_house_mates)
        setSupportActionBar(toolbar)
        val housematesCount = intent.getIntExtra("housemates_count", 0)
        val houseName = intent.getStringExtra("house_name")
        val houseAddress = intent.getStringExtra("house_address")

        val housematesArray: MutableList<HouseMate> = mutableListOf<HouseMate>()

        for (i in 0 until housematesCount) {
            housematesArray.add(HouseMate())
        }

        val adapter = InviteHouseMatesInputAdapter(this, housematesArray)
        inputList.adapter = adapter

        btnCreateHouse.setOnClickListener {
            var validInput = true

            for (i in 0 until housematesCount){
                housematesArray[i] = adapter.getHousmate(i)
                validInput = (Utilities.isEmailValid(housematesArray[i].email) && housematesArray[i].name.isNotEmpty())
            }


            if (validInput){
                //TODO: actually create the house in the database
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                val house = House()
                house.name = houseName
                house.address = houseAddress
                house.houseMates = housematesArray
                Session.userHouse = house

                val intent = Intent(this, ViewHouseActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Please input all housemates information.", Toast.LENGTH_SHORT).show()
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
