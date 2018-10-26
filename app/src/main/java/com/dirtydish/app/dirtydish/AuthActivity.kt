package com.dirtydish.app.dirtydish

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val tag = "AUTH"

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        auth = FirebaseAuth.getInstance()
        setupUIOnCreate()
    }

    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        updateUIPostAuth(currentUser)
    }

    // Changes the layout UI when the activity is created
    private fun setupUIOnCreate() {
        btnLogin.setOnClickListener { login() }
        btnSignup.setOnClickListener { register() }
    }

    // Changes the layout UI elements based on if the user is logged in
    private fun updateUIPostAuth(currentUser: FirebaseUser?) {
        if (currentUser == null) {
            //displayUser.text = "None"
        } else {
           // displayUser.text = "${currentUser.email}"
        }
    }

    private fun verifyEmail(email: String): Boolean {
        return !email.isEmpty()
    }

    private fun verifyPassword(pass: String): Boolean {
        return !pass.isEmpty()
    }

    private fun register() {
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()
        if (!verifyEmail(email) || !verifyPassword(password)) {
            Toast.makeText(this, "Email or password do not satisfy the requirements.", Toast.LENGTH_SHORT).show()
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isComplete && it.isSuccessful) {
                        Toast.makeText(this, "Registered successfully.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Registration failed.", Toast.LENGTH_SHORT).show()
                        Log.d(tag, "Signup fail ${it.exception.toString()}")
                    }
                }
    }

    private fun login() {
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            return
        }
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isComplete && it.isSuccessful) {
                        Toast.makeText(this, "Logged in.", Toast.LENGTH_SHORT).show()
                        updateUIPostAuth(auth.currentUser)
                    } else {
                        Toast.makeText(this, "Login failed.", Toast.LENGTH_SHORT).show()
                        Log.d(tag, "Login fail ${it.exception.toString()}")
                    }
                }
    }

}