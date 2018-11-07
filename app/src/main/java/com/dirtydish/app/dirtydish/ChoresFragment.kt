package com.dirtydish.app.dirtydish

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Button
import kotlinx.android.synthetic.main.activity_chore_home.*

class ChoresFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_chore_home,
                container, false)

        //For some reason it won't bind the click listener to the button
        btnAddNewChore?.setOnClickListener {
            val intent = Intent(activity, AddChoreActivity::class.java)
            startActivity(intent)
        }

        btnViewChoreSchedule?.setOnClickListener {
            val intent = Intent(activity, ViewChoresActivity::class.java)
            startActivity(intent)
        }
        return view
    }


}
