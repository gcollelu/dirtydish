package com.dirtydish.app.dirtydish

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dirtydish.app.dirtydish.data.Chore
import kotlinx.android.synthetic.main.chore_min.view.*


class MainChoreAdapter(private val data: List<Chore>, val context: Context) : RecyclerView.Adapter<MainChoreAdapter.ChoreHolder>() {

    class ChoreHolder(view: View) : RecyclerView.ViewHolder(view) {
        val choreName: TextView? = view.supply_name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainChoreAdapter.ChoreHolder {
        return ChoreHolder(LayoutInflater.from(context).inflate(R.layout.chore_min, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MainChoreAdapter.ChoreHolder, position: Int) {
        var currChore = data[position]
        holder.choreName?.text = currChore.name
    }

}