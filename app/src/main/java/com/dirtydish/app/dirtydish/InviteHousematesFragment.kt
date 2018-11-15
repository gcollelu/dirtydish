package com.dirtydish.app.dirtydish


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_invite_housemates.*
import org.jetbrains.anko.doAsync


class InviteHousematesFragment : Fragment() {
    private lateinit var hmRef: DatabaseReference
    private lateinit var houseListRef: DatabaseReference
    var housematesCount = 0
    var houseName = ""
    var houseAddress = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainMenuActivity).supportActionBar!!.hide()

        val db = FirebaseDatabase.getInstance()
        hmRef = db.getReference("housemates")
        houseListRef = db.getReference("houses")

        housematesCount = InviteHousematesFragmentArgs.fromBundle(arguments).housematesCount.toInt()
        houseName = InviteHousematesFragmentArgs.fromBundle(arguments).houseName
        houseAddress = InviteHousematesFragmentArgs.fromBundle(arguments).houseAddress


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_invite_housemates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val housematesArray: MutableList<HouseMate> = mutableListOf<HouseMate>()
        var choreArray: MutableList<Chore> = mutableListOf<Chore>()

        for (i in 0 until housematesCount) {
            housematesArray.add(HouseMate())
        }

        val adapter = InviteHouseMatesInputAdapter(activity!!, housematesArray)
        inputList.adapter = adapter

        btnCreateHouse.setOnClickListener {
            var validInput = true

            for (i in 0 until housematesCount) {
                housematesArray[i] = adapter.getHousemate(i)
                validInput = validInput && (Utilities.isEmailValid(housematesArray[i].email) && housematesArray[i].name.isNotEmpty())
            }


            if (validInput) {
                Toast.makeText(activity, "House created", Toast.LENGTH_SHORT).show()
                val house = House()
                house.name = houseName
                house.address = houseAddress
                house.houseMates = housematesArray
                house.chores = choreArray
                storeHouseToDB(house)
                //TODO: should we add line below to immediately show user has a house?
                //Session.userHouse = house
                view?.findNavController()?.navigate(R.id.action_inviteHousematesFragment_to_homeFragment)
            } else {
                Toast.makeText(activity, "Please input all housemates information.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun storeHouseToDB(house: House) {
        doAsync {
            val key = houseListRef.push().key
            if (key != null) {
                house.id = key
                val hmArr = house.houseMates.toMutableList()
                house.houseMates = mutableListOf()
                houseListRef.child(key).setValue(house)
                assignIdsToHousemates(hmArr, house, houseListRef.child(key))
            }
        }
    }

    private fun assignIdsToHousemates(houseMates: MutableList<HouseMate>, house: House, houseRef: DatabaseReference) {
        hmRef.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(err: DatabaseError) {
                        // TODO potentially deal with connection error
                    }

                    override fun onDataChange(snap: DataSnapshot) {

                        val list = mutableListOf<HouseMate>()
                        snap.children.mapNotNullTo(list) {
                            it.getValue<HouseMate>(HouseMate::class.java)
                        }

                        val currentUser = Session.housemate
                        if (currentUser != null) {
                            houseMates.add(currentUser)
                        }

                        for (housemate in houseMates) {
                            val id: String? = registeredId(housemate, list)
                            if (id != null) { // User is already registered
                                val existingUser = snap.child(id).getValue<HouseMate>(HouseMate::class.java)
                                if (existingUser != null) {
                                    if (existingUser.name.isNotEmpty()) {
                                        housemate.name = existingUser.name
                                    }
                                    housemate.id = existingUser.id
                                }
                            } else { // User is not yet registered
                                //TODO sendRegistrationEmail()
                                val key = houseRef.child("houseMates").push().key
                                if (key != null) {
                                    housemate.id = key
                                }
                            }
                            housemate.houseId = house.id
                            hmRef.child(housemate.id).setValue(housemate)
                        }

                        houseRef.child("houseMates").setValue(houseMates)
                    }

                }
        )
    }


    private fun registeredId(hm: HouseMate, list: MutableList<HouseMate>): String? {
        for (user in list) {
            if (hm.email == user.email) {
                return user.id
            }
        }
        return null
    }


}
