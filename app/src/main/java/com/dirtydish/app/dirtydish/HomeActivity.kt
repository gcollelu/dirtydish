package com.dirtydish.app.dirtydish

import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.app_bar_main_menu.*

class HomeActivity : CustomMenuDrawer() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //------------------------ADD ACTUAL PAGE------------------------
        val mainLayout = main_container as ConstraintLayout
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.homepage, null)
        mainLayout.removeAllViews()
        mainLayout.addView(layout)
        //---------------------------------------------------------------

    }
}
