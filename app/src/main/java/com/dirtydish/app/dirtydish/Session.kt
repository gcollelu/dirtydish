package com.dirtydish.app.dirtydish

import com.google.firebase.auth.FirebaseAuth

/*
*
* This singleton is used to set/get variable relative to the current session
*
 */

object Session {

    var userHouse: House? = null

    fun isLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    fun hasHouse(): Boolean {
        //TODO: check whether user has a house assigned from FireBase

        return (this.userHouse != null)
    }


}