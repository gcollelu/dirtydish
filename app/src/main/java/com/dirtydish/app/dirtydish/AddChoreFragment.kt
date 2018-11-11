package com.dirtydish.app.dirtydish

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_chore.*

class AddChoreFragment : Fragment() {

    private lateinit var db: FirebaseDatabase
    private lateinit var choreRef: DatabaseReference
    private val tag_local = "CHORE_ADD"

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_add_chore,
                container, false)

        db = FirebaseDatabase.getInstance()
        choreRef = db.getReference("chores")

        val btnDone = view.findViewById<Button>(R.id.btnDone)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        btnDone.setOnClickListener {
            createChore()
        }
        btnCancel.setOnClickListener {
        }

        return view
    }

    private fun createChore() {
        val key = choreRef.push().key
        Log.d(tag_local, key)
        if (key != null) {
            val chore = Chore(name = editName.text.toString(), id = key,
                    frequency = Integer.parseInt(editFrequency.text.toString()))
            choreRef.child(key).setValue(chore)
        }
    }
}
