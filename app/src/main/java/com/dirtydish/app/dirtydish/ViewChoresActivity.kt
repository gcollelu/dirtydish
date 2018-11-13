package com.dirtydish.app.dirtydish

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_view_chores.*

class ViewChoresActivity : AppCompatActivity() {


    private lateinit var db: FirebaseDatabase
    //private lateinit var choreRef: DatabaseReference
    private lateinit var houseRef: DatabaseReference
    private lateinit var listener: ValueEventListener
    private val context = this
    private val tag = "VIEW_CHORES"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_chores)

        db = FirebaseDatabase.getInstance()
        houseRef = db.getReference("houses").child(Session.userHouse!!.id)
        //choreRef = db.getReference("chores")
        //choreRef.keepSynced(true)
        houseRef.keepSynced(true)

        val viewManager = LinearLayoutManager(this)
        recyclerView.layoutManager = viewManager

        attachListenerForChanges()
    }

    override fun onDestroy() {
        super.onDestroy()
        //choreRef.removeEventListener(listener)
        houseRef.removeEventListener(listener)
    }

    private fun attachListenerForChanges() {
        listener = object : ValueEventListener {

            override fun onDataChange(snap: DataSnapshot) {
                //val list = mutableListOf<Chore>()
                val list = Session.userHouse!!.chores
                /*snap.children.mapNotNullTo(list) {
                    it.getValue<Chore>(Chore::class.java)
                }*/
                recyclerView.adapter = RecyclerAdapter(list, context)
            }

            override fun onCancelled(err: DatabaseError) {
                // Failed to connect to database
                Log.d(tag, err.message)
            }

        }

        //choreRef.addValueEventListener(listener)
        houseRef.addValueEventListener(listener)
    }

}
