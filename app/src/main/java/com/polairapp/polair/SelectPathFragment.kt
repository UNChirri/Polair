package com.polairapp.polair

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_select_path.*

class SelectPathFragment: Fragment() {

    lateinit var mContext: BackListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_select_path,container,false)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is BackListener)
            mContext = context
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        rdbtnHouseToU.setOnClickListener {
            rdbtnWorkToHouse.isChecked = false
        }

        rdbtnWorkToHouse.setOnClickListener {
            rdbtnHouseToU.isChecked = false
        }

        imvBack.setOnClickListener {
            mContext.onBackFragmentClick()
        }
    }

    fun showSavedRoutes() {
        consLaySavedPaths.visibility = View.VISIBLE
    }

    fun hideSavedRoutes() {
        consLaySavedPaths.visibility = View.GONE
    }

    interface BackListener{
        fun onBackFragmentClick()
    }

}