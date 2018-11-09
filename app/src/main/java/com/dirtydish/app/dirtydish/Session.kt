package com.dirtydish.app.dirtydish

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/*
*
* This singleton is used to set/get variable relative to the current session
*
 */

object Session {

    var userHouse: House? = null
    var housemate: HouseMate? = null

    init {
        initInfo()
    }

    private fun initInfo() {
        val curUser = FirebaseAuth.getInstance().currentUser
        if (curUser != null){
            val db = FirebaseDatabase.getInstance()
            val hmref = db.getReference("housemates")
            val userRef = hmref.child(curUser.uid)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    housemate = p0.getValue<HouseMate>(HouseMate::class.java)
                    val hm = housemate
                    if (hm != null && !hm.houseId.isEmpty()) {
                        getHouseInfo(hm.houseId)
                    }
                }
            })
        }
    }

    private fun getHouseInfo(houseId: String){
        val db = FirebaseDatabase.getInstance()
        val housesRef = db.getReference("houses")
        housesRef.child(houseId).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                userHouse = p0.getValue<House>(House::class.java)
            }
        })
    }

    // call this on logout
    fun clear(){
        housemate = null
        userHouse = null
    }

    // call this on login
    fun init(){
        initInfo()
    }

    fun isLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    fun hasHouse(): Boolean {
      return userHouse != null
    }


}