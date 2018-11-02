package com.dirtydish.app.dirtydish

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_modify_chore.*
import kotlinx.android.synthetic.main.nav_header_main_menu.*

class ModifyChore : AppCompatActivity() {

    private lateinit var db: FirebaseDatabase
    private lateinit var choreRef: DatabaseReference
    private val tag = "CHORE_MODIFY"
    private lateinit var choreQuery: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_chore)

        db = FirebaseDatabase.getInstance()
        choreRef = db.getReference("chores")

        choreQuery = choreRef.child("chores").orderByChild("id").equalTo(intent.getStringExtra("id"))

        var tv1:TextView = findViewById(R.id.modify_chore_text)
        tv1.text = intent.getStringExtra("id")


        btn_delete.setOnClickListener {
            deleteChore(intent.getStringExtra("id"))
            finish()
        }
        btn_submit.setOnClickListener {
            editChore(intent.getStringExtra("id"))
            finish()
        }
    }
//TODO Fix these
    private fun editChore(key:String) {
        Log.d(tag, key)
        if (key != null) {
            val chore = Chore(name = modify_chore_text.text.toString(), id = key)
            choreRef.child(key).setValue(chore)
        }
    }

    private fun deleteChore(key:String) {
        val key = choreRef.key
        Log.d(tag, key)
        /*choreQuery.addListenerForSingleValueEvent( new ValueEventListener(){
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (querySnap:DataSnapshot in dataSnapshot.children){
                    querySnap.ref.removeValue();
                }
            }
        });*/
    }

}
