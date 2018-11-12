package com.dirtydish.app.dirtydish

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_view_house.*

class ViewHouseFragment : Fragment() {
    var housematesArray: MutableList<HouseMate> = mutableListOf<HouseMate>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_view_house,
                container, false)


        //TODO: add actual housemates
        for (i in 0 until 10) {
            val housemate = HouseMate("John Smith " + i.toString(), "lmao@lmao.com", i.toString())
            housematesArray.add(housemate)
        }
        if (Session.userHouse != null) {
            housematesArray = Session.userHouse!!.houseMates
            houseName.text = Session.userHouse!!.name
            houseAddress.text = Session.userHouse!!.address
        }


        val list = view.findViewById<View>(R.id.housematesList) as ListView
        val adapter = ViewHouseMatesAdapter(activity!!, housematesArray)
        list.adapter = adapter

        val btnEditHouse = view.findViewById<Button>(R.id.btnEditHouse)

        btnEditHouse.setOnClickListener {
            view.findNavController().navigate(R.id.action_viewHouseFragment_to_editHouseFragment2)
        }
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_view_house, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.shareHouse -> {
                Toast.makeText(activity, "Share House clicked.", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
