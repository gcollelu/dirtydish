package com.dirtydish.app.dirtydish

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_modify_chore.*

class ModifyChore : AppCompatActivity() {

    private lateinit var db: FirebaseDatabase
    private lateinit var choreRef: DatabaseReference
    private val tag = "CHORE_MODIFY"

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(tag, "SWITCHING SUCCESSFUL")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_chore)

        db = FirebaseDatabase.getInstance()
        choreRef = db.getReference("chores")

        var tv1:TextView = findViewById(R.id.modify_chore_text)
        tv1.text = intent.getStringExtra("name")


        btn_delete.setOnClickListener {
            deleteChore(intent.getStringExtra("id"))
            finish()
        }
        btn_submit.setOnClickListener {
            editChore(intent.getStringExtra("id"))
            finish()
        }
    }

    private fun editChore(key:String) {
        Log.d(tag, key)
        val chore = Chore(name = modify_chore_text.text.toString(), id = key)
        choreRef.child(key).setValue(chore)
    }

    private fun deleteChore(key:String) {
        Log.d(tag, key)
        choreRef.child(key).removeValue()
    }

}
