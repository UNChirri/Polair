package com.polairapp.polair

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_predict_results.*
import bolts.Task.delay
import java.util.Timer
import kotlin.concurrent.schedule


class PredictResultsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var heatMapTool: HeatMapUtils

    private var infoTouched : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predict_results)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        initButtonsListeners()
        predict_params.text = getString(R.string.prediction_params).format(
            intent.getStringExtra("gas"),
            intent.getStringExtra("time")
        )
    }

    private fun initButtonsListeners() {
        btn_back.setOnClickListener {
            val intent = Intent(this, PredictActivity::class.java)
            startActivity(intent)
            finish()
        }

        info.setOnClickListener {
            val barsFragment = BarsFragment()
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_placeholder,
                barsFragment
            ).commit()
            Timer("SettingUp", false).schedule(5000) {
                supportFragmentManager.beginTransaction().apply {
                    remove(barsFragment)
                    commit()
                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val sydney = LatLng(19.423977, -99.185688)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15f))

        //Experimental call - HeatMap
        heatMapTool = HeatMapUtils(mMap)
        heatMapTool.addHeatMap()
    }
}