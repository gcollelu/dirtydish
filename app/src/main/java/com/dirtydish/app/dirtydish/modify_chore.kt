package com.dirtydish.app.dirtydish

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_modify_chore.*

class modify_chore : AppCompatActivity() {

    private lateinit var db: FirebaseDatabase
    private lateinit var choreRef: DatabaseReference
    private val tag = "CHORE_MODIFY"
    private lateinit var choreQuery: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_chore)

        db = FirebaseDatabase.getInstance()
        choreRef = db.getReference("chores")
        choreQuery = choreRef.child("chores").orderByChild("name")


        btn_delete.setOnClickListener {
            deleteChore()
            finish()
        }
        btn_submit.setOnClickListener {
            editChore()
            finish()
        }
    }

    private fun editChore() {
        val key = choreRef.key
        Log.d(tag, key)
        if (key != null) {
            val chore = Chore(name = modify_chore_text.text.toString(), id = key)
            choreRef.child(key).setValue(chore)
        }
    }

    private fun deleteChore() {
        val key = choreRef.key
        Log.d(tag, key)
    }
}
