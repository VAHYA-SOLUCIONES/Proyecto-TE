package com.example.taxiexpress2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_acumulado.*

class Acumulado : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acumulado)

        val rutas = Intent(this,Ruta::class.java)

        btnBack.setOnClickListener {
            startActivity(rutas)
        }
    }
}
