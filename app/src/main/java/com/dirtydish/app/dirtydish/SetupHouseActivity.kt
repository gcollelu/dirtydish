package com.dirtydish.app.dirtydish

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.content_setup_house.*


class SetupHouseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_house)
//        setSupportActionBar(toolbar)
    //    toolbar.title = "New House"

        btnNewHouse.setOnClickListener {
            if (editAddress.text.isNotEmpty()
                    && editHouseName.text.isNotEmpty()
                    && editHouseMateCount.text.isNotEmpty()
                    && (editHouseMateCount.text.toString().toIntOrNull() is Int)){
                //Go to next page
                val intent = Intent(this, InviteHouseMatesActivity::class.java)
                intent.putExtra("house_name", editHouseName.text.toString())
                intent.putExtra("house_address", editAddress.text.toString())
                intent.putExtra("housemates_count", editHouseMateCount.text.toString().toInt())
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Please enter each value.", Toast.LENGTH_SHORT).show()
            }

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
