package com.dirtydish.app.dirtydish

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController


class ParticipantsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

    var participantName: TextView
    var selected: Boolean

    private var itemClickListener: ItemClickListener? = null

    init {
        participantName = itemView.findViewById<View>(R.id.nameField) as TextView
        selected = false

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

class ParticipantsRecyclerAdapter(private val houseMates: MutableList<HouseMate>,  private val participants: MutableList<HouseMate>,
                                  private val context: Context) : RecyclerView.Adapter<ParticipantsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.housemate_view_row, parent, false)
        return ParticipantsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ParticipantsViewHolder, position: Int) {

        holder.participantName.text = houseMates[position].name

        if (participants.contains(houseMates[position])){
            holder.selected = true
            holder.participantName.background = ContextCompat.getDrawable(context, R.drawable.view_round_secondary)
            holder.itemView.background = ContextCompat.getDrawable(context, R.drawable.view_rectangle_white)
        }

        holder.setItemClickListener(object : ItemClickListener {
            override fun onClick(view: View, position: Int, isLongClick: Boolean) {
                if (!holder.selected){
                    participants.remove(houseMates[position])
                    holder.participantName.background = ContextCompat.getDrawable(context, R.drawable.view_round_secondary)
                    view.background = ContextCompat.getDrawable(context, R.drawable.view_rectangle_white)
                } else {
                    participants.add(houseMates[position])
                    holder.participantName.background = ContextCompat.getDrawable(context, R.drawable.view_round_primary)
                    view.background = null
                }
                holder.selected = !holder.selected
            }
        })
    }

    override fun getItemCount(): Int {
        return houseMates.size
    }

    public fun getSelected(): Boolean {
        return true
    }



}
