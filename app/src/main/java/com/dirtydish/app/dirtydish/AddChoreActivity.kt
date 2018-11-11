package com.dirtydish.app.dirtydish

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_chore.*

class AddChoreActivity : AppCompatActivity() {

    private lateinit var db: FirebaseDatabase
    private lateinit var choreRef: DatabaseReference
    private val tag = "CHORE_ADD"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_chore)

        db = FirebaseDatabase.getInstance()
        choreRef = db.getReference("chores")

        btnDone.setOnClickListener {
            createChore()
            finish()
        }
        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun createChore() {
        val key = choreRef.push().key
        Log.d(tag, key)
        if (key != null) {
            val chore = Chore(name = editName.text.toString(), id = key,
                    frequency = Integer.parseInt(editFrequency.text.toString()))
            choreRef.child(key).setValue(chore)
        }
    }
}
