package com.dirtydish.app.dirtydish

import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater

import kotlinx.android.synthetic.main.activity_view_house.*
import kotlinx.android.synthetic.main.app_bar_main_menu.*

class ViewHouseActivity : CustomMenuDrawer() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //------------------------ADD ACTUAL PAGE------------------------
        val mainLayout = main_container as ConstraintLayout
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.activity_view_house, null)
        mainLayout.removeAllViews()
        mainLayout.addView(layout)
        //---------------------------------------------------------------

    }
}
