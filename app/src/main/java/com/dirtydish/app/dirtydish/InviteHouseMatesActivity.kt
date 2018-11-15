package com.dirtydish.app.dirtydish

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.content_invite_house_mates.*
import org.jetbrains.anko.doAsync


class InviteHouseMatesActivity : AppCompatActivity() {

    private lateinit var hmRef: DatabaseReference
    private lateinit var houseListRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_house_mates)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        val db = FirebaseDatabase.getInstance()
        hmRef = db.getReference("housemates")
        houseListRef = db.getReference("houses")

        val housematesCount = intent.getIntExtra("housemates_count", 0)
        val houseName = intent.getStringExtra("house_name")
        val houseAddress = intent.getStringExtra("house_address")

        val housematesArray: MutableList<HouseMate> = mutableListOf<HouseMate>()
        var choreArray: MutableList<Chore> = mutableListOf<Chore>()

        for (i in 0 until housematesCount) {
            housematesArray.add(HouseMate())
        }

        val adapter = InviteHouseMatesInputAdapter(this, housematesArray)
        inputList.adapter = adapter

        btnCreateHouse.setOnClickListener {
            var validInput = true

            for (i in 0 until housematesCount) {
                housematesArray[i] = adapter.getHousemate(i)
                validInput = validInput && (Utilities.isEmailValid(housematesArray[i].email) && housematesArray[i].name.isNotEmpty())
            }


            if (validInput) {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                val house = House()
                house.name = houseName
                house.address = houseAddress
                house.houseMates = housematesArray
                house.chores = choreArray
                storeHouseToDB(house)

                val intent = Intent(this, MainMenuActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please input all housemates information.", Toast.LENGTH_SHORT).show()
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
