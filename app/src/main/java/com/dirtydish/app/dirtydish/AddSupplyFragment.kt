package com.dirtydish.app.dirtydish

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.dirtydish.app.dirtydish.data.Supply
import com.dirtydish.app.dirtydish.singletons.Session
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_supply.*


class AddSupplyFragment : Fragment() {

    var isMissing = true
    var supplyName = ""
    var supplyArray: MutableList<Supply> = mutableListOf<Supply>()
    private lateinit var db: FirebaseDatabase

    private lateinit var supplyRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        db = FirebaseDatabase.getInstance()
        supplyRef = db.getReference("houses").child(Session.userHouse!!.id)


        return inflater.inflate(R.layout.fragment_add_supply, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Session.userHouse != null) {
            supplyArray = Session.userHouse!!.supplies

        }

        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            isMissing = isChecked
        }
        btnAddSupply.setOnClickListener {
            if (editName.text.isNotEmpty()){
                supplyName = editName.text.toString()
                if (createSupply()){
                    Toast.makeText(activity, "Supply Successfully added!", Toast.LENGTH_SHORT).show()
                    view.findNavController().navigateUp()
                }
                else
                    Toast.makeText(activity, "Could not add supply, try again later.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun createSupply() : Boolean{
        if (Session.hasHouse()) {
            var id = 0
            if (!Session.userHouse!!.supplies.isEmpty())
                id = Session.userHouse!!.supplies.lastIndex + 1
            val houseKey = Session.userHouse!!.id

            val supply = Supply(
                    name = supplyName,
                    missing = isMissing,
                    id = id.toString(),
                    houseId = houseKey)

            supplyArray.add(supply)
            supplyRef.child("supplies").setValue(supplyArray)
            return true
        }
        else
            return false
    }

}
