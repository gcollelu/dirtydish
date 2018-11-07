package com.dirtydish.app.dirtydish

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class SharedSuppliesFragment : Fragment() {

    companion object {
        fun newInstance() = SharedSuppliesFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shared_supplies_fragment, container, false)
    }


}
