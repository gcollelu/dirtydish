package com.dirtydish.app.dirtydish

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import androidx.navigation.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.chore_min.view.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    var myView: View? = null
    private lateinit var listener: ValueEventListener
    private lateinit var houseRef: DatabaseReference
    private lateinit var db: FirebaseDatabase

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home,
                container, false)
        db = FirebaseDatabase.getInstance()
        myView = view
        (activity as MainMenuActivity).supportActionBar!!.show()
        setHasOptionsMenu(true)

        if (Session.hasHouse()) {
            houseRef = db.getReference("houses").child(Session.userHouse!!.id)
            houseRef.keepSynced(true)
        }

//        if (!Session.hasHouse())
//            view?.findNavController()?.navigate(R.id.selectHouseFragment)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myChoresList.layoutManager = LinearLayoutManager(activity as Context)

        if (Session.hasHouse()) {
            myChoresList.adapter = MainChoreAdapter(Session.userHouse!!.chores, activity as Context)
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