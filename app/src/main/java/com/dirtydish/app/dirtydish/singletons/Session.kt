package com.dirtydish.app.dirtydish.singletons

import com.dirtydish.app.dirtydish.data.AccessToken
import com.dirtydish.app.dirtydish.data.House
import com.dirtydish.app.dirtydish.data.HouseMate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import org.jetbrains.anko.doAsync

/*
*
* This singleton is used to set/get variable relative to the current session
*
 */

object Session {

    var userHouse: House? = null
    var housemate: HouseMate? = null
    private var currFunc: (() -> Unit)? = null

    private var userRef: DatabaseReference? = null
    private var houseRef: DatabaseReference? = null
    private val db = FirebaseDatabase.getInstance()
    var accessToken : AccessToken = AccessToken()

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
            FirebaseMessaging.getInstance().subscribeToTopic(userHouse?.id)
            currFunc.let {
                it?.invoke()
                currFunc = null
            }
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
            FirebaseMessaging.getInstance().unsubscribeFromTopic(userHouse?.id)
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
        clear()
    }

    fun callOnInitiated(funct: () -> Unit) {
        housemate.let { user ->
            if (user == null || (user.houseId != null && userHouse == null)) {
                currFunc = funct
                init()
            } else {
                funct.invoke()
            }
        }
    }


}