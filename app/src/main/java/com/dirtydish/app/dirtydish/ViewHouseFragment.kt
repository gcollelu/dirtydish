package com.dirtydish.app.dirtydish

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_view_house.*

class ViewHouseFragment : Fragment() {
    var housematesArray: MutableList<HouseMate> = mutableListOf<HouseMate>()
    var myView: View? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_view_house,
                container, false)
        myView = view

        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Session.userHouse != null) {
            housematesArray = Session.userHouse!!.houseMates
            houseName.text = Session.userHouse!!.name
            houseAddress.text = Session.userHouse!!.address
        } else { // use sample data for now
            //TODO redirect user to house join/create page
            for (i in 0 until 10) {
                val housemate = HouseMate("John Smith " + i.toString(), "lmao@lmao.com", i.toString())
                housematesArray.add(housemate)
            }
        }
        
        val adapter = ViewHouseMatesAdapter(activity!!, housematesArray)
        housematesList.adapter = adapter

        btnEditHouse.setOnClickListener {
            view.findNavController().navigate(R.id.action_viewHouseFragment_to_editHouseFragment2)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_view_house, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.shareHouse -> {
                myView!!.findNavController().navigate(R.id.action_viewHouseFragment_to_shareHouseFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
