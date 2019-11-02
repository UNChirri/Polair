package com.polairapp.polair

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_maps.*
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, SelectPathFragment.BackListener {

    private lateinit var mMap: GoogleMap
    private val selectPathFragment = SelectPathFragment()
    private var startMarket: MarkerOptions?=null
    private var finishMarker: MarkerOptions?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        initButtonsListeners()
        initObservers()
    }

    private fun initObservers() {
        selectPathFragment.startPath.observe(this, Observer {
            hideSoftKeyboard()
            startPath(it)
        })
        selectPathFragment.finisPath.observe(this, Observer {
            hideSoftKeyboard()
            finishPath(it)
        })
    }

    private fun finishPath(latLng: LatLng) {
        finishMarker = MarkerOptions().apply {
            position(latLng)
            icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_endpoint))
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16f))
        mMap.addMarker(finishMarker)
    }

    fun startPath(latLng: LatLng) {
        startMarket = MarkerOptions().apply {
            position(latLng)
            icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bike_color))
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,18f))
        mMap.addMarker(startMarket)

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

        imvSOS.setOnClickListener {
            drawPath()
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

    fun drawPath(){

        onBackPressed()

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(19.415077, -99.188688),14f))

        mMap.addPolyline(
            PolylineOptions().apply {
                add(startMarket?.position,
                    LatLng(19.420971, -99.199891),
                    LatLng(19.420074, -99.197279),
                    LatLng(19.418565, -99.193715),
                    LatLng(19.420941, -99.192623),
                    LatLng(19.421442, -99.190165),
                    LatLng(19.421112, -99.189558),
                    LatLng(19.421356, -99.188253),
                    LatLng(19.421487, -99.184251),
                    LatLng(19.422185, -99.181633),
                    LatLng(19.421952, -99.180217),
                    LatLng(19.421062, -99.179251),
                    LatLng(19.421502, -99.178586),
                    LatLng(19.421730, -99.178768),
                    LatLng(19.422557, -99.177123),
                    finishMarker?.position)
                width(8f)
                color(Color.BLUE)
                geodesic(true)
            }
        )


    }

    private fun showPath() {
        replaceFragment(selectPathFragment)
        imvBurguerMenu.visibility = View.GONE
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
        hidePath()
    }

    private fun hidePath(){
        imvDropdown.visibility = View.GONE
        imvBurguerMenu.visibility = View.VISIBLE
        imvTitle.visibility = View.VISIBLE
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

        val ciudadMexico = LatLng(19.423977, -99.185688)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ciudadMexico,13f))
    }

    fun hideSoftKeyboard() {
        val inputMethodManager = getSystemService(
            Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            currentFocus?.windowToken, 0)
    }

    companion object{
        const val START_PATH = 1
        const val FINISH_PATH = 2
    }
}
