package com.dirtydish.app.dirtydish


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_edit_house.*

class EditHouseFragment : Fragment() {
    var housematesArray: MutableList<HouseMate> = mutableListOf<HouseMate>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_edit_house,
                container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Session.userHouse != null) {
            housematesArray = Session.userHouse!!.houseMates
            houseName.setText(Session.userHouse!!.name)
            houseAddress.setText(Session.userHouse!!.address)
        } else { // use sample data for now
            //TODO redirect user to house join/create page
            for (i in 0 until 10) {
                val housemate = HouseMate("John Smith " + i.toString(), "lmao@lmao.com", i.toString())
                housematesArray.add(housemate)
            }
        }

        val adapter = ViewHouseMatesAdapter(activity!!, housematesArray)
        housematesEditList.adapter = adapter

        btnSave.setOnClickListener {
            view.findNavController().navigateUp()
        }

        housematesEditList.setOnItemClickListener { parent, view, position, id ->
            val housemate = housematesArray.get(position)
            val directions = EditHouseFragmentDirections.ActionEditHouseFragmentToEditHousemateFragment(housemate)
            view.findNavController().navigate(directions)
        }
    }
}
