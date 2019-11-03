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
import com.google.android.gms.maps.model.*
import com.google.maps.android.heatmaps.Gradient
import com.google.maps.android.heatmaps.HeatmapTileProvider
import kotlinx.android.synthetic.main.activity_maps.*
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val selectPathFragment = SelectPathFragment()
    private var startMarket: MarkerOptions?=null
    private var finishMarker: MarkerOptions?=null
    private val screenStates = MutableLiveData<ScreenStates>()
    private val path = PolylineOptions().apply {
        add(LatLng(19.420971, -99.199891),
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
            LatLng(19.422784, -99.175932))
        width(8f)
        color(Color.BLUE)
        geodesic(true)
    }

    private val ALT_HEATMAP_GRADIENT_COLORS = intArrayOf(
        Color.argb(0, 0, 255, 255), // transparent
        Color.argb(255 / 3 * 2, 0, 255, 255),
        Color.rgb(0, 255, 255),
        Color.rgb(0, 255, 0),
        Color.rgb(255, 255, 0),
        Color.rgb(255, 0, 0)
    )

    private val ALT_HEATMAP_GRADIENT_START_POINTS = floatArrayOf(0.0f, 0.10f, 0.20f, 0.30f, 0.40f, 1.0f)

    private val ALT_HEATMAP_GRADIENT = Gradient(
        ALT_HEATMAP_GRADIENT_COLORS,
        ALT_HEATMAP_GRADIENT_START_POINTS
    )

    private val ALT_HEATMAP_GRADIENT_OPACITY = 0.3

    private val ALT_HEATMAP_GRADIENT_RADIUS = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
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
        screenStates.observe(this, Observer { screenStates ->
            when(screenStates){
                is ScreenStates.MainMap -> showMainMap()
                is ScreenStates.LetsGo -> showLetsGoFragment()
                is ScreenStates.PathTraced -> showPath()
            }
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
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameStartPath, fragment, fragment.tag)
            commit()
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
        btnLocation.visibility = View.GONE
        imvBackBlue.visibility = View.GONE
        imvFinishJourney.visibility = View.GONE
        imvDropdown.visibility = View.GONE
        replaceFragment(Fragment())

    }

    private fun showLetsGoFragment() {
        mMap.clear()
        imvDropdown.visibility = View.VISIBLE
        btnLocation.visibility = View.VISIBLE
        btnLetsGo.visibility = View.VISIBLE
        imvBackBlue.visibility = View.GONE
        imvFinishJourney.visibility = View.GONE
        imvBurguerMenu.visibility = View.GONE
        imvTitle.visibility = View.GONE
        consLayButtonsWithoutGo.visibility = View.GONE
        btnCamera.visibility = View.GONE
        replaceFragment(selectPathFragment)
    }

    private fun showPath() {
        selectPathFragment.clear()
        imvBackBlue.visibility = View.VISIBLE
        imvFinishJourney.visibility = View.VISIBLE
        btnCamera.visibility = View.VISIBLE
        consLayButtonsWithoutGo.visibility = View.VISIBLE
        imvDropdown.visibility = View.GONE
        btnLetsGo.visibility = View.GONE
        btnLocation.visibility = View.GONE
        drawPath()
        replaceFragment(Fragment())
    }

    private fun drawPath(){

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(19.415077, -99.188688),14f))

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

        val ciudadMexico = LatLng(19.423977, -99.185688)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15f))

        addHeatMap()
    }

    private fun addHeatMap() {
        val defaultPoints : ArrayList<LatLng> = ArrayList()
        //Points, should be read from a json dataset or generated by a custom class
        defaultPoints.add(LatLng(19.423977, -99.182688))
        defaultPoints.add(LatLng(19.421927, -99.185618))
        defaultPoints.add(LatLng(19.429997, -99.187648))
        defaultPoints.add(LatLng(19.428997, -99.187648))
        //HeatmapTileProvider must be declared here in order to modify radius
        val mProvider: HeatmapTileProvider = HeatmapTileProvider.Builder().data(defaultPoints).build()
        //Setups
        mProvider.setRadius(ALT_HEATMAP_GRADIENT_RADIUS)
        mProvider.setGradient(ALT_HEATMAP_GRADIENT)
        mProvider.setOpacity(ALT_HEATMAP_GRADIENT_OPACITY)
        val mOverlay : TileOverlay = mMap.addTileOverlay(TileOverlayOptions().tileProvider(mProvider))
        mOverlay.clearTileCache()
    }

    fun hideSoftKeyboard() {
        val inputMethodManager = getSystemService(
            Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            currentFocus?.windowToken, 0)
    }
}
