package com.polairapp.polair

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.polairapp.polair.MapsActivity.Companion.FINISH_PATH
import com.polairapp.polair.MapsActivity.Companion.START_PATH
import kotlinx.android.synthetic.main.fragment_select_path.*

class SelectPathFragment: Fragment() {

    lateinit var mContext: BackListener
    var startPath = MutableLiveData<LatLng>()
    var finisPath = MutableLiveData<LatLng>()

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

        edtStartPath.setOnFocusChangeListener{v,bool ->
            linLayStartPathHistory.visibility = if(bool) View.VISIBLE
            else View.GONE
        }

        edtFinishPath.setOnFocusChangeListener{v,bool ->
            linLayFinishPathHistory.visibility = if(bool) View.VISIBLE
            else View.GONE
        }
        txvNavalClub.setOnClickListener {
            linLayStartPathHistory.visibility = View.GONE
            startPath.value = LatLng(19.421086, -99.199854)
            edtStartPath.setText(txvNavalClub.text)
        }

        txvAngelaTheatre.setOnClickListener {
            linLayStartPathHistory.visibility = View.GONE
            startPath.value = LatLng(19.429074, -99.194224)
            edtStartPath.setText(txvAngelaTheatre.text)
        }

        txvCentroCulturalDigital.setOnClickListener {
            linLayFinishPathHistory.visibility = View.GONE
            finisPath.value = LatLng(19.422784, -99.175932)
            edtFinishPath.setText(txvCentroCulturalDigital.text)
        }
        txvPresstoParquePolanco.setOnClickListener {
            linLayFinishPathHistory.visibility = View.GONE
            finisPath.value = LatLng(19.440345, -99.184166)
            edtFinishPath.setText(txvPresstoParquePolanco.text)
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