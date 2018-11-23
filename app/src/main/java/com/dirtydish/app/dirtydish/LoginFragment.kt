package com.dirtydish.app.dirtydish


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.dirtydish.app.dirtydish.singletons.Session
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var housemateRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Session.logout()


        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        housemateRef = db.getReference("housemates")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (activity as MainMenuActivity).supportActionBar!!.hide()
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUIOnCreate()

    }

    private fun setupUIOnCreate() {
        btnLogin.setOnClickListener { login() }
        btnNewHouse.setOnClickListener {
            val directions = LoginFragmentDirections.ActionLoginFragmentToSignUpFragment(editEmail.text.toString())
            view?.findNavController()?.navigate(directions)
        }
    }


    private fun login() {
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(activity, "Enter Email & Password.", Toast.LENGTH_SHORT).show()
            return
        }
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isComplete && it.isSuccessful) {
                        Toast.makeText(activity, "Logged in.", Toast.LENGTH_SHORT).show()
                        Session.init()
                        view?.findNavController()?.navigate(R.id.homeFragment)
                    } else {
                        Toast.makeText(activity, "Login failed.", Toast.LENGTH_SHORT).show()
                        Log.d(tag, "Login fail ${it.exception.toString()}")
                    }
                }
    }

}
