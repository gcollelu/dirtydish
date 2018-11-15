package com.dirtydish.app.dirtydish

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_input_name.*

class InputNameActivity : AppCompatActivity() {
    private lateinit var housemateRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_name)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        btnSignUp.setOnClickListener {
            if (editName.text.isNotEmpty()) {
                //TODO: update name of housemate from backend
                startActivity(Intent(this, SelectHouseActivity::class.java))
            }
        }
    }
}
