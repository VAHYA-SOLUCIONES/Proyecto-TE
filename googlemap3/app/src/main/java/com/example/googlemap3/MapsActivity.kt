package com.example.googlemap3

import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebStorage

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception

class MapsActivity : AppCompatActivity() {

    lateinit var mapFragment : SupportMapFragment
    lateinit var mMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            mMap = it
            mMap.isMyLocationEnabled = true

            val Eli = LatLng(21.8945755, -102.25574)
            val Nissan = LatLng(21.7386577, -102.2803625)
            mMap.addMarker(MarkerOptions().position(Eli).title("Eli"))
            mMap.addMarker(MarkerOptions().position(Nissan).title("Nissan 2"))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Eli,11f))
            //
            val URL = getDirectionURL(Eli,Nissan)
            GetDirection(URL).execute()
        })
    }
    fun getDirectionURL(origin:LatLng,dest:LatLng) : String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}&destination=${dest.latitude},${dest.longitude}&sensor=false&mode=driving"
    }

    private inner class GetDirection(val url : String) : AsyncTask<Void,Void,List<List<LatLng>>>(){
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()
            val result = ArrayList<List<LatLng>>()
            try {
                val respObj = Gson().fromJson(data,GoogleMapDTO::class.java)
                val path = ArrayList<LatLng>()
                for(i in 0..(respObj.routes[0].legs[0].steps.size-1)){
                    val startLatLng = LatLng(respObj.routes[0].legs[0].steps[i].start_location.lat.toDouble()
                        ,respObj.routes[0].legs[0].steps[i].start_location.lng.toDouble())
                    path.add(startLatLng)
                    val endLatLng = LatLng(respObj.routes[0].legs[0].steps[i].end_location.lat.toDouble()
                        ,respObj.routes[0].legs[0].steps[i].end_location.lng.toDouble())
                    path.add(endLatLng)
                }
                result.add(path)
            }catch (e:Exception){
                e.printStackTrace() //Imprimir mensaje de Excepción
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for(i in result.indices){
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.BLUE)
                lineoption.geodesic(true)
            }
            mMap.addPolyline(lineoption)
        }
    }
}
