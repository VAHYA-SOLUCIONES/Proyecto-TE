package com.example.taxiexpress2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_ruta.*

class Ruta : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ruta)

        /**Activities**/
        val mapeo = Intent(this,MapaViajes::class.java)
        val acu = Intent(this,Acumulado::class.java)
        btnMenu.setOnClickListener {
            //Toast.makeText(this,"Botón de Menú",Toast.LENGTH_SHORT).show()
            startActivity(acu)
        }
        btnMapa1.setOnClickListener {
            startActivity(mapeo)
        }
        btnAceptar.setOnClickListener {
            Toast.makeText(this,"Aceptar Viaje",Toast.LENGTH_SHORT).show()
        }
        btnRechazar.setOnClickListener {
            Toast.makeText(this,"Rechazar Viaje",Toast.LENGTH_SHORT).show()
        }
    }
}
