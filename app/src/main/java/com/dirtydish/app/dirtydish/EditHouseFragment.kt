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
    val housematesArray: MutableList<HouseMate> = mutableListOf<HouseMate>()


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_edit_house,
                container, false)


        //TODO: add actual housemates
        for (i in 0 until 10) {
            val housemate = HouseMate("John Smith " + i.toString(), "lmao@lmao.com", i.toString())
            housematesArray.add(housemate)
        }

        val list = view.findViewById<ListView>(R.id.housematesEditList)
        val adapter = ViewHouseMatesAdapter(activity!!, housematesArray)
        list.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
