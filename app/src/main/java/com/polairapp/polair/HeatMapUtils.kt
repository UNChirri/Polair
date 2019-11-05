package com.polairapp.polair

import android.graphics.Color
import com.google.maps.android.heatmaps.Gradient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.TileOverlay
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.maps.android.heatmaps.HeatmapTileProvider
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.round
import kotlin.math.roundToLong

class HeatMapUtils(mMap: GoogleMap) {

    private val _mMap = mMap

    private val defaultPoints : ArrayList<LatLng> = ArrayList()

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

    private val ALT_HEATMAP_GRADIENT_RADIUS = 80

    private val RANDOM_POINT_BASE_NUMBER = 100

    private val BASE_LAT_LNG : LatLng = LatLng(19.420977, -99.182688)

    private val NEAR_POINT_BASE_MIN = 10000
    private val NEAR_POINT_BASE_MAX = 20000
    private val FAR_POINT_BASE_MIN = 1000
    private val FAR_POINT_BASE_MAX = 2000

    fun addHeatMap() {
        populatePointsArray()
        //HeatmapTileProvider must be declared here in order to modify radius
        val mProvider: HeatmapTileProvider = HeatmapTileProvider.Builder().data(defaultPoints).build()
        //Setups
        mProvider.setRadius(ALT_HEATMAP_GRADIENT_RADIUS)
        mProvider.setGradient(ALT_HEATMAP_GRADIENT)
        mProvider.setOpacity(ALT_HEATMAP_GRADIENT_OPACITY)
        val mOverlay : TileOverlay = _mMap.addTileOverlay(TileOverlayOptions().tileProvider(mProvider))
        mOverlay.clearTileCache()
    }

    private fun round(value: Double, places: Int): Double {
        var bd = BigDecimal.valueOf(value)
        bd = bd.setScale(places, RoundingMode.HALF_UP)
        return bd.toDouble()
    }

    private fun populatePointsArray() {
        //Raw values based on BASE_LAT_LNG point
        val latDecPartRaw = BASE_LAT_LNG.latitude % 1
        val lngDecPartRaw = BASE_LAT_LNG.longitude % 1
        val latIntPart = BASE_LAT_LNG.latitude - (latDecPartRaw)
        val lngIntPart = BASE_LAT_LNG.longitude - (lngDecPartRaw)
        for (x in 0 until RANDOM_POINT_BASE_NUMBER) {
            when (getRandomNumber(0, 3).toInt()) {
                0 -> {
                    defaultPoints.add(
                        LatLng(
                            latIntPart + latDecPartRaw + getDecimalBase(1, 99, NEAR_POINT_BASE_MIN, NEAR_POINT_BASE_MAX),
                            lngIntPart + lngDecPartRaw + getDecimalBase(1, 99, NEAR_POINT_BASE_MIN, NEAR_POINT_BASE_MAX)
                        )
                    )
                    defaultPoints.add(
                        LatLng(
                            latIntPart + latDecPartRaw + getDecimalBase(1, 99, FAR_POINT_BASE_MIN, FAR_POINT_BASE_MAX),
                            lngIntPart + lngDecPartRaw + getDecimalBase(1, 99, FAR_POINT_BASE_MIN, FAR_POINT_BASE_MAX)
                        )
                    )
                }
                1 -> {
                    defaultPoints.add(
                        LatLng(
                            latIntPart + latDecPartRaw - getDecimalBase(1, 99, NEAR_POINT_BASE_MIN, NEAR_POINT_BASE_MAX),
                            lngIntPart + lngDecPartRaw + getDecimalBase(1, 99, NEAR_POINT_BASE_MIN, NEAR_POINT_BASE_MAX)
                        )
                    )
                    defaultPoints.add(
                        LatLng(
                            latIntPart + latDecPartRaw - getDecimalBase(1, 99, FAR_POINT_BASE_MIN, FAR_POINT_BASE_MAX),
                            lngIntPart + lngDecPartRaw + getDecimalBase(1, 99, FAR_POINT_BASE_MIN, FAR_POINT_BASE_MAX)
                        )
                    )
                }
                2 -> {
                    defaultPoints.add(
                        LatLng(
                            latIntPart + latDecPartRaw + getDecimalBase(1, 99, NEAR_POINT_BASE_MIN, NEAR_POINT_BASE_MAX),
                            lngIntPart + lngDecPartRaw - getDecimalBase(1, 99, NEAR_POINT_BASE_MIN, NEAR_POINT_BASE_MAX)
                        )
                    )
                    defaultPoints.add(
                        LatLng(
                            latIntPart + latDecPartRaw + getDecimalBase(1, 99, FAR_POINT_BASE_MIN, FAR_POINT_BASE_MAX),
                            lngIntPart + lngDecPartRaw - getDecimalBase(1, 99, FAR_POINT_BASE_MIN, FAR_POINT_BASE_MAX)
                        )
                    )
                }
                else -> {
                    defaultPoints.add(
                        LatLng(
                            latIntPart + latDecPartRaw - getDecimalBase(1, 99, NEAR_POINT_BASE_MIN, NEAR_POINT_BASE_MAX),
                            lngIntPart + lngDecPartRaw - getDecimalBase(1, 99, NEAR_POINT_BASE_MIN, NEAR_POINT_BASE_MAX)
                        )
                    )
                    defaultPoints.add(
                        LatLng(
                            latIntPart + latDecPartRaw - getDecimalBase(1, 99, FAR_POINT_BASE_MIN, FAR_POINT_BASE_MAX),
                            lngIntPart + lngDecPartRaw - getDecimalBase(1, 99, FAR_POINT_BASE_MIN, FAR_POINT_BASE_MAX)
                        )
                    )
                }
            }
        }
    }

    private fun getRandomNumber(min: Int, max: Int): Double {
        return ((Math.random() * (max - min + 1) ) + min)
    }

    private fun getDecimalBase(minNum: Int, maxNum: Int, minDen: Int, maxDen: Int): Double {
        return round((getRandomNumber(minNum, maxNum)/getRandomNumber(minDen,maxDen)),getRandomNumber(6,35).toInt())
    }

}