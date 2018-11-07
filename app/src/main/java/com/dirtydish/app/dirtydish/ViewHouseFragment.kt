package com.dirtydish.app.dirtydish

import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.activity_view_house.*
import kotlinx.android.synthetic.main.app_bar_main_menu.*

class ViewHouseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_view_house,
                container, false)

        return view
    }
}
