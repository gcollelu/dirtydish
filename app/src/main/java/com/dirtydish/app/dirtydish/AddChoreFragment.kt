package com.dirtydish.app.dirtydish

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
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
    private val READ_REQUEST_CODE = 101
    private var myContext: Context? = null
    val PICK_IMAGE = 1

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_add_chore,
                container, false)

        db = FirebaseDatabase.getInstance()
        choreRef = db.getReference("chores")

        val btnDone = view.findViewById<Button>(R.id.btnDone)
        val btnPickImage = view.findViewById<Button>(R.id.btnPickImage)

        setupPermissions()
        btnDone.setOnClickListener {
            createChore()
        }

        btnPickImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.myContext = context
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun createChore() {
        val key = choreRef.push().key
        Log.d(tag_local, key)
        if (key != null) {
            val chore = Chore(name = editName.text.toString(), id = key,
                    frequency = Integer.parseInt(editFrequency.selectedItem.toString()))
            choreRef.child(key).setValue(chore)
        }
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(myContext!!,
                Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(activity!!,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_REQUEST_CODE)
    }

}
