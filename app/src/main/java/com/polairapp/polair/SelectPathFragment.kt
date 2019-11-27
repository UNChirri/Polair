package com.polairapp.polair

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_select_path.*

class SelectPathFragment: Fragment() {

    lateinit var mContext: BackListener
    var startPath = MutableLiveData<LatLng>()
    var finisPath = MutableLiveData<LatLng>()
    var bicycleFlag = MutableLiveData<Boolean>().apply { value = true }

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

    override fun onResume() {
        super.onResume()
        clear()
        bicycleFlag.value = true
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

        edtStartPath.setOnTouchListener { _, _ ->
            linLayStartPathHistory.visibility = View.VISIBLE
            edtFinishPath.visibility = View.INVISIBLE
            false
        }

        edtFinishPath.setOnTouchListener{_,_ ->
            linLayFinishPathHistory.visibility = View.VISIBLE
            false
        }
        txvNavalClub.setOnClickListener {
            linLayStartPathHistory.visibility = View.GONE
            startPath.value = LatLng(19.421086, -99.199854)
            edtStartPath.setText(txvNavalClub.text)
            edtFinishPath.visibility = View.VISIBLE
        }

        txvAngelaTheatre.setOnClickListener {
            linLayStartPathHistory.visibility = View.GONE
            startPath.value = LatLng(19.429074, -99.194224)
            edtStartPath.setText(txvAngelaTheatre.text)
            edtFinishPath.visibility = View.VISIBLE
        }

        txvCentroCulturalDigital.setOnClickListener {
            linLayFinishPathHistory.visibility = View.INVISIBLE
            finisPath.value = LatLng(19.422784, -99.175932)
            edtFinishPath.setText(txvCentroCulturalDigital.text)
        }

        imvBicycle.setOnClickListener {
            if(bicycleFlag.value == false){
                imvBicycle.setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.ic_white_bicycle))
                imvHuman.setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.ic_grey_human))
                bicycleFlag.value = true
            }
        }

        imvHuman.setOnClickListener {
            if(bicycleFlag.value == true){
                imvBicycle.setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.ic_grey_bicycle))
                imvHuman.setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.ic_white_human))
                bicycleFlag.value = false
            }
        }

    }

    fun showSavedRoutes() {
        consLaySavedPaths.visibility = View.VISIBLE
    }

    fun hideSavedRoutes() {
        consLaySavedPaths.visibility = View.GONE
    }

    fun clear() {
        edtStartPath.setText("")
        edtFinishPath.setText("")
    }

    interface BackListener{
        fun onBackFragmentClick()
    }

}