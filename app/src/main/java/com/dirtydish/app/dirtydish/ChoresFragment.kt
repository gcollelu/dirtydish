package com.dirtydish.app.dirtydish

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Button

class ChoresFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_chore_home,
                container, false)

        val btnAdd = view.findViewById<Button>(R.id.btnAddNewChore)
        val btnView = view.findViewById<Button>(R.id.btnViewChoreSchedule)

        btnAdd.setOnClickListener { startActivity(Intent(activity, AddChoreActivity::class.java)) }
        btnView.setOnClickListener { startActivity(Intent(activity, ViewChoresActivity::class.java)) }

        return view
    }

}
