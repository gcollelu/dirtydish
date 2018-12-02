package com.dirtydish.app.dirtydish.house


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.dirtydish.app.dirtydish.MainMenuActivity
import com.dirtydish.app.dirtydish.PinEntryEditText
import com.dirtydish.app.dirtydish.R
import com.dirtydish.app.dirtydish.singletons.Session
import com.dirtydish.app.dirtydish.data.House
import com.dirtydish.app.dirtydish.data.HouseMate
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_join_house.*
import org.jetbrains.anko.doAsync
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


class JoinHouseFragment : Fragment() {

    private val LOG_TAG = "Barcode Scanner API"
    private val PHOTO_REQUEST = 10
    private var scanResults: TextView? = null
    private var detector: BarcodeDetector? = null
    private var imageUri: Uri? = null
    private val REQUEST_WRITE_PERMISSION = 20
    private var currImagePath: String? = null
    internal var imageFile: File? = null
    private lateinit var houseListRef: DatabaseReference
    private lateinit var houseMateRef: DatabaseReference
    private val houses: MutableList<House> = mutableListOf<House>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainMenuActivity).supportActionBar!!.hide()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_join_house, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = FirebaseDatabase.getInstance()
        houseListRef = db.getReference("houses")
        houseMateRef = db.getReference("housemates")

        houseListRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.mapNotNullTo(houses) { it.getValue<House>(House::class.java) }
                Log.i("Count " ,""+snapshot.getChildrenCount())
            }
        })

        val txtPinEntry: PinEntryEditText = pin_entry_edit as PinEntryEditText
        txtPinEntry.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                var houseFound = false
                if (s?.length == 4){
                    for (i in 0 until houses.size){
                        if (s.toString().equals(houses[i].pin)){
                            Log.i("CURRENTUSER", Session.housemate?.name)
                            houses[i].houseMates.add(Session.housemate!!)
                            var key: String = ""
                            doAsync { houseListRef.child(houses[i].id).child("houseMates")
                                    .setValue(houses[i].houseMates) }
                            val houseMate = Session.housemate!!
                            houseMate.houseId = houses[i].id
                            doAsync { houseMateRef.child(houseMate.id).setValue(houseMate) }
                            houseFound = true
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                            view.findNavController().navigate(R.id.action_joinHouseFragment_to_homeFragment)
                        }
                    }
                    if (!houseFound){
                        Toast.makeText(context, "Incorrect", Toast.LENGTH_SHORT).show()
                        txtPinEntry.text = null
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        btnScanCode.setOnClickListener {
            ActivityCompat.requestPermissions(activity as Activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission_group.CAMERA, Manifest.permission.CAMERA), REQUEST_WRITE_PERMISSION)
            if (checkPermission(context!!))
                takePicture()
            else
                Toast.makeText(activity, "Permission Denied!", Toast.LENGTH_SHORT).show()
        }

        detector = BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build()
        if (!detector!!.isOperational) {
            //scanResults!!.text = "Could not set up the detector!"
            return
        }

    }

    private fun registeredId(hm: HouseMate, list: MutableList<HouseMate>): String? {
        for (user in list) {
            if (hm.email == user.email) {
                return user.id
            }
        }
        return null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_WRITE_PERMISSION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture()
            } else {
                Toast.makeText(activity, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHOTO_REQUEST && resultCode == Activity.RESULT_OK) {
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            mediaScanIntent.data = imageUri
            launchMediaScanIntent(mediaScanIntent)
            try {
                val bitmap = decodeBitmapUri(context!!, imageUri)
                if (detector!!.isOperational && bitmap != null) {
                    val frame = Frame.Builder().setBitmap(bitmap).build()
                    val barcodes = detector!!.detect(frame)
                    for (index in 0 until barcodes.size()) {
                        val code = barcodes.valueAt(index)
                        scanResults!!.text = scanResults!!.text.toString() + code.displayValue
                        val type = barcodes.valueAt(index).valueFormat
                        when (type) {
                            Barcode.CONTACT_INFO -> Log.i(LOG_TAG, code.contactInfo.title)
                            Barcode.EMAIL -> Log.i(LOG_TAG, code.email.address)
                            Barcode.ISBN -> Log.i(LOG_TAG, code.rawValue)
                            Barcode.PHONE -> Log.i(LOG_TAG, code.phone.number)
                            Barcode.PRODUCT -> Log.i(LOG_TAG, code.rawValue)
                            Barcode.SMS -> Log.i(LOG_TAG, code.sms.message)
                            Barcode.TEXT -> Log.i(LOG_TAG, code.rawValue)
                            Barcode.URL -> Log.i(LOG_TAG, "url: " + code.url.url)
                            Barcode.WIFI -> Log.i(LOG_TAG, code.wifi.ssid)
                            Barcode.GEO -> Log.i(LOG_TAG, code.geoPoint.lat.toString() + ":" + code.geoPoint.lng)
                            Barcode.CALENDAR_EVENT -> Log.i(LOG_TAG, code.calendarEvent.description)
                            Barcode.DRIVER_LICENSE -> Log.i(LOG_TAG, code.driverLicense.licenseNumber)
                            else -> Log.i(LOG_TAG, code.rawValue)
                        }
                    }
                    if (barcodes.size() == 0) {
                        scanResults!!.text = getString(R.string.scanFailed)
                    }
                } else {
                    scanResults!!.text = getString(R.string.cantSetupBarcodeDetector)
                }
            } catch (e: Exception) {
                Toast.makeText(activity, "Failed to load Image", Toast.LENGTH_SHORT)
                        .show()
                Log.e(LOG_TAG, e.toString())
            }

        }
    }

    private fun takePicture() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            imageFile = createImageFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val authorities: String = activity?.applicationContext?.packageName + ".fileprovider"



        imageUri = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            Uri.fromFile(imageFile)
        } else {
            FileProvider.getUriForFile(activity as MainMenuActivity, authorities, imageFile!!)
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        if (intent.resolveActivity(activity!!.packageManager) != null) {
            startActivityForResult(intent, PHOTO_REQUEST)
        }

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val storageDir = File(Environment.getExternalStorageDirectory(), "picture.jpg")
        if (!storageDir.exists()) {
            storageDir.parentFile.mkdirs()
            storageDir.createNewFile()
        }
        currImagePath = storageDir.absolutePath
        return storageDir
    }

    private fun launchMediaScanIntent(mediaScanIntent: Intent) {

        activity?.sendBroadcast(mediaScanIntent)
    }

    @Throws(FileNotFoundException::class)
    private fun decodeBitmapUri(ctx: Context, uri: Uri?): Bitmap? {

        val targetW = 600
        val targetH = 600
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true

        BitmapFactory.decodeStream(ctx.contentResolver.openInputStream(if (uri != null) uri else throw NullPointerException("Expression 'uri' must not be null")), null, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        val scaleFactor = Math.min(photoW / targetW, photoH / targetH)

        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor

        return BitmapFactory.decodeStream(ctx.contentResolver
                .openInputStream(uri), null, bmOptions)
    }

    private fun checkPermission(context: Context): Boolean {

        val camera = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
        val writeStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)


        if (camera != PackageManager.PERMISSION_GRANTED || writeStorage != PackageManager.PERMISSION_GRANTED)
            return false


        return true
    }
}
