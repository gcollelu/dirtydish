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

class NotificationRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

    var description: TextView

    private var itemClickListener: ItemClickListener? = null

    init {
        description = itemView.findViewById<View>(R.id.description) as TextView
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

class NotificationsRecyclerAdapter(private val listData: MutableList<String>, private val context: Context) : RecyclerView.Adapter<NotificationRecyclerViewHolder>() {
    private lateinit var db: FirebaseDatabase
    private lateinit var houseRef: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationRecyclerViewHolder {
        db = FirebaseDatabase.getInstance()
        //choreRef = db.getReference("chores")
        houseRef = db.getReference("houses").child(Session.userHouse!!.id)
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.notification_row, parent, false)
        return NotificationRecyclerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotificationRecyclerViewHolder, position: Int) {
        val currentSupply = listData[position]

        holder.description.text = listData[position]

        holder.setItemClickListener(object : ItemClickListener {
            override fun onClick(view: View, position: Int, isLongClick: Boolean) {
                //TODO: implement on click notification
            }
        })
    }

    override fun getItemCount(): Int {
        return listData.size
    }

}