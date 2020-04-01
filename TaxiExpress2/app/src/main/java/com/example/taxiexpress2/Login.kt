package com.example.taxiexpress2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.contracts.Returns

class Login : AppCompatActivity() {
    lateinit var username : String
    lateinit var password : String
    private var mIsShowPass = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        /**Activities**/
        editTextUser!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PERSON_NAME //Para cuando no recibe nulos

        val bienvenida = Intent(this,Welcome::class.java)

        btnIngresar.setOnClickListener {
            username = editTextUser?.text.toString()
            password = editTextContra?.text.toString()

            if (username == "Juan" && password == "1234"){
                bienvenida.putExtra("Nombre",username)//Envio de datos al siguiente Activity mediante Intent=bienvenida
                startActivity(bienvenida) //start Activity
                finish()//Cierra la Actividad
            }
            else Toast.makeText(this,"Usuario o contraseña incorrecto",Toast.LENGTH_SHORT).show()

            bienvenida.putExtra("Nombre",username)//Envio de datos al siguiente Activity mediante Intent=bienvenida
            //startActivity(bienvenida) //start Activity
            //finish()//Cierra la Actividad
        }
        //para mostrar/esconder el password
        showcontra.setOnClickListener{
            mIsShowPass = !mIsShowPass
            showPassword(mIsShowPass)
        }
        showPassword(mIsShowPass)
    }
    private fun showPassword(isShow: Boolean){
        if (isShow){ //Para mostrar el password
            editTextContra.transformationMethod = HideReturnsTransformationMethod.getInstance()
            showcontra.setImageResource(R.drawable.ic_hide_password_24dp)
        } else { //Para esconder el password
            editTextContra.transformationMethod = PasswordTransformationMethod.getInstance()
            showcontra.setImageResource(R.drawable.ic_show_password_24dp)
        }
        // Para poner el puntero al final del string del passsword
        editTextContra.setSelection(editTextContra.text.toString().length)
    }
}
