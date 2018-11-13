package com.dirtydish.app.dirtydish

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_chore.*

class AddChoreFragment : Fragment() {

    private lateinit var db: FirebaseDatabase
    private lateinit var choreRef: DatabaseReference
    private val tag_local = "CHORE_ADD"
    var participantsList: MutableList<HouseMate> = mutableListOf<HouseMate>()
    var housematesArray: MutableList<HouseMate> = mutableListOf<HouseMate>()
    var choreArray: MutableList<Chore> = mutableListOf<Chore>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_add_chore,
                container, false)

        db = FirebaseDatabase.getInstance()

        choreRef = db.getReference("houses").child(Session.userHouse!!.id)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (i in 0 until 5) {
            val housemate = HouseMate("John Smith " + i.toString(), "lmao@lmao.com", i.toString())
            housematesArray.add(housemate)
        }
        if (Session.userHouse != null) {
            housematesArray = Session.userHouse!!.houseMates
        }

        if (Session.userHouse != null) {
            choreArray = Session.userHouse!!.chores

        }


        val adapter = ViewHouseMatesAdapter(activity!!, housematesArray)
        participants.adapter = adapter

        participants.setOnItemClickListener { parent, itemView, position, id ->
            val housemate = housematesArray.get(position)
            if (!participantsList.contains(housemate)){
                itemView.background = ContextCompat.getDrawable(this.requireContext(), R.drawable.view_rectangle_light)
                participantsList.add(housemate)
            } else {
                itemView.background = ContextCompat.getDrawable(this.requireContext(), R.drawable.view_rectangle_white)
                participantsList.remove(housemate)
            }
        }

        btnDone.setOnClickListener {
            createChore()
        }

    }

    private fun createChore() {
        //val key = choreRef.push().key
        //Log.d(tag_local, key)
        if (Session.hasHouse()) {
            var frequency = Integer.parseInt(editFrequency.selectedItem.toString())
            var frequencyType = freq_type.selectedItemPosition
            if (frequencyType == 1){
                frequency *= 7
            } else if (frequencyType == 2){
                frequency *= 30
            }
            var houseKey = Session.userHouse!!.id;
            val chore = Chore(name = editName.text.toString(), id = houseKey,
                    frequency = frequency, participants = participantsList, houseId = houseKey)
            //choreRef.child(key).setValue(chore)

            Log.d("ADD_CHORE_NEW", chore.toString())
            choreArray.add(chore)
            //choreRef.child("choresList").setValue("xzcv")
            Log.d("ADD_CHORE_NEW", Session.userHouse!!.chores.toString())
        }

    }
}
