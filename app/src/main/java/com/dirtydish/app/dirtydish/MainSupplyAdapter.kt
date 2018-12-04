package com.dirtydish.app.dirtydish

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dirtydish.app.dirtydish.data.Supply
import kotlinx.android.synthetic.main.supply_row.view.*

class MainSupplyAdapter(private val data: List<Supply>, val context: Context)
    : RecyclerView.Adapter<MainSupplyAdapter.SupplyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplyHolder {
        return SupplyHolder(LayoutInflater.from(context).inflate(R.layout.supply_row, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: SupplyHolder, position: Int) {
        var currSupply = data[position]
        holder.supplyName.text = currSupply.name
        holder.supplyAvailability.text = when (currSupply.missing) {
            true -> "Missing"
            false -> "In stock"
        }
    }

    class SupplyHolder(view: View) : RecyclerView.ViewHolder(view) {
        val supplyName: TextView = view.supply_name
        val supplyAvailability: TextView = view.availability
    }
}