package com.dirtydish.app.dirtydish.house


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import com.dirtydish.app.dirtydish.house.EditHousemateFragmentArgs
import com.dirtydish.app.dirtydish.R
import com.dirtydish.app.dirtydish.data.HouseMate
import kotlinx.android.synthetic.main.fragment_edit_housemate.*


class EditHousemateFragment : Fragment() {
    val housematesArray: MutableList<HouseMate> = mutableListOf<HouseMate>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_housemate,
                container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id: String = EditHousemateFragmentArgs.fromBundle(arguments).housemate.id
        val name: String = EditHousemateFragmentArgs.fromBundle(arguments).housemate.name
        val email: String = EditHousemateFragmentArgs.fromBundle(arguments).housemate.email


        editHousemateEmail.setText(email, TextView.BufferType.EDITABLE)
        editHousemateName.setText(name, TextView.BufferType.EDITABLE)

        btnSaveHousemate.setOnClickListener {
            //TODO: save the actual housemate
            view.findNavController().navigateUp()
        }

    }
}
