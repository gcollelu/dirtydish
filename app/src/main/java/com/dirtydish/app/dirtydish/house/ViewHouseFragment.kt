package com.dirtydish.app.dirtydish.house

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import androidx.navigation.findNavController
import com.dirtydish.app.dirtydish.R
import com.dirtydish.app.dirtydish.singletons.Session
import com.dirtydish.app.dirtydish.house.ViewHouseFragmentArgs
import com.dirtydish.app.dirtydish.data.HouseMate
import kotlinx.android.synthetic.main.fragment_view_house.*

class ViewHouseFragment : Fragment() {
    var housematesArray: MutableList<HouseMate> = mutableListOf<HouseMate>()
    var myView: View? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_house,
                container, false)
        myView = view

        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Session.userHouse != null) {
            housematesArray = Session.userHouse!!.houseMates
            val houseNameString = Session.userHouse!!.name
            houseName.setText(houseNameString)
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

    override fun onResume() {
        super.onResume()
        val name = ViewHouseFragmentArgs.fromBundle(arguments).houseName
        val address = ViewHouseFragmentArgs.fromBundle(arguments).houseAddress
        if (name != "My House" && name.isNotEmpty()) {
            houseName.text = name
        }
        if (address != "My Address" && address.isNotEmpty()) {
            houseAddress.text = address
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
            R.id.leaveHouse -> {
                //myView!!.findNavController().navigate(R.id.action_joinHouseFragment_to_homeFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
