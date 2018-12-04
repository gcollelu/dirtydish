package com.dirtydish.app.dirtydish.chores

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.dirtydish.app.dirtydish.R
import com.dirtydish.app.dirtydish.data.Chore
import com.dirtydish.app.dirtydish.data.HouseMate
import com.dirtydish.app.dirtydish.singletons.Session
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_add_chore.*
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
    private val RESULT_LOAD_IMAGE = 1
    var previewImageView: ImageView? = null
    var houseName = ""

    private lateinit var imageName:String
    private var imageURL = ""
    internal var storage:FirebaseStorage?=null
    internal var storageReference:StorageReference?=null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_chore,
                container, false)

        db = FirebaseDatabase.getInstance()
        houseListRef = db.getReference("houses")

        choreRef = db.getReference("houses").child(Session.userHouse!!.id)

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        houseName = Session.userHouse!!.id
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

        val startDate: TextView = view.findViewById<TextView>(R.id.startDate)
        val endDate: TextView = view.findViewById<TextView>(R.id.endDate)
        val participants: RecyclerView = view.findViewById<RecyclerView>(R.id.participants)
        val choreImagePreview: ImageView = view.findViewById<ImageView>(R.id.choreImagePreview)
        val btnPickImage: Button = view.findViewById<Button>(R.id.btnPickImage)

        previewImageView = choreImagePreview

        val viewManager = LinearLayoutManager(this.requireContext())
        participants.layoutManager = viewManager
        val adapter = ParticipantsRecyclerAdapter(housematesArray, participantsList, this.requireContext())
        participants.adapter = adapter

        btnDone.setOnClickListener {
            if (createChore()) {
                view.findNavController().navigateUp()
                Toast.makeText(activity, "Chore created.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Select at least one housemates to assign the chore to", Toast.LENGTH_SHORT).show()
            }
        }

        btnPickImage.setOnClickListener {
            setupPermissions()
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
        }


        makeCalendarField(startDate)
        makeCalendarField(endDate)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.myContext = context
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null && data.data != null) {
            val selectedImage = data.data
            val progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()
            imageName = UUID.randomUUID().toString()

            val imageRef = storageReference!!.child("$houseName/chores/$imageName")
            val uploadTask = imageRef.putFile(selectedImage!!)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(context, "File Uploaded", Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener{
                        progressDialog.dismiss()
                        Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show()
                    }
                    .addOnProgressListener {taskSnapShot->
                        val progress = 100.0 * taskSnapShot.bytesTransferred/taskSnapShot.totalByteCount
                        progressDialog.setMessage("Uploaded " + progress.toInt() + "%...")
                    }

            val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation imageRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    imageURL = task.result.toString()
                    Log.d("IMAGEURL", imageURL)
                } else {
                    // Handle failures
                    // ...
                }
            }
            Log.d("IMAGEURL", imageRef.toString())
            Picasso.get().load(selectedImage).into(previewImageView)
        }
        Toast.makeText(activity, "Imaged selected.", Toast.LENGTH_SHORT).show()
    }

    private fun createChore(): Boolean {
        if (Session.hasHouse()) {
            participantsList = mutableListOf()
            for (i in 0 until participants.childCount) {
                val partic = participants.getChildViewHolder(participants.getChildAt(i))
                if (partic.itemView.background != null) {
                    participantsList.add(housematesArray[i])
                }
            }

            var frequency = Integer.parseInt(editFrequency.selectedItem.toString())
            var frequencyType = freq_type.selectedItemPosition
            if (frequencyType == 1) {
                frequency *= 7
            } else if (frequencyType == 2) {
                frequency *= 30
            }

            var id = 0

            if (!Session.userHouse!!.chores.isEmpty())
                id = Session.userHouse!!.chores.lastIndex + 1

            val houseKey = Session.userHouse!!.id;

            return if (participantsList.isEmpty()) {
                false
            } else {
                val chore = Chore(
                        name = editName.text.toString(),
                        id = id.toString(),
                        frequency = frequency,
                        participants = participantsList,
                        houseId = houseKey,
                        description = description.text.toString(),
                        startDate = startDate.text.toString(),
                        endDate = endDate.text.toString(),
                        assignee = participantsList[0].id,
                        image = "")
                if (imageURL!="")
                    chore.image = imageURL
                Log.d(tag_local, chore.toString())
                choreArray.add(chore)
                choreRef.child("chores").setValue(choreArray)
                true
            }
        }

        return false
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
            DatePickerDialog(context!!, dateSetListener,
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
