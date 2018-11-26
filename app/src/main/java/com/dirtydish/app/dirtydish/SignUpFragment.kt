package com.dirtydish.app.dirtydish

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.dirtydish.app.dirtydish.data.HouseMate
import com.dirtydish.app.dirtydish.singletons.Session
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : Fragment() {
    var emailAddress = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var housemateRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        emailAddress = SignUpFragmentArgs.fromBundle(arguments).email
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        housemateRef = db.getReference("housemates")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val emailField = view?.findViewById<EditText>(R.id.editEmail)
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editEmail.setText(emailAddress)


        btnSignUp.setOnClickListener {
            register()
        }

    }


    private fun register() {
        val email = editEmail.text.toString()
        val name = editName.text.toString()
        val password = editPassword.text.toString()
        if (!verifyEmail(email) || !verifyPassword(password) || name.isEmpty()) {
            Toast.makeText(activity, "Please input all fields.", Toast.LENGTH_SHORT).show()
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isComplete && it.isSuccessful) {
                        Toast.makeText(activity, "Registered successfully.", Toast.LENGTH_SHORT).show()
                        createHouseMate(email, name, auth.currentUser?.uid)
                        Session.init()
                        view?.findNavController()?.navigate(R.id.action_signUpFragment_to_selectHouseFragment)
                    } else {
                        Toast.makeText(activity, "Registration failed.", Toast.LENGTH_SHORT).show()
                        Log.d(tag, "Signup fail ${it.exception.toString()}")
                    }
                }
    }

    private fun createHouseMate(email: String, name: String, id: String?) {
        if (id != null) {
            val user = HouseMate(email = email, name = name, id = id)
            housemateRef.child(id).setValue(user)
        }
    }


    private fun verifyEmail(email: String): Boolean {
        return !email.isEmpty()
    }

    private fun verifyPassword(pass: String): Boolean {
        return !pass.isEmpty()
    }


}
