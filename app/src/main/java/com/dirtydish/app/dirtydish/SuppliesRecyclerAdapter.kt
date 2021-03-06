package com.dirtydish.app.dirtydish

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dirtydish.app.dirtydish.data.Supply
import com.dirtydish.app.dirtydish.singletons.Session
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.anko.doAsync


private val tag = "SUPPLIES_RECYCLER_ADAPTER"

class SupplyRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

    var supply_name: TextView
    var availability: TextView

    private var itemClickListener: ItemClickListener? = null

    init {
        supply_name = itemView.findViewById<View>(R.id.chore_name) as TextView
        availability = itemView.findViewById<View>(R.id.availability) as TextView
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

class SuppliesRecyclerAdapter(private val listData: MutableList<Supply>, private val context: Context) : RecyclerView.Adapter<SupplyRecyclerViewHolder>() {
    private lateinit var db: FirebaseDatabase
    private lateinit var houseRef: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplyRecyclerViewHolder {
        db = FirebaseDatabase.getInstance()
        //choreRef = db.getReference("chores")
        houseRef = db.getReference("houses").child(Session.userHouse!!.id)
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.supply_row, parent, false)
        return SupplyRecyclerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SupplyRecyclerViewHolder, position: Int) {
        val currentSupply = listData[position]

        holder.supply_name.text = currentSupply.name

        if (currentSupply.missing)
            holder.availability.text = "Missing"
        else
            holder.availability.text = "Available"
        holder.setItemClickListener(object : ItemClickListener {
            override fun onClick(view: View, position: Int, isLongClick: Boolean) {
                listData[position].missing = !currentSupply.missing
                notifyItemChanged(position)
                doAsync { houseRef.child("supplies").setValue(listData) }
            }
        })
    }

    fun removeAt(position: Int) {
        listData.removeAt(position)
        notifyItemRemoved(position)
        reindexSupplies()
    }


    private fun reindexSupplies() {
        for(i in 0 until listData.size) {
            listData[i].id = i.toString()
        }
        doAsync { houseRef.child("supplies").setValue(listData) }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

}

