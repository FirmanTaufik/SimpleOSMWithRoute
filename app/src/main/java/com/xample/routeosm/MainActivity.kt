package com.xample.routeosm

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.xample.routeosm.retrofit.ApiClient
import com.xample.routeosm.retrofit.GetService
import kotlinx.android.synthetic.main.activity_main.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapController
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    lateinit var mapController: MapController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        val geoPoint = GeoPoint( -6.944825980216912, 106.95139998495478)
        addMarker("start",geoPoint)

        val end = GeoPoint(  -6.921954294090376, 106.92868422103138)
        addMarker("end",end)

        mapView.setMultiTouchControls(true)
        mapView.controller.animateTo(geoPoint)
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)

        mapController = mapView.controller as MapController
        mapController.setCenter(geoPoint)
        mapController.zoomTo(15)

        val routeType = "routed-bike" //routed-car //routed-foot //routed-bike
        val service = ApiClient().client.create(GetService::class.java)
        val startEndLatLng = "${geoPoint.longitude},${geoPoint.latitude};${end.longitude},${end.latitude}"
        service.getRoute(routeType,startEndLatLng).enqueue(object :Callback<RouteModel>{
            override fun onResponse(call: Call<RouteModel>, response: Response<RouteModel>) {
                if (response.isSuccessful){
                    val polyline = parseRoute( response.body())
                    runOnUiThread {
                        if (polyline != null) {
                            polyline.color = Color.RED
                            polyline.width = 5f
                            mapView.overlayManager.add(polyline)
                            mapView.invalidate()
                        }
                    }
                }

            }

            override fun onFailure(call: Call<RouteModel>, t: Throwable) {
                Log.i("Retrofit Log", "onFailure: ${t.message}")

            }
        })

    }

    private fun addMarker(info :String, geoPoint: GeoPoint) {
        val marker = Marker(mapView)
        marker.icon = resources.getDrawable(R.drawable.ic_place)
        marker.position = geoPoint
        marker.title = info
        mapView.overlays.add(marker)
        mapView.invalidate()
    }

    private fun parseRoute(responseBody: RouteModel?): Polyline? {
        if (responseBody == null) return null
        val geometry = responseBody.routes?.getOrNull(0)?.geometry
        return decodePolyline(geometry)
    }


    private fun decodePolyline(polyline: String?): Polyline? {
        if (polyline == null) return null

        val decodedPoints = mutableListOf<GeoPoint>()
        var index = 0
        var lat = 0
        var lon = 0

        while (index < polyline.length) {
            var shift = 0
            var result = 0
            var byte: Int

            do {
                byte = polyline[index++].toInt() - 63
                result = result or ((byte and 0x1F) shl shift)
                shift += 5
            } while (byte >= 0x20)

            val dlat = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
            lat += dlat

            shift = 0
            result = 0

            do {
                byte = polyline[index++].toInt() - 63
                result = result or ((byte and 0x1F) shl shift)
                shift += 5
            } while (byte >= 0x20)

            val dlon = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
            lon += dlon

            val decodedLat = lat.toDouble() / 1E5
            val decodedLon = lon.toDouble() / 1E5

            decodedPoints.add(GeoPoint(decodedLat, decodedLon))
        }

        val polylineOverlay = Polyline()
        polylineOverlay.setPoints(decodedPoints)
        return polylineOverlay
    }

    public override fun onResume() {
        super.onResume()
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        if (mapView != null) {
            mapView.onResume()
        }
    }

    public override fun onPause() {
        super.onPause()
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        if (mapView != null) {
            mapView.onPause()
        }
    }

}