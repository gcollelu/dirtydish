package com.dirtydish.app.dirtydish

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.doAsync

/*
*
* This singleton is used to set/get variable relative to the current session
*
 */

object Session {

    var userHouse: House? = null
    var housemate: HouseMate? = null

    private var userRef: DatabaseReference? = null
    private var houseRef: DatabaseReference? = null
    private val db = FirebaseDatabase.getInstance()

    private object housemateListener : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {}
        override fun onDataChange(p0: DataSnapshot) {
            housemate = p0.getValue<HouseMate>(HouseMate::class.java)
            val hm = housemate
            if (userHouse == null && hm != null && !hm.houseId.isEmpty()) {
                getHouseInfo(hm.houseId)
            }
        }
    }

    private object houseListener : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {}
        override fun onDataChange(p0: DataSnapshot) {
            userHouse = p0.getValue<House>(House::class.java)
        }
    }

    init {
        initInfo()
    }

    private fun initInfo() {
        doAsync {
            val curUser = FirebaseAuth.getInstance().currentUser
            if (curUser != null) {
                userRef = db.getReference("housemates").child(curUser.uid)
                userRef?.addValueEventListener(housemateListener)
            }
        }
    }

    private fun getHouseInfo(houseId: String) {
        houseRef = db.getReference("houses").child(houseId)
        houseRef?.addValueEventListener(houseListener)
    }

    // call this on logout
    fun clear() {
        doAsync {
            housemate = null
            userHouse = null
            userRef?.removeEventListener(housemateListener)
            houseRef?.removeEventListener(houseListener)
        }
    }

    // call this on login
    fun init() {
        initInfo()
    }

    fun isLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    fun hasHouse(): Boolean {
        return userHouse != null
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        Session.clear()
    }


}