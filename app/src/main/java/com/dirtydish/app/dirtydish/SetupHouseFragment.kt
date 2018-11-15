package com.dirtydish.app.dirtydish


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_setup_house.*

class SetupHouseFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainMenuActivity).supportActionBar!!.hide()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_setup_house, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        btnCreate.setOnClickListener {
            val houseName = editHouseName.text.toString()
            val houseAddress = editAddress.text.toString()
            val housematesCount = editHouseMateCount.selectedItem.toString()
            val directions = SetupHouseFragmentDirections.ActionSetupHouseFragmentToInviteHousematesFragment(houseName, houseAddress, housematesCount)
            view.findNavController().navigate(directions)
        }

    }
}
