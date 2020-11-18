package com.example.driverregister

import android.app.Activity
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.android.core.location.LocationEnginePriority
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.activity_asignar_viajes.*
import kotlinx.android.synthetic.main.activity_aux_map1.*

/** Este programa no tiene la "navigation API" **/
class auxMap1 : AppCompatActivity(), PermissionsListener, LocationEngineListener,
    OnMapReadyCallback, MapboxMap.OnMapClickListener {

    //1
    val REQUEST_CHECK_SETTINGS = 1
    var settingsClient: SettingsClient? = null

    //2
    lateinit var map: MapboxMap
    lateinit var permissionManager: PermissionsManager
    var originLocation: Location? = null

    var locationEngine: LocationEngine? = null
    var locationComponent: LocationComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, "pk.eyJ1IjoidmF5aGEiLCJhIjoiY2tmbHFvcmttMThnMjJxbWpsbXJoYnNlZyJ9.IGNHQxU_pas6jkuTy2zM9w")
        setContentView(R.layout.activity_aux_map1)
        mapbox.onCreate(savedInstanceState)
        mapbox.getMapAsync(this)
        settingsClient = LocationServices.getSettingsClient(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                enableLocation()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                finish()
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    override fun onStart() {
        super.onStart()
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            locationEngine?.requestLocationUpdates()
            locationComponent?.onStart()
        }
        mapbox.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapbox.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapbox.onPause()
    }

    override fun onStop() {
        super.onStop()
        locationEngine?.removeLocationUpdates()
        locationComponent?.onStop()
        mapbox.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationEngine?.deactivate()
        mapbox.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapbox.onLowMemory()
    }

    /*override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (outState != null) {
            mapbox.onSaveInstanceState(outState)
        }
    }*/

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(this, "Esta aplicación necesita permiso para mostrar tu localización en el mapa",
            Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocation()
        } else {
            Toast.makeText(this, "Ubicación del usuario no garantizada", Toast.LENGTH_LONG).show()
            finish()
        }
    }
    @SuppressWarnings("MissingPermission")
    override fun onConnected() {
        locationEngine?.requestLocationUpdates()
    }

    override fun onLocationChanged(location: Location?) {
        location?.run {
            originLocation = this
            setCameraPosition(this)
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap?) {
        //1
        map = mapboxMap ?: return
        //2
        val locationRequestBuilder = LocationSettingsRequest.Builder().addLocationRequest(
            LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY))
        //3
        val locationRequest = locationRequestBuilder?.build()

        settingsClient?.checkLocationSettings(locationRequest)?.run {
            addOnSuccessListener {
                enableLocation()
            }

            addOnFailureListener {
                val statusCode = (it as ApiException).statusCode

                if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    val resolvableException = it as? ResolvableApiException
                    resolvableException?.startResolutionForResult(this@auxMap1, REQUEST_CHECK_SETTINGS)
                }
            }
        }
    }
    /** Funciones **/
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    //1
    fun enableLocation() {
        map.addOnMapClickListener(this) // no irá antes?
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            initializeLocationComponent()
            initializeLocationEngine()
            //
        } else {
            permissionManager = PermissionsManager(this)
            permissionManager.requestLocationPermissions(this)
        }
    }
    //2
    @SuppressWarnings("MissingPermission")
    fun initializeLocationEngine() {
        locationEngine = LocationEngineProvider(this).obtainBestLocationEngineAvailable()
        locationEngine?.priority = LocationEnginePriority.HIGH_ACCURACY
        locationEngine?.activate()
        locationEngine?.addLocationEngineListener(this)

        val lastLocation = locationEngine?.lastLocation
        if (lastLocation != null) {
            originLocation = lastLocation
            setCameraPosition(lastLocation)
        } else {
            locationEngine?.addLocationEngineListener(this)
        }
    }

    @SuppressWarnings("MissingPermission")
    fun initializeLocationComponent() {
        locationComponent = map.locationComponent
        locationComponent?.activateLocationComponent(this)
        locationComponent?.isLocationComponentEnabled = true
        locationComponent?.cameraMode = CameraMode.TRACKING
    }

    //3
    fun setCameraPosition(location: Location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude,
        location.longitude), 30.0))
    }

    override fun onMapClick(point: LatLng) {
        if(!map.markers.isEmpty()){
            map.clear()
        }
        map.addMarker(MarkerOptions().setTitle("Estuve aquí!").setSnippet("Descripción de lo que debería salir aquí").position(point))
        //map.addMarker(MarkerOptions().setTitle("Estuve aquí!").setSnippet("Descripción de lo que debería salir aquí").position(LatLng(-102.25574, 21.8945755))) //Experimental

        var coordenada = Point.fromLngLat(point.longitude, point.latitude)
        Log.d("mapax", "Latitud: ${point.latitude}")
        Log.d("mapax", "Longitud: ${point.longitude}")
        Log.d("mapax", "Coordenadas: ${point}")
        Log.d("mapax", "Coordenadas coordinates: ${coordenada.coordinates()}")
        Log.d("mapax", "Coordenadas altitud: ${coordenada.altitude()}")

        val intent = Intent(this@auxMap1, AsignarViajes::class.java)
        var dato = coordenada.coordinates().toString()
        var b : Bundle = Bundle()
        b.putString("dt", dato)
        intent.putExtras(b)
        Handler().postDelayed({
            startActivity(intent)
        },2000)
    }
}