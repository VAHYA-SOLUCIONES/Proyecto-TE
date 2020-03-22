package com.example.taxiexpress2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_welcome.*

class Welcome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val objetoBienvenida: Intent=intent
        var name: String = objetoBienvenida.getStringExtra("Nombre")//Pase de datos
        /**Activities**/
        val pedidos = Intent(this,Ruta::class.java)
        AlfilName.text = name
        Handler().postDelayed({
            startActivity(pedidos)//start Activity
            finish()//Cierra la Actividad
        },2500)
    }
}
