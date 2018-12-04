package com.dirtydish.app.dirtydish

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.dirtydish.app.dirtydish.data.Chore
import com.dirtydish.app.dirtydish.data.House
import com.dirtydish.app.dirtydish.data.HouseMate
import com.dirtydish.app.dirtydish.data.Supply
import com.dirtydish.app.dirtydish.singletons.Session
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.doAsync

class HomeFragment : Fragment() {

    var myView: View? = null
    private lateinit var listener: ValueEventListener
    private lateinit var houseRef: DatabaseReference
    private lateinit var db: FirebaseDatabase
    private var navController: NavController? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home,
                container, false)
        db = FirebaseDatabase.getInstance()
        myView = view
        (activity as MainMenuActivity).supportActionBar!!.show()
        setHasOptionsMenu(true)

        Session.callOnInitiated {
            if (Session.userHouse != null) {
                Log.d("SESSION", "you now have a house")
            } else {
                Log.d("SESSION", "didn't work as expected")
            }
        }

        if (Session.hasHouse()) {
            houseRef = db.getReference("houses").child(Session.userHouse!!.id)
            houseRef.keepSynced(true)
        }

//        if (!Session.hasHouse())
//            view?.findNavController()?.navigate(R.id.selectHouseFragment)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myChoresList.layoutManager = LinearLayoutManager(activity as Context)
        navController = myView?.findNavController()

        checkHasHouse()
        missingSupplies.layoutManager = LinearLayoutManager(activity as Context)
    }


    override fun onResume() {
        super.onResume()

        if (Session.hasHouse()) {
            populateAdapters(Session.userHouse!!, Session.housemate!!)
        } else {
            instantiateAdapters()
        }
    }

    private fun instantiateAdapters() {
        doAsync {
            val curUser = FirebaseAuth.getInstance().currentUser
            if (curUser != null) {
                val userRef = FirebaseDatabase.getInstance().getReference("housemates").child(curUser.uid)
                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(p0: DataSnapshot) {
                        val user = p0.getValue<HouseMate>(HouseMate::class.java)
                        Log.d("test", "$user")
                        if (user != null && user.houseId.isNotEmpty()) {
                            val housesRef = FirebaseDatabase.getInstance().getReference("houses")
                            housesRef.child(user.houseId).addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {}
                                override fun onDataChange(p0: DataSnapshot) {
                                    val house = p0.getValue<House>(House::class.java)
                                    Log.d("test", "$house")
                                    if (house != null) {
                                        populateAdapters(house, user)
                                    }
                                }

                            })
                        }
                    }

                })
            }
        }
    }

    private fun populateAdapters(house: House, user: HouseMate) {

        val myChores = getPersonalChores(house.chores, user)
        if (myChores != null) {
            myChoresList.adapter = MainChoreAdapter(myChores, activity as Context)
        }

        val myMissingSupplies = getMissingSupplies(house.supplies)
        if (myMissingSupplies != null) {
            missingSupplies.adapter = MainSupplyAdapter(myMissingSupplies, activity as Context)
        }
    }

    private fun getMissingSupplies(supplies: MutableList<Supply>): MutableList<Supply>? {
        return if (supplies.isNotEmpty()) {
            var mySupplies = supplies.filter { supply -> supply.missing } as MutableList<Supply>
            if (mySupplies.isEmpty()) {
                null
            } else {
                mySupplies
            }
        } else {
            null
        }
    }

    private fun getPersonalChores(chores: MutableList<Chore>, user: HouseMate): MutableList<Chore>? {
        return if (chores.isNotEmpty()) {
            var myChores = chores.filter { chore -> containsUser(chore, user) } as MutableList<Chore>
            if (myChores.isEmpty()) {
                null
            } else {
                myChores
            }
        } else {
            null
        }
    }

    private fun containsUser(chore: Chore, user: HouseMate): Boolean = chore.assignee == user.id

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_homepage, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.notification_button -> {
                myView!!.findNavController().navigate(R.id.action_homeFragment_to_notificationsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkHasHouse() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val houseIdRef = db.getReference("housemates").child(currentUserId).child("houseId")
        houseIdRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Snapshot: ", snapshot.toString())
                if (snapshot.value.toString() == "") {
                    Log.d("WTF: ", "$snapshot.value")
                    navController?.navigate(R.id.selectHouseFragment)
                }
            }
        })

    }
}

