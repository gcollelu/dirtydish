package com.dirtydish.app.dirtydish.chores

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import com.dirtydish.app.dirtydish.ItemClickListener
import com.dirtydish.app.dirtydish.R
import com.dirtydish.app.dirtydish.data.Chore
import com.dirtydish.app.dirtydish.singletons.Utilities
import com.squareup.picasso.Picasso

private val tag = "RECYCLER_ADAPTER"

class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

    var chore_name: TextView
    var choreImage: ImageView
    var frequency: TextView
    //val choreTimeFrame: TextView
    val choreAssignee: TextView
    private lateinit var imageName:String

    private var itemClickListener: ItemClickListener? = null

    init {
        chore_name = itemView.findViewById<View>(R.id.chore_name) as TextView
        frequency = itemView.findViewById<View>(R.id.chore_frequency) as TextView
        //choreTimeFrame = itemView.findViewById<View>(R.id.chore_time_frame) as TextView
        choreAssignee = itemView.findViewById<View>(R.id.chore_assignee) as TextView
        choreImage = itemView.findViewById<ImageView>(R.id.choreImage) as ImageView

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
        if(currentChore.image != null && currentChore.image != "")
            Picasso.get().load(currentChore.image).into(holder.choreImage)

        holder.frequency.text = Utilities.intFrequencyToString(currentChore.frequency)

        if (currentChore.participants.size > 0) {
            var assigneeName: String = currentChore.participants[0].name;
            currentChore.participants.forEach {
                if (it.id === currentChore.assignee){
                    assigneeName = it.name
                }
            }
            holder.choreAssignee.text = assigneeName
        } else {
            holder.choreAssignee.text = "none"
        }

        holder.setItemClickListener(object : ItemClickListener {
            override fun onClick(view: View, position: Int, isLongClick: Boolean) {
                val directions = ChoresFragmentDirections.ActionChoresFragmentToChoreDetailFragment(currentChore)
                view.findNavController().navigate(directions)
            }
        })
    }

    override fun getItemCount(): Int {
        return listData.size
    }

}
