package com.example.taxiexpress2.inicio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.widget.Toast
import com.example.taxiexpress2.Menus
import com.example.taxiexpress2.R
import com.example.taxiexpress2.models.Alfil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_welcome.*

class Welcome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        /** Experimental **/
        val fromId = FirebaseAuth.getInstance().uid
        val alfilex = FirebaseFirestore.getInstance()
        val alfilData = alfilex.collection("Alfil").document(fromId.toString())
        //Log.d("Checando", "Welcome alfilData: ${alfilData}")
        alfilData.get().addOnCompleteListener {
            AlfilName.text = it.result?.get("Nombre").toString()
        }
        val pedidos = Intent(this, Menus::class.java)
        Handler().postDelayed({
            startActivity(pedidos)
            finish()
        },2500)
    }
}
