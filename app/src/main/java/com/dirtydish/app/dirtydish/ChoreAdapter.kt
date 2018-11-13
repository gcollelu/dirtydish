package com.dirtydish.app.dirtydish

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.chore.view.*

class ChoreAdapter(private val data: List<Chore>, val context: Context) : RecyclerView.Adapter<ChoreAdapter.ChoreHolder>() {

    class ChoreHolder(view: View) : RecyclerView.ViewHolder(view) {
        val choreName: TextView? = view.chore_name
        val choreFrequency: TextView? = view.chore_frequency
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoreAdapter.ChoreHolder {
        return ChoreHolder(LayoutInflater.from(context).inflate(R.layout.chore, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ChoreAdapter.ChoreHolder, position: Int) {
        var currChore = data[position]
        holder.choreName?.text = currChore.name
        holder.choreFrequency?.text = currChore.frequency.toString()
    }

}