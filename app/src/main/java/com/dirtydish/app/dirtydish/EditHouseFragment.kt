package com.dirtydish.app.dirtydish


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_edit_house.*
import org.jetbrains.anko.doAsync

class EditHouseFragment : Fragment() {
    var housematesArray: MutableList<HouseMate> = mutableListOf<HouseMate>()
    var choreArray: MutableList<Chore> = mutableListOf<Chore>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_house,
                container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Session.userHouse != null) {
            housematesArray = Session.userHouse!!.houseMates
            choreArray = Session.userHouse!!.chores
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
            val house = saveHouseToDB()
            var bundle = Bundle()
            bundle.putString("houseName", house?.name)
            bundle.putString("houseAddress", house?.address)
            Toast.makeText(activity, "House saved.", Toast.LENGTH_SHORT).show()
            view.findNavController().navigateUp()
        }

        housematesEditList.setOnItemClickListener { parent, view, position, id ->
            val housemate = housematesArray.get(position)
            val directions = EditHouseFragmentDirections.ActionEditHouseFragmentToEditHousemateFragment(housemate)
            view.findNavController().navigate(directions)
        }
    }

    private fun saveHouseToDB(): House? {
        val house = Session.userHouse
        var changed = false
        if (house != null) {
            val houseRef = FirebaseDatabase.getInstance().getReference("houses").child(house.id)
            val changedName = houseName.text.toString()
            val changedAddress = houseAddress.text.toString()
            if (changedName != house.name && changedName.isNotEmpty()) {
                house.name = changedName
                changed = true
                doAsync { houseRef.child("name").setValue(changedName) }
            }
            if (changedAddress != house.address && changedAddress.isNotEmpty()) {
                house.address = changedAddress
                changed = true
                doAsync { houseRef.child("address").setValue(changedAddress) }
            }
        }
        return if (changed) house else null
    }
}
