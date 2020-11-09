package com.example.taxiexpress2.maps

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.example.taxiexpress2.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.android.core.location.LocationEnginePriority
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import kotlinx.android.synthetic.main.activity_mapbox_map.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapboxMap : AppCompatActivity(), PermissionsListener, LocationEngineListener,
    OnMapReadyCallback, MapboxMap.OnMapClickListener {
    /** Variables requeridas **/
    //1
    val REQUEST_CHECK_SETTINGS = 1
    var settingsClient: SettingsClient? = null

    //2
    lateinit var map: MapboxMap
    lateinit var permissionManager: PermissionsManager
    var originLocation: Location? = null // Esta es mi ubicación en el mapa
    // Agregadas por mi
    var destino: Location? = null // Ubicación fin de ruta!!!
    var partida: Location? = null // Ubicación para empezar la ruta!!

    var locationEngine: LocationEngine? = null
    var locationComponent: LocationComponent? = null
    /** Variables requeridas **/
    /** Direccionar usuario entre ubicaciones **/
    var navigationMapRoute: NavigationMapRoute? = null
    var currentRoute: DirectionsRoute? = null
    /** Direccionar usuario entre ubicaciones **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, "pk.eyJ1IjoidmF5aGEiLCJhIjoiY2tmbHFvcmttMThnMjJxbWpsbXJoYnNlZyJ9.IGNHQxU_pas6jkuTy2zM9w")
        setContentView(R.layout.activity_mapbox_map)
        mapbox.onCreate(savedInstanceState)
        mapbox.getMapAsync(this)
        settingsClient = LocationServices.getSettingsClient(this)
        /** Esto es para activar la navegación !! **/
        btnNavigate.isEnabled = false

        btnNavigate.setOnClickListener {
            val navigationLauncherOptions = NavigationLauncherOptions.builder() //1 ruta de navegación
                .directionsRoute(currentRoute) //2 la ruta
                .shouldSimulateRoute(true) //3 simulación vuelta por vuelta
                .build()
            // abajo: contexto y "opciones"
            NavigationLauncher.startNavigation(this, navigationLauncherOptions) //4
        }
        /** Esto es para activar la navegación !! **/
        // Debría llamar aquí lo de la ruta -> ...
        // Delay para activar la ruta
        Handler().postDelayed({
            trazarRuta() //Experimental
        },500)
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
    //
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
    ///////////////////////////////////////////////////////////////////////////////////
    //1
    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(this, "Esta aplicación necesita permiso para mostrar tu localización en el mapa",
            Toast.LENGTH_LONG).show()
    }
    //2
    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocation()
        } else {
            Toast.makeText(this, "Ubicación del usuario no garantizada", Toast.LENGTH_LONG).show()
            finish()
        }
    }
    //4
    @SuppressWarnings("MissingPermission")
    override fun onConnected() {
        locationEngine?.requestLocationUpdates()
    }
    //3
    override fun onLocationChanged(location: Location?) {
        location?.run {
            originLocation = this
            setCameraPosition(this)
        }
    }
    //5
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
                    resolvableException?.startResolutionForResult(this@MapboxMap, REQUEST_CHECK_SETTINGS)
                }
            }
        }
    }
    //6
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    /** Life cycle  **/
    //1
    fun enableLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            initializeLocationComponent()
            initializeLocationEngine()
            map.addOnMapClickListener(this)
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
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(location.latitude,
            location.longitude), 10.0))
    }

    override fun onMapClick(point: LatLng) {
        /*if(!map.markers.isEmpty()){
            map.clear()
        }
        map.addMarker(MarkerOptions().setTitle("Estuve aquí!").setSnippet("Descripción de lo que debería salir aquí").position(point))
        checkLocation()
        originLocation?.run {
            // Acaso será esto lo que busco?
            val startPoint = Point.fromLngLat(longitude, latitude)
            //val endPoint = Point.fromLngLat(point.longitude, point.latitude)
            val endPoint = Point.fromLngLat(-102.25574, 21.8945755)
            //val endPoint = Point.fromLngLat(21.8945755, -102.25574)
            //Log.d("Mapeo", endPoint.toString())

            getRoute(startPoint, endPoint)
        }*/
    }
    @SuppressLint("MissingPermission")
    private fun checkLocation() {
        if (originLocation == null) {
            map.locationComponent.lastKnownLocation?.run {
                originLocation = this
            }
        }
    }
    /** Life cycle  **/
    //fun trazarRuta(point: LatLng) {
    fun trazarRuta() {
        if(!map.markers.isEmpty()){
            map.clear()
        }
        //map.addMarker(MarkerOptions().position(point)) // No lo necesitamos
        //map.addMarker(MarkerOptions().setTitle("Estuve aquí!").setSnippet("Descripción de lo que debería salir aquí").position(point))
        checkLocation()
        originLocation?.run {
            // Acaso será esto lo que busco?
            val startPoint = Point.fromLngLat(longitude, latitude)
            //val endPoint = Point.fromLngLat(point.longitude, point.latitude)
            val endPoint = Point.fromLngLat(-102.25574, 21.8945755)
            //map.addMarker(MarkerOptions().setTitle("Estuve aquí!").setSnippet("Descripción de lo que debería salir aquí").position(point))
            //Log.d("Mapeo", endPoint.toString())
            getRoute(startPoint, endPoint)
        }
    }
    /** Función para Mapbox Navigation API **/
    fun getRoute(originPoint: Point, endPoint: Point) {
        /** EXPERIMENTAL **/
        //val
        /** EXPERIMENTAL **/
        NavigationRoute.builder(this) //1
            .accessToken(Mapbox.getAccessToken()!!) //2
            .origin(originPoint) //3
            .destination(endPoint) //4
            .build() //5
            .getRoute(object : Callback<DirectionsResponse> { //6
                override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                    Log.d("MapboxMap", t.localizedMessage)
                }

                override fun onResponse(call: Call<DirectionsResponse>,
                                        response: Response<DirectionsResponse>
                ) {
                    if (navigationMapRoute != null) {
                        navigationMapRoute?.updateRouteVisibilityTo(false)
                    } else {
                        navigationMapRoute = NavigationMapRoute(null, mapbox, map)
                    }

                    currentRoute = response.body()?.routes()?.first()
                    if (currentRoute != null) {
                        navigationMapRoute?.addRoute(currentRoute)
                    }
                    btnNavigate.isEnabled = true
                }
            })
    }
    /** Función para Mapbox Navigation API **/
}