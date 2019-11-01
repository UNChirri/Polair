package com.polairapp.polair

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import android.view.MotionEvent
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class MapsActivity : AppCompatActivity(), OnMapReadyCallback, SelectPathFragment.BackListener {

    private lateinit var mMap: GoogleMap
    private val selectPathFragment = SelectPathFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        initButtonsListeners()
    }

    private fun initButtonsListeners() {
        btnWorkshop.setOnClickListener {
            mMap.addMarker(MarkerOptions().apply {
                position(LatLng(4.63819,-74.08623))
                title("Marker in Sydney")
                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_tools))
                flat(true)
            })
        }
        btnPrediction.setOnClickListener {
            val intent = Intent(this, PredictActivity::class.java)
            startActivity(intent)
            finish()
        }
        btnLetsGo.setOnClickListener {
            showPath()
        }

        imvDropdown.setOnTouchListener(View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    selectPathFragment.showSavedRoutes()
                    imvDropdown.visibility = View.INVISIBLE
                    return@OnTouchListener true
                } // if you want to handle the touch event
                MotionEvent.ACTION_UP ->{
                    imvDropdown.visibility = View.VISIBLE
                    selectPathFragment.hideSavedRoutes()
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    private fun showPath() {
        replaceFragment(selectPathFragment)
        imvBurguerMenu.visibility = View.VISIBLE
        imvDropdown.visibility = View.VISIBLE
        imvTitle.visibility = View.GONE

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameStartPath, fragment, fragment.tag).addToBackStack(fragment.tag)
            commit()
        }
    }

    override fun onBackFragmentClick() {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        imvDropdown.visibility = View.GONE
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

        val sydney = LatLng(4.63819,-74.08623)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15f))
    }
}
