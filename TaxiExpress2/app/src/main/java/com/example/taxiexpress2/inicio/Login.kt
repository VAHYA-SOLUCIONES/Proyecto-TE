package com.example.taxiexpress2.inicio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import com.example.taxiexpress2.R
import com.example.taxiexpress2.models.Alfil
import com.example.taxiexpress2.ui.gallery.GalleryFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    private var mIsShowPass = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /** Logeo con Firebase **/
        btnIngresar.setOnClickListener {
            performLogin()
        }
        /** Logeo con Firebase **/
        showcontra.setOnClickListener{
            mIsShowPass = !mIsShowPass
            showPassword(mIsShowPass)
        }
        showPassword(mIsShowPass)
    }

    private fun performLogin() {
        editTextAlfilEmail!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS //Para cuando no recibe nulos
        val email = editTextAlfilEmail?.text.toString() //Es email pero lo dejamos así
        val password = editTextAlfilContra?.text.toString()

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Porfavor llene los campos email/password", Toast.LENGTH_SHORT).show()
            return
        }
        //Firebase
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("Login", "Bienvenido: ${it.result?.user?.uid}")
                val ID_UID = it.result?.user?.uid.toString()
                // Para transferir el nombre del Alfil a la siguiente pantalla
                val alfilex = FirebaseFirestore.getInstance()
                val alfilData = alfilex.collection("Alfil").document(it.result?.user?.uid.toString())
                //Log.d("Checando", "Login alfilData: ${alfilData}")
                alfilData.get().addOnCompleteListener {
                    val intent = Intent(this, Welcome::class.java)
                    val perfil = Intent(this, GalleryFragment::class.java)
                    /** Experimental **/
                    //
                    /** Experimental **/
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)//Ve al centro de mensajes
                    //finish()//experimental
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Fallo al ingresar: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showPassword(isShow: Boolean){
        if (isShow){ //Para mostrar el password
            editTextAlfilContra.transformationMethod = HideReturnsTransformationMethod.getInstance()
            showcontra.setImageResource(R.drawable.ic_hide_password_24dp)
        } else { //Para esconder el password
            editTextAlfilContra.transformationMethod = PasswordTransformationMethod.getInstance()
            showcontra.setImageResource(R.drawable.ic_show_password_24dp)
        }
        // Para poner el puntero al final del string del passsword
        editTextAlfilContra.setSelection(editTextAlfilContra.text.toString().length)
    }
}
/*var usuario = Alfil(
                        ID_UID,
                        it.result?.get("Activo") as Boolean,
                        it.result?.get("Alfil #").toString(),
                        it.result?.get("Nombre").toString(),
                        it.result?.get("Apellido_Paterno").toString(),
                        it.result?.get("Apellido_Materno").toString(),
                        it.result?.get("Placas").toString(),
                        it.result?.get("Licencia").toString(),
                        it.result?.get("email").toString(),
                        it.result?.get("foto").toString(),
                        //Otros
                        0.0f,
                        0.0f,
                        0,
                        0,
                        0,
                        0,
                        0.0f
                    )
                    // Para transferir el nombre del Alfil a la siguiente pantalla
                    //var ID = it.result?.get("Nombre").toString()
                    val b : Bundle = Bundle()
                    //b.putString("ID", usuario.toString())
                    b.putParcelable("ID", usuario)
                    perfil.putExtras(b) //Experimental
                    intent.putExtras(b)*/