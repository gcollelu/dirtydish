package com.dirtydish.app.dirtydish.house

import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.dirtydish.app.dirtydish.R
import com.dirtydish.app.dirtydish.data.HouseMate
import kotlinx.android.synthetic.main.housemate_view_row.view.*

class ViewHouseMatesAdapter(private val context: FragmentActivity, private val housematesArray: MutableList<HouseMate>) : BaseAdapter() {
    private val inflater = context.layoutInflater
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
        // Get view for row item
        val rowView = inflater.inflate(R.layout.housemate_view_row, parent, false)
        rowView.nameField.text = getHousemate(position).name

        return rowView
    }


    fun getHousemate(position: Int): HouseMate {
        return housematesArray[position]
    }

}