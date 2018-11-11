package com.dirtydish.app.dirtydish

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
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

        btnAddNewChore.setOnClickListener { view.findNavController().navigate(R.id.action_choresFragment_to_addChoreFragment) }
        btnViewChoreSchedule.setOnClickListener { startActivity(Intent(activity, ViewChoresActivity::class.java)) }
    }

}
