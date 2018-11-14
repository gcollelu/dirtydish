package com.dirtydish.app.dirtydish

import android.Manifest
import android.app.DatePickerDialog
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
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_chore.*
import java.text.SimpleDateFormat
import java.util.*

class AddChoreFragment : Fragment() {

    private lateinit var db: FirebaseDatabase
    private lateinit var choreRef: DatabaseReference
    private lateinit var houseListRef: DatabaseReference
    private val tag_local = "CHORE_ADD"
    var participantsList: MutableList<HouseMate> = mutableListOf<HouseMate>()
    var housematesArray: MutableList<HouseMate> = mutableListOf<HouseMate>()
    var choreArray: MutableList<Chore> = mutableListOf<Chore>()
    private val READ_REQUEST_CODE = 101
    private var myContext: Context? = null
    val PICK_IMAGE = 1

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_add_chore,
                container, false)

        db = FirebaseDatabase.getInstance()
        houseListRef = db.getReference("houses")

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
            if (!participantsList.contains(housemate)) {
                itemView.background = ContextCompat.getDrawable(this.requireContext(), R.drawable.view_rectangle_light)
                participantsList.add(housemate)
            } else {
                itemView.background = ContextCompat.getDrawable(this.requireContext(), R.drawable.view_rectangle_white)
                participantsList.remove(housemate)
            }
        }

        btnDone.setOnClickListener {
            createChore()
            view.findNavController().navigateUp()
            Toast.makeText(activity, "Chore created.", Toast.LENGTH_SHORT).show()
        }

        btnPickImage.setOnClickListener {
            setupPermissions()
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
        }

        val startDate: TextView = view.findViewById(R.id.startDate)
        val endDate: TextView = view.findViewById(R.id.endDate)


        makeCalendarField(startDate)
        makeCalendarField(endDate)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.myContext = context
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(activity, "Imaged selected.", Toast.LENGTH_SHORT).show()
    }

    private fun createChore() {
        //val key = choreRef.push().key
        //Log.d(tag_local, key)
        //val key = choreRef.push().key
        if (Session.hasHouse()) {
            var frequency = Integer.parseInt(editFrequency.selectedItem.toString())
            var frequencyType = freq_type.selectedItemPosition
            if (frequencyType == 1) {
                frequency *= 7
            } else if (frequencyType == 2) {
                frequency *= 30
            }

            var id = Session.userHouse!!.chores.lastIndex + 1

            var houseKey = Session.userHouse!!.id;
            val chore = Chore(name = editName.text.toString(), id = id.toString(),
                    frequency = frequency, participants = participantsList, houseId = houseKey)


            Log.d(tag_local, chore.toString())
            choreArray.add(chore)

            choreRef.child("chores").setValue(choreArray)
        }

        //TODO: this fnction should return success/failure
    }


    private fun makeCalendarField(textView: TextView) {
        textView.text = SimpleDateFormat("EEEE, MMMM d, yyyy").format(System.currentTimeMillis())

        val calendar = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "EEEE, MMMM d, yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            textView.text = sdf.format(calendar.time)

        }

        textView.setOnClickListener {
            DatePickerDialog(context, dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
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
