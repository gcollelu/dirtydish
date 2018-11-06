package com.dirtydish.app.dirtydish

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.support.v7.app.AppCompatActivity
import android.util.Log


class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

    var chore_name: TextView

    private var itemClickListener: ItemClickListener? = null

    init {
        chore_name = itemView.findViewById<View>(R.id.chore_name) as TextView

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
        val itemView = inflater.inflate(R.layout.chore, parent, false)
        return RecyclerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        //TODO pass chore object to ModifyChore instead of just id
        holder.chore_name.text = listData[position].name

        holder.setItemClickListener(object : ItemClickListener {
            override fun onClick(view: View, position: Int, isLongClick: Boolean) {
                if (isLongClick) {
                    Toast.makeText(context, "Long Click" + listData[position].name, Toast.LENGTH_SHORT).show()
                    val intent = Intent(context,ModifyChore::class.java).putExtra("id",
                            listData[position].id).putExtra("name", listData[position].name)
                    Log.d("ADAPTER_INTENT", context.toString())
                    context.startActivity(intent)
                } else
                    Toast.makeText(context, "Short Click" + listData[position].name, Toast.LENGTH_SHORT).show()
                    val intent = Intent(context,ModifyChore::class.java).putExtra("id",
                            listData[position].id).putExtra("name", listData[position].name)
                    Log.d("ADAPTER_INTENT", context.toString())
                    context.startActivity(intent)

            }
        })
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}
