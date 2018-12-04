package com.dirtydish.app.dirtydish.house


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.dirtydish.app.dirtydish.MainMenuActivity
import com.dirtydish.app.dirtydish.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_select_house.*


class SelectHouseFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainMenuActivity).supportActionBar!!.hide()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_house, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnJoinHouse.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            auth.currentUser!!.reload()
            if(!auth.currentUser!!.isEmailVerified){
                Toast.makeText(context, "Email not verified", Toast.LENGTH_LONG).show()
            } else {
                view.findNavController().navigate(R.id.action_selectHouseFragment_to_joinHouseFragment)
            }
        }
        btnNewHouse.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            auth.currentUser!!.reload()
            if(!auth.currentUser!!.isEmailVerified){
                Toast.makeText(context, "Email not verified", Toast.LENGTH_LONG).show()
            } else {
                view.findNavController().navigate(R.id.action_selectHouseFragment_to_setupHouseFragment)
            }
        }
    }


}
