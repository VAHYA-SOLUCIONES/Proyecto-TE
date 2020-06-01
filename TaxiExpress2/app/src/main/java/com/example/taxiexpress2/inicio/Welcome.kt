package com.example.taxiexpress2.inicio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.taxiexpress2.Menus
import com.example.taxiexpress2.R
import kotlinx.android.synthetic.main.activity_welcome.*

class Welcome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        /**Activities**/
        //val pedidos = Intent(   this,Ruta::class.java)
        val pedidos = Intent(this, Menus::class.java)
        //AlfilName.text = name
        Handler().postDelayed({
            startActivity(pedidos)//start Activity
            finish()//Cierra la Actividad
        },2500)
    }
}
