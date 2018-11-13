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

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_add_chore,
                container, false)

        db = FirebaseDatabase.getInstance()
        choreRef = db.getReference("chores")

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
        val key = choreRef.push().key
        Log.d(tag_local, key)
        if (key != null) {
            var frequency = Integer.parseInt(editFrequency.selectedItem.toString())
            var frequencyType = freq_type.selectedItemPosition
            if (frequencyType == 1){
                frequency *= 7
            } else if (frequencyType == 2){
                frequency *= 30
            }
            val chore = Chore(name = editName.text.toString(), id = key,
                    frequency = frequency, participants = participantsList)
            choreRef.child(key).setValue(chore)
        }
    }
}
