package com.example.taxiexpress2.inicio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.taxiexpress2.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /**Activities**/
        val logeo = Intent(this, Login::class.java)
        Handler().postDelayed({
            //start Activity
            startActivity(logeo)
            finish()//Cierra la Actividad
        },2000)
    }
}
