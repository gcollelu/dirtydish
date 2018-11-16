package com.dirtydish.app.dirtydish

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.TextView
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_chore_detail.*


class ChoreDetailFragment : Fragment() {
    var chore: Chore? = null
    var myView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chore = ChoreDetailFragmentArgs.fromBundle(arguments).chore

        (activity as? AppCompatActivity)?.supportActionBar?.title = chore?.name

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chore_detail, container, false)
        if (chore != null)
            activity!!.setTitle(chore!!.name)

        myView = view

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val startDate: TextView = view.findViewById<TextView>(R.id.startDate)
        val endDate: TextView = view.findViewById<TextView>(R.id.endDate)

        if (chore != null) {
            chore_name.text = chore!!.name

            chore_frequency.text = Utilities.intFrequencyToString(chore!!.frequency)
            choreDescription.text = chore!!.description

            if (chore!!.participants.size > 0){
                chore_assignee.text = chore!!.participants[0].name
            }

            val participantAdapter = ViewHouseMatesSimpleAdapter(activity!!, chore!!.participants)
            participants.adapter = participantAdapter

            startDate.text = chore!!.startDate
            endDate.text = chore!!.endDate

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.edit_chore_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_chore_button -> {
                val directions = ChoreDetailFragmentDirections.ActionChoreDetailFragmentToEditChoreFragment(this.chore!!)
                myView!!.findNavController().navigate(directions)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

}
