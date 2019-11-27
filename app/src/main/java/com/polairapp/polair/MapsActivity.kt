package com.polairapp.polair

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.json.JSONArray
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, SelectPathFragment.BackListener {

    private lateinit var mMap: GoogleMap
    private val selectPathFragment = SelectPathFragment()
    private var startMarker: Marker?=null
    private var finishMarker: Marker?=null
    private val screenStates = MutableLiveData<ScreenStates>()
    private lateinit var path: PolylineOptions
    private lateinit var heatMapTool: HeatMapUtils
    private var areWorkshopsActive: Boolean = false
    private lateinit var workshopMarkers: ArrayList<Marker>
    private var bycycleSeleceted = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        initButtonsListeners()
        initObservers()
        path = PolylineOptions().apply {
            add(LatLng(19.421086, -99.199854),
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
                LatLng(19.422784, -99.175932),
                LatLng(19.422784, -99.175932))
            width(8f)
            color(getColor(R.color.blue_line))
            geodesic(true)
        }
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
        selectPathFragment.bicycleFlag.observe(this, Observer {
            bycycleSeleceted = it
        })
        screenStates.observe(this, Observer { screenStates ->
            when(screenStates){
                is ScreenStates.MainMap -> showMainMap()
                is ScreenStates.LetsGo -> showLetsGoFragment()
                is ScreenStates.PathTraced -> showPath()
            }
        })
    }

    private fun finishPath(latLng: LatLng) {
        finishMarker =  mMap.addMarker(MarkerOptions().apply {
            position(latLng)
            icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_endpoint))
        })
    }

    fun startPath(latLng: LatLng) {
        startMarker?.let {
            it.remove()
        }
        startMarker = if(bycycleSeleceted){
            mMap.addMarker(MarkerOptions().apply {
                position(latLng)
                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bike_color))
                flat(true)
            })
        }else{
            mMap.addMarker(MarkerOptions().apply {
                position(latLng)
                icon(BitmapDescriptorFactory.fromResource(R.drawable.woman_running))
                flat(true)
            })
        }
    }

    @Throws(JSONException::class)
    private fun readItems(resource: Int): HashMap<String, LatLng> {
        val markerMap : HashMap<String, LatLng> = HashMap()
        val inputStream = resources.openRawResource(resource)
        val json = Scanner(inputStream).useDelimiter("\\A").next()
        val array = JSONArray(json)
        for (i in 0 until array.length()) {
            val `object` = array.getJSONObject(i)
            val lat = `object`.getDouble("lat")
            val lng = `object`.getDouble("lng")
            markerMap.put(`object`.getString("title"), LatLng(lat, lng))
        }
        return markerMap
    }

    private fun placeMarkers() {
        val workshops = readItems(R.raw.workshops)
        workshopMarkers = ArrayList()
        workshops.forEach { (title, point) ->
            val workshopMarker = mMap.addMarker(MarkerOptions().apply {
                position(point)
                title(title)
                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_tools))
                flat(true)
            })
            workshopMarkers.add(workshopMarker)
        }
    }

    private fun removeMarkers() {
        workshopMarkers.forEach {
            it.remove()
        }
    }

    private fun initButtonsListeners() {
        btnWorkshop.setOnClickListener {
            areWorkshopsActive = !areWorkshopsActive
            if (areWorkshopsActive){
                placeMarkers()
                btnWorkshop.setBackgroundResource(R.drawable.ic_workshops_button_active)
            } else {
                removeMarkers()
                btnWorkshop.setBackgroundResource(R.drawable.ic_workshops_button)
            }

        }
        btnPrediction.setOnClickListener {
            val intent = Intent(this, PredictActivity::class.java)
            startActivity(intent)
        }
        btnLetsGo.setOnClickListener {
            if(screenStates.value == ScreenStates.LetsGo)
                screenStates.value = ScreenStates.PathTraced
            else
                screenStates.value = ScreenStates.LetsGo
        }

        imvDropdown.setOnTouchListener(View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    selectPathFragment.showSavedRoutes()
                    imvDropdown.visibility = View.INVISIBLE
                    return@OnTouchListener true
                }
                MotionEvent.ACTION_UP ->{
                    imvDropdown.visibility = View.VISIBLE
                    selectPathFragment.hideSavedRoutes()
                    return@OnTouchListener true
                }
            }
            false
        })

        imvBackBlue.setOnClickListener {
            onBackPressed()
        }

        imvFinishJourney.setOnClickListener {
            startActivity(Intent(this, FinishJourneyActivity::class.java))
            finish()
        }
    }

    private fun replaceFragment(replace: Boolean) {
        if (replace){
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameStartPath, selectPathFragment, selectPathFragment.tag)
                commit()
            }
        }else{
            supportFragmentManager.beginTransaction().apply {
                remove(selectPathFragment)
                commit()
            }
        }
    }

    override fun onBackFragmentClick() {
        onBackPressed()
    }

    override fun onBackPressed() {
        when(screenStates.value){
            is ScreenStates.MainMap -> finish()
            is ScreenStates.LetsGo -> screenStates.value = ScreenStates.MainMap
            is ScreenStates.PathTraced -> screenStates.value = ScreenStates.LetsGo
        }
    }

    private fun showMainMap(){
        imvBurguerMenu.visibility = View.VISIBLE
        imvTitle.visibility = View.VISIBLE
        consLayButtonsWithoutGo.visibility = View.VISIBLE
        btnPrediction.visibility = View.VISIBLE
        btnLocation.visibility = View.GONE
        imvBackBlue.visibility = View.GONE
        imvFinishJourney.visibility = View.GONE
        imvDropdown.visibility = View.GONE
        replaceFragment(false)
    }

    private fun showLetsGoFragment() {
        imvDropdown.visibility = View.VISIBLE
        btnLocation.visibility = View.VISIBLE
        btnLetsGo.visibility = View.VISIBLE
        imvBackBlue.visibility = View.GONE
        imvFinishJourney.visibility = View.GONE
        imvBurguerMenu.visibility = View.GONE
        imvTitle.visibility = View.GONE
        consLayButtonsWithoutGo.visibility = View.GONE
        btnCamera.visibility = View.GONE
        replaceFragment(true)
        mMap.clear()
    }

    private fun showPath() {
        imvBackBlue.visibility = View.VISIBLE
        imvFinishJourney.visibility = View.VISIBLE
        btnCamera.visibility = View.VISIBLE
        consLayButtonsWithoutGo.visibility = View.VISIBLE
        imvDropdown.visibility = View.GONE
        btnLetsGo.visibility = View.GONE
        btnLocation.visibility = View.GONE
        btnPrediction.visibility = View.GONE
        drawPath()
        replaceFragment(false)
        heatMapTool.addHeatMap()
    }

    private fun drawPath(){
        mMap.addPolyline(path)
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
        mMap.uiSettings.isCompassEnabled = false

        val ciudadMexico = LatLng(19.423977, -99.185688)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ciudadMexico,14f))

        heatMapTool = HeatMapUtils(mMap)
        heatMapTool.addHeatMap()
        mMap.uiSettings.isMapToolbarEnabled = false

    }

    fun hideSoftKeyboard() {
        val inputMethodManager = getSystemService(
            Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            currentFocus?.windowToken, 0)
    }

}
