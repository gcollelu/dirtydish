package com.dirtydish.app.dirtydish

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_chore_home.*

class ChoresFragment : Fragment() {

    private lateinit var db: FirebaseDatabase
    private lateinit var choreRef: DatabaseReference
    private lateinit var listener: ValueEventListener
    var recyclerView: RecyclerView? = null
    //private val context = getContext()!!
    //private val tag = "VIEW_CHORES"

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //Session.userHouse!!.chores
        //choreRef.keepSynced(true)
        val view = inflater.inflate(R.layout.activity_chore_home,
                container, false)

        val chores = Session.userHouse!!.chores
        recyclerView = view!!.findViewById(R.id.choresList) as RecyclerView
        recyclerView!!.adapter = RecyclerAdapter(chores, getContext()!!)

        recyclerView!!.layoutManager = LinearLayoutManager(activity)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAddNewChore.setOnClickListener { view.findNavController().navigate(R.id.action_choresFragment_to_addChoreFragment) }
        //btnViewChoreSchedule.setOnClickListener { startActivity(Intent(activity, ViewChoresActivity::class.java)) }
    }


}
