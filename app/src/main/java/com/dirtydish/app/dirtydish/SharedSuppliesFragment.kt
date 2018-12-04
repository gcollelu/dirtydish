package com.dirtydish.app.dirtydish

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
import com.dirtydish.app.dirtydish.chores.RecyclerAdapter
import com.dirtydish.app.dirtydish.singletons.Session
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_shared_supplies.*


class SharedSuppliesFragment : Fragment() {

    private lateinit var houseRef: DatabaseReference
    private lateinit var listener: ValueEventListener
    private lateinit var db: FirebaseDatabase
    private lateinit var choreRef: DatabaseReference
    var recyclerView: RecyclerView? = null
    private var thisContext: Context? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        db = FirebaseDatabase.getInstance()

        thisContext = context
        if (Session.hasHouse()) {
            houseRef = db.getReference("houses").child(Session.userHouse!!.id)
            houseRef.keepSynced(true)
        }

        val view = inflater.inflate(R.layout.fragment_shared_supplies, container, false)

        recyclerView = view!!.findViewById(R.id.suppliesList) as RecyclerView

        recyclerView!!.layoutManager = LinearLayoutManager(activity)


        attachListenerForChanges()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAddSupply.setOnClickListener {
            view.findNavController().navigate(R.id.action_sharedSuppliesFragment_to_addSupplyFragment)
        }

    }

    private fun attachListenerForChanges() {
        listener = object : ValueEventListener {

            override fun onDataChange(snap: DataSnapshot) {
                val list = Session.userHouse!!.supplies
                recyclerView!!.adapter = SuppliesRecyclerAdapter(list, thisContext!!)
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
