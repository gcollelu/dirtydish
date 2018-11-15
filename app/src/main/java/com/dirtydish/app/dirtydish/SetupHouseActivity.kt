package com.dirtydish.app.dirtydish

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_setup_house.*


class SetupHouseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_house)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        btnNewHouse.setOnClickListener {
            if (editAddress.text.isNotEmpty()
                    && editHouseName.text.isNotEmpty()) {
                //Go to next page
                val intent = Intent(this, InviteHouseMatesActivity::class.java)
                intent.putExtra("house_name", editHouseName.text.toString())
                intent.putExtra("house_address", editAddress.text.toString())
                intent.putExtra("housemates_count", editHouseMateCount.selectedItem.toString().toInt())
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Please enter each value.", Toast.LENGTH_SHORT).show()
            }

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
