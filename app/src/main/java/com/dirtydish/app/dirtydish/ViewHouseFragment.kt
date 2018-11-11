package com.dirtydish.app.dirtydish

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.navigation.findNavController

class ViewHouseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_view_house,
                container, false)

        val housematesArray: MutableList<HouseMate> = mutableListOf<HouseMate>()

        //TODO: add actual housemates
        for (i in 0 until 10) {
            val housemate = HouseMate("John Smith " + i.toString(), "lmao@lmao.com", i.toString())
            housematesArray.add(housemate)
        }

        val list = view.findViewById<ListView>(R.id.housematesList)
        val adapter = ViewHouseMatesAdapter(activity!!, housematesArray)
        list.adapter = adapter

        val btnEditHouse = view.findViewById<Button>(R.id.btnEditHouse)

        btnEditHouse.setOnClickListener {
            view.findNavController().navigate(R.id.action_viewHouseFragment_to_editHouseFragment2)
        }
        return view
    }

}
