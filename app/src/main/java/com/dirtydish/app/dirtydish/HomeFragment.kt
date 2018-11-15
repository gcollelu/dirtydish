package com.dirtydish.app.dirtydish

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.chore_min.view.*
import kotlinx.android.synthetic.main.homepage.*

class HomeFragment : Fragment() {

    var myView: View? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.homepage,
                container, false)
        myView = view

        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myChoresList.layoutManager = LinearLayoutManager(activity as Context)
        val house = Session.userHouse
        if (house != null && house.chores != null) {
            myChoresList.adapter = MainChoreAdapter(house.chores, activity as Context)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_homepage, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.notification_button -> {
                myView!!.findNavController().navigate(R.id.action_homeFragment_to_notificationsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    class MainChoreAdapter(private val data: List<Chore>, val context: Context) : RecyclerView.Adapter<MainChoreAdapter.ChoreHolder>(){

        class ChoreHolder(view: View) : RecyclerView.ViewHolder(view) {
            val choreName: TextView? = view.chore_name
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

}