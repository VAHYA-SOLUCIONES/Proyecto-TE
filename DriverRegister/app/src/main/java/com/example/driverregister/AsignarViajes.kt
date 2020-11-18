package com.example.driverregister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_asignar_viajes.*

class AsignarViajes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asignar_viajes)
        /** Vamos a asignar nuevos viajes con esta app **/

        var bundle = intent.extras
        if (bundle != null) {
            textViewGeoPoint.text = bundle.getString("dt")
        }
        textViewGeoPoint.setOnClickListener {
            val intent = Intent(this, auxMap1::class.java)
            //finish()
            startActivity(intent)
        }
    }

    /** Menú de opciones **/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.driver_register -> {
                val intent = Intent(this, RegisterActivity::class.java)
                finish()
                startActivity(intent)
            }
            R.id.route_register -> {
                /*val intent = Intent(this, AsignarViajes::class.java)
                finish()
                startActivity(intent)*/
            }
            R.id.experimental_map -> {
                //
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    /** Menú de opciones **/
}