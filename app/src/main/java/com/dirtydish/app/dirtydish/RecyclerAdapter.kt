package com.dirtydish.app.dirtydish

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

private val tag = "RECYCLER_ADAPTER"

class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

    var chore_name: TextView
    var frequency: TextView
    //val choreTimeFrame: TextView
    val choreAssignee: TextView

    private var itemClickListener: ItemClickListener? = null

    init {
        chore_name = itemView.findViewById<View>(R.id.chore_name) as TextView
        frequency = itemView.findViewById<View>(R.id.chore_frequency) as TextView
        //choreTimeFrame = itemView.findViewById<View>(R.id.chore_time_frame) as TextView
        choreAssignee = itemView.findViewById<View>(R.id.chore_assignee) as TextView

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

class RecyclerAdapter(private val listData: List<Chore>, private val context: Context) : RecyclerView.Adapter<RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.chore_row, parent, false)
        return RecyclerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val currentChore = listData[position]
        holder.chore_name.text = currentChore.name
        var frequency_text = ""
        val freq = currentChore.frequency
        if (freq % 7 == 0) {
            frequency_text = (freq / 7).toString()
            if (freq == 7) frequency_text = "week"
            else frequency_text += " weeks"

        } else if (freq % 30 == 0) {
            frequency_text = (freq / 30).toString()
            if (freq == 30) frequency_text = "month"
            else frequency_text += " months"

        } else {
            frequency_text = freq.toString()
            if (freq == 1) frequency_text = "day"
            else frequency_text += " days"
        }

        holder.frequency.text = frequency_text

        if (currentChore.participants.size > 0) {
            //TODO: ACTUALLY ROTATE BETWEEN ALL CHORE PARTICIPANTS
            holder.choreAssignee.text = currentChore.participants[0].name
        } else {
            holder.choreAssignee.text = "none"
        }

        holder.setItemClickListener(object : ItemClickListener {
            override fun onClick(view: View, position: Int, isLongClick: Boolean) {
                Log.i("FREQUENCY", "frequency=" + currentChore.frequency)
                if (isLongClick) {
                    Toast.makeText(context, "Long Click" + currentChore.name, Toast.LENGTH_SHORT).show()
                    val intent = Intent(context.applicationContext,ModifyChore::class.java)
                            .putExtra("id", currentChore.id)
                            .putExtra("name", currentChore.name)
                            .putExtra("frequency", currentChore.frequency)
                            .putExtra("participants", currentChore.participants.toTypedArray())
                    Log.d(tag, context.toString())
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "Short Click" + currentChore.name, Toast.LENGTH_SHORT).show()
                    val intent = Intent(context.applicationContext, ModifyChore::class.java)
                            .putExtra("id", currentChore.id)
                            .putExtra("name", currentChore.name)
                            .putExtra("frequency", currentChore.frequency)
                            .putExtra("participants", currentChore.participants.toTypedArray())
                    Log.d(tag, context.toString())
                    context.startActivity(intent)
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return listData.size
    }

}
