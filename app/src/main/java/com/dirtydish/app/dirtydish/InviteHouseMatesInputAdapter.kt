package com.dirtydish.app.dirtydish

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.Toast

class InviteHouseMatesInputAdapter(private val context: Activity, private val housematesArray: MutableList<HouseMate>) : BaseAdapter() {
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
        val rowView = inflater.inflate(R.layout.housemate_input_row, parent, false)
        val inputName = rowView.findViewById(R.id.editName) as EditText
        val inputEmail = rowView.findViewById(R.id.editEmail) as EditText

        inputEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                housematesArray[position].email = inputEmail.text.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        inputName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                housematesArray[position].name = inputName.text.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })


        return rowView
    }

    fun getHousmate(position: Int) : HouseMate{
        return housematesArray[position]
    }


}