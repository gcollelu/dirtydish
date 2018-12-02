package com.dirtydish.app.dirtydish

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import com.dirtydish.app.dirtydish.chores.ChoresFragmentDirections
import com.dirtydish.app.dirtydish.data.Supply


private val tag = "SUPPLIES_RECYCLER_ADAPTER"

class SupplyRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

    var supply_name: TextView

    private var itemClickListener: ItemClickListener? = null

    init {
        supply_name = itemView.findViewById<View>(R.id.chore_name) as TextView

        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun onClick(v: View) {
        itemClickListener!!.onClick(v, adapterPosition, false)
    }

    override fun onLongClick(v: View): Boolean {
        itemClickListener!!.onClick(v, adapterPosition, true)
        return true
    }
}

class SuppliesRecyclerAdapter(private val listData: List<Supply>, private val context: Context) : RecyclerView.Adapter<SupplyRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplyRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.chore_row, parent, false)
        return SupplyRecyclerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SupplyRecyclerViewHolder, position: Int) {
        val currentSupply = listData[position]


        holder.setItemClickListener(object : ItemClickListener {
            override fun onClick(view: View, position: Int, isLongClick: Boolean) {
                //TODO: what happens when you click on a supply?
            }
        })
    }

    override fun getItemCount(): Int {
        return listData.size
    }

}
