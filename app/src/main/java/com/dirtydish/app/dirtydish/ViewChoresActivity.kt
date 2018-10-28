package com.dirtydish.app.dirtydish

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_view_chores.*

class ViewChoresActivity : AppCompatActivity() {

    private lateinit var db : FirebaseDatabase
    private lateinit var choreRef : DatabaseReference
    private val context = this
    private val tag = "VIEW_CHORES"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_chores)

        db = FirebaseDatabase.getInstance()
        choreRef = db.getReference("chores")

        val viewManager = LinearLayoutManager(this)
        recyclerView.layoutManager = viewManager

        fab.setOnClickListener {
            val intent = Intent(this, AddChoreActivity::class.java)
            startActivity(intent)
        }

        choreRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snap: DataSnapshot) {
                val list = mutableListOf<Chore>()
                snap.children.mapNotNullTo(list){
                    it.getValue<Chore>(Chore::class.java)
                }
                recyclerView.adapter = ChoreAdapter(list, context)
                Log.d(tag, list.toString())
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

    }
}
