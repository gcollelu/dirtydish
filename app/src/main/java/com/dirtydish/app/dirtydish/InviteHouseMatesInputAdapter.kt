package com.dirtydish.app.dirtydish

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.housemate_input_row.view.*

class InviteHouseMatesInputAdapter(private val context: Activity, private val housematesArray: MutableList<HouseMate>) : BaseAdapter() {
    private val inflater = context.layoutInflater
    private lateinit var parent: ViewGroup
    //1
    override fun getCount(): Int {
        return housematesArray.size
    }

    //2
    override fun getItem(position: Int): Any {
        return housematesArray[position]
    }

    //3
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //4
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        this.parent = parent
        // Get view for row item

        return inflater.inflate(R.layout.housemate_input_row, parent, false)
    }

    fun getHousemate(position: Int): HouseMate {
        val row = parent.getChildAt(position)
        return HouseMate(name = row.editName.text.toString(), email = row.editEmail.text.toString())
    }


}