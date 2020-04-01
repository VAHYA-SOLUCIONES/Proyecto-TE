package com.example.taxiexpress2

import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.sql.ClientInfoStatus

class MapaViajes : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    //Propiedades
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1 //nos va a decir del permiso?
    }

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa_viajes)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        //Para la localización del usuario
        //así obtenemos al cliente
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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
        // Add a marker in Sydney and move the camera
        /*val sydney = LatLng(-34.0, 151.0)}
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
        mMap.setOnMarkerClickListener(this)
        mMap.uiSettings.isZoomControlsEnabled = true

        setUpMap()//Runtime Permissions - permisos de tiempo de ejecución
    }
    private fun placeMarker(location: LatLng){ //Marcador
        val markerOptions = MarkerOptions().position(location)
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))//naranja

        mMap.addMarker(markerOptions) //Posiciona marcador en donde se pasa el parámetro
        //experimento
        //val Eli = LatLng(21.8945755, -102.25574)
        val Nissan = LatLng(21.7386577, -102.2803625)
        //mMap.addMarker(MarkerOptions().position(Eli).title("Baka"))
        mMap.addMarker(MarkerOptions().position(Nissan).title("Nissan 2"))
        //experimento
    }
    private fun setUpMap() {//checa que tengamos los permisos, sino lo pide
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true //Esta es mi localización
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL//tipo de mapa?

        fusedLocationClient.lastLocation.addOnSuccessListener(this){
                location -> //esta llamada va a "llamar" cuando tenga la localización
            if(location != null){
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                placeMarker(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 11f))
                //Valores de 0 a 20, 0 = sin Zoom, 20 = lo más cerca
            }
        }
    }
    override fun onMarkerClick(p0: Marker?) = false
}
