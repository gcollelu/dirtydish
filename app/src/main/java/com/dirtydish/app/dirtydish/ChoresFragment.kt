package com.dirtydish.app.dirtydish

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import kotlinx.android.synthetic.main.activity_chore_home.*

class ChoresFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_chore_home,
                container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAddNewChore.setOnClickListener { startActivity(Intent(activity, AddChoreActivity::class.java)) }
        btnViewChoreSchedule.setOnClickListener { startActivity(Intent(activity, ViewChoresActivity::class.java)) }
    }

}
