package com.dirtydish.app.dirtydish

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_chore_detail.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ChoreDetailFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ChoreDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ChoreDetailFragment : Fragment() {
    var chore: Chore? = null
    var myView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myView = view
        chore = ChoreDetailFragmentArgs.fromBundle(arguments).chore



        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if (chore != null)
            activity!!.setTitle(chore!!.name)



        return inflater.inflate(R.layout.fragment_chore_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = view!!.findViewById<TextView>(R.id.chore_name)
        val assignedToField = view!!.findViewById<TextView>(R.id.chore_assignee)
        val frequencyField = view!!.findViewById<TextView>(R.id.chore_frequency)

        if (chore != null) {
            chore_name.text = chore!!.name
//            chore_assignee.text = chore!!.participants[0].name
            chore_frequency.text = Utilities.intFrequencyToString(chore!!.frequency)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.edit_chore_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_chore_button -> {
                //myView!!.findNavController().navigate(R.id.action_viewHouseFragment_to_shareHouseFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

}
