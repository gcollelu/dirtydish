package com.dirtydish.app.dirtydish.chores

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.dirtydish.app.dirtydish.chores.ChoreDetailFragmentArgs
import com.dirtydish.app.dirtydish.R
import com.dirtydish.app.dirtydish.singletons.Session
import com.dirtydish.app.dirtydish.data.Chore
import com.dirtydish.app.dirtydish.data.HouseMate
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_edit_chore.*
import java.text.SimpleDateFormat
import java.util.*

class EditChoreFragment : Fragment() {
    private lateinit var db: FirebaseDatabase
    //private lateinit var choreRef: DatabaseReference
    private lateinit var houseRef: DatabaseReference
    var participantsList: MutableList<HouseMate> = mutableListOf<HouseMate>()
    var housematesArray: MutableList<HouseMate> = mutableListOf<HouseMate>()
    var choreArray: MutableList<Chore> = mutableListOf<Chore>()
    lateinit var chore: Chore
    private val READ_REQUEST_CODE = 101
    private var myContext: Context? = null
    val PICK_IMAGE = 1
    var previewImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = FirebaseDatabase.getInstance()
        //choreRef = db.getReference("chores")
        houseRef = db.getReference("houses").child(Session.userHouse!!.id)
        choreArray = Session.userHouse!!.chores

        chore = ChoreDetailFragmentArgs.fromBundle(arguments).chore
        (activity as? AppCompatActivity)?.supportActionBar?.title = chore?.name + " - Edit"

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_chore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editName: EditText = view.findViewById<EditText>(R.id.editName)
        val editFrequency: Spinner = view.findViewById<Spinner>(R.id.editFrequency)
        val freqType: Spinner = view.findViewById<Spinner>(R.id.freq_type)
        val startDate: TextView = view.findViewById<TextView>(R.id.startDate)
        val endDate: TextView = view.findViewById<TextView>(R.id.endDate)
        val description: EditText = view.findViewById<EditText>(R.id.description)
        //val participants: RecyclerView = view.findViewById<RecyclerView>(R.id.participants)
        val choreImagePreview: ImageView = view.findViewById<ImageView>(R.id.choreImagePreview)
        val btnPickImage: Button = view.findViewById<Button>(R.id.btnPickImage)
        val btnSave: Button = view.findViewById<Button>(R.id.btnSave)
        val btnDelete: Button = view.findViewById<Button>(R.id.btnDelete)

        editName.setText(chore!!.name)
        description.setText(chore!!.description)
        Log.d("EDIT_CHORE", "Frequency = " + chore!!.frequency.toString())

        when {
            chore!!.frequency % 30 == 0 -> {
                freqType.setSelection(2)
                editFrequency.setSelection(chore!!.frequency/30 - 1)
            }
            chore!!.frequency % 7 == 0 -> {
                freqType.setSelection(1)
                editFrequency.setSelection(chore!!.frequency/7 - 1)
            }
            else -> {
                freqType.setSelection(0)
                editFrequency.setSelection(chore!!.frequency - 1)
            }
        }

        startDate.text = chore!!.startDate
        endDate.text = chore!!.endDate



        previewImageView = choreImagePreview

        //TODO: re-instantiate the frequency in the right way?

        for (i in 0 until 5) {
            val housemate = HouseMate("John Smith " + i.toString(), "lmao@lmao.com", i.toString())
            housematesArray.add(housemate)
        }
        if (Session.userHouse != null) {
            housematesArray = Session.userHouse!!.houseMates
            choreArray = Session.userHouse!!.chores
        }

        participantsList = chore!!.participants

        val viewManager = LinearLayoutManager(this.requireContext())
        participants.layoutManager = viewManager
        val adapter = ParticipantsRecyclerAdapter(housematesArray, participantsList, this.requireContext())
        participants.adapter = adapter

        makeCalendarField(startDate)
        makeCalendarField(endDate)

        btnDelete.setOnClickListener {
            deleteChore(chore!!.id)
            view.findNavController().navigateUp()
            Toast.makeText(activity, "Chore deleted.", Toast.LENGTH_SHORT).show()
        }
        btnSave.setOnClickListener {
            editChore(chore!!.id)
            view.findNavController().navigateUp()
            Toast.makeText(activity, "Chore saved.", Toast.LENGTH_SHORT).show()
        }

        btnPickImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.myContext = context
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val selectedImage = data.data
            Picasso.get().load(selectedImage).into(previewImageView)
        }
        Toast.makeText(activity, "Imaged selected.", Toast.LENGTH_SHORT).show()
    }

    private fun editChore(key: String) {
        participantsList = mutableListOf()
        for (i in 0 until participants.childCount) {
            val partic = participants.getChildViewHolder(participants.getChildAt(i))
            if (partic.itemView.background != null){
                participantsList.add(housematesArray[i])
            }
        }
        if (participantsList.isEmpty()){
            return
        }

        var newAssignee = chore.assignee
        var assigneeInList = false;
        participantsList.forEach {
            if (it.id === newAssignee){
                assigneeInList = true
            }
        }
        if (!assigneeInList){
            newAssignee = participantsList[0].id
        }

        if (Session.hasHouse()) {
            var frequency = Integer.parseInt(editFrequency.selectedItem.toString())
            val frequencyType = freq_type.selectedItemPosition
            if (frequencyType == 1) {
                frequency *= 7
            } else if (frequencyType == 2) {
                frequency *= 30
            }

            val id = chore!!.id.toInt()

            val houseKey = Session.userHouse!!.id
            val newChore = Chore(
                    name = editName.text.toString(),
                    id = id.toString(),
                    frequency = frequency,
                    participants = participantsList,
                    houseId = houseKey,
                    description = description.text.toString(),
                    startDate = startDate.text.toString(),
                    endDate = endDate.text.toString(),
                    assignee = newAssignee)


            choreArray[id] = newChore
            houseRef.child("chores").setValue(choreArray)
        }
    }

    private fun deleteChore(key: String) {
        Log.d(tag, key)
        val id = Integer.parseInt(key)
        //choreRef.child(key).removeValue()
        //TODO: id is not local anymore but the actual chore id, so this may throw index out of bounds
        choreArray.removeAt(id)
        houseRef.child("chores").setValue(choreArray)
    }

    private fun makeCalendarField(textView: TextView) {
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
            Log.i(ContentValues.TAG, "Permission to record denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(activity!!,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_REQUEST_CODE)
    }

}
