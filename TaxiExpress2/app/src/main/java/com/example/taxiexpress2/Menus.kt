package com.example.taxiexpress2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.taxiexpress2.inicio.MainActivity
import com.example.taxiexpress2.inicio.NextTravel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.nav_header_menus.*

class Menus : AppCompatActivity() {
    /** Este es el menú lateral! **/
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menus)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        /** Experimental **/
        val fromId = FirebaseAuth.getInstance().uid
        val alfilex = FirebaseFirestore.getInstance()
        val alfilData = alfilex.collection("Alfil").document(fromId.toString())
        alfilData.get().addOnCompleteListener {
            //AlfilName.text = it.result?.get("Nombre").toString()
            textViewUserNameNav.text = it.result?.get("Nombre").toString()
            textViewPlacasNav.text = "Placas: "+it.result?.get("Placas").toString()
            //Foto
            val ref = it.result?.get("foto").toString() //dirección de la foto
            Picasso.get().load(ref).into(header_alfil_image)
        }
        /** Experimental **/

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /** Experimental **/
        // en esta parte se están listando las opciones del menú para trabajar
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_soporte, R.id.nav_ayuda
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {//ese es el ID del recurso del menú
            R.id.action_settings -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)//agregar al Taxi Express code
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    /** Experimental **/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menus, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
