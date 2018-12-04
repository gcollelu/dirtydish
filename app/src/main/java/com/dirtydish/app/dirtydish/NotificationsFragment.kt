package com.dirtydish.app.dirtydish

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dirtydish.app.dirtydish.singletons.Session
import com.google.firebase.database.*

class NotificationsFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    private lateinit var houseRef: DatabaseReference
    private lateinit var listener: ValueEventListener
    private lateinit var db: FirebaseDatabase
    private var thisContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        db = FirebaseDatabase.getInstance()

        thisContext = context
        if (Session.hasHouse()) {
            houseRef = db.getReference("houses").child(Session.userHouse!!.id)
            houseRef.keepSynced(true)
        }

        recyclerView = view!!.findViewById(R.id.notificationsList) as RecyclerView

        recyclerView!!.layoutManager = LinearLayoutManager(activity)

        return view
    }

    private fun attachListenerForChanges() {
        listener = object : ValueEventListener {

            override fun onDataChange(snap: DataSnapshot) {
                val list = mutableListOf<String>("Fowler", "Beck", "Evans")
                recyclerView!!.adapter = NotificationsRecyclerAdapter(list, thisContext!!)
                val swipeHandler = object : SwipeToDeleteCallback(thisContext!!) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val adapter = recyclerView!!.adapter as NotificationsRecyclerAdapter

                    }
                }
                val itemTouchHelper = ItemTouchHelper(swipeHandler)
                itemTouchHelper.attachToRecyclerView(recyclerView)
            }

            override fun onCancelled(err: DatabaseError) {
                // Failed to connect to database
                Log.d(tag, err.message)
            }
        }
        //choreRef.addValueEventListener(listener)
        if (Session.hasHouse()) {
            houseRef.addValueEventListener(listener)
        }

    }

}
