package com.dirtydish.app.dirtydish.chores

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.dirtydish.app.dirtydish.R
import com.dirtydish.app.dirtydish.singletons.Session
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_chore_home.*

class ChoresFragment : Fragment() {
    private lateinit var houseRef: DatabaseReference
    private lateinit var listener: ValueEventListener
    private lateinit var db: FirebaseDatabase
    private lateinit var choreRef: DatabaseReference
    var recyclerView: RecyclerView? = null
    private var thisContext: Context? = null
    //private val tag = "VIEW_CHORES"

    private lateinit var imageName:String
    internal var storage: FirebaseStorage?=null
    internal var storageReference: StorageReference?=null


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        db = FirebaseDatabase.getInstance()


        thisContext = context
        if (Session.hasHouse()) {
            houseRef = db.getReference("houses").child(Session.userHouse!!.id)
            houseRef.keepSynced(true)
        }
        val view = inflater.inflate(R.layout.fragment_chore_home,
                container, false)

        recyclerView = view!!.findViewById(R.id.choresList) as RecyclerView

        recyclerView!!.layoutManager = LinearLayoutManager(activity)


        attachListenerForChanges()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAddNewChore.setOnClickListener { view.findNavController().navigate(R.id.action_choresFragment_to_addChoreFragment) }
        //btnViewChoreSchedule.setOnClickListener { startActivity(Intent(activity, ViewChoresActivity::class.java)) }
    }


    private fun attachListenerForChanges() {
        listener = object : ValueEventListener {

            override fun onDataChange(snap: DataSnapshot) {
                val list = Session.userHouse!!.chores
                recyclerView!!.adapter = RecyclerAdapter(list, thisContext!!)
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
