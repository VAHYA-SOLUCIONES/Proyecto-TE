package com.example.driverregister

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    var selectedPhotoUri: Uri? = null //Variable de "foto"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Registrar usuario
        btn_registrar.setOnClickListener {
            registrarUsuario()
            //uploadAlfilDataToFirebaseFirestore("Gogdanov Bone", "test")
        }
        //Para poner imagen de perfil nueva:
        btn_photo.setOnClickListener {
            //Toast.makeText(this, "Seleccione su imágen de perfil", Toast.LENGTH_SHORT).show()
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*" //Directorio para guardalo en Fire Base
            startActivityForResult(intent, 0)// nos lleva a buscar imágenes
        }
    }
    // Esto es para la foto
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            //proceder y verificar cuál era la imagen seleccionada...
            Toast.makeText(this, "Foto seleccionada", Toast.LENGTH_SHORT).show()
            /****Manejando la foto****/
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri) //Actualizar
            imageView_perfil.setImageBitmap(bitmap)//lo que va a redondear la imagen (su ID)
            btn_photo.alpha = 0f //esto coloca la imagen circular... curioso
            /****Manejando la foto****/
        }
    }
    // Función al activar el botón
    private fun registrarUsuario() {
        /***Captura las cadenas de registro****/
        val email = editText_Alfil_email.text.toString()
        val password = editText_Alfil_password.text.toString()
        //Para que no crashie
        if(email.isEmpty() || password.isEmpty() || (selectedPhotoUri == null)) return Toast.makeText(this, "Falta uno o más campos por llenar", Toast.LENGTH_SHORT).show()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    Toast.makeText(baseContext, "Fallo de autenticación", Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener //Fallo de registro
                } else {
                    uploadImageToFirebaseStorage() //sube la foto
                    /** Limpiar **/
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Fallo de creación de usuario", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadAlfilDataToFirebaseFirestore(uid: String, imageStr: String) {
        /** Experimental **/
        // Access a Cloud Firestore instance from your Activity
        val ref = FirebaseFirestore.getInstance()
        // Create a new user with a first and last name
        val alfiles = ref.collection("Alfil")
        // Add a new document with a generated ID
        //el .document(uid) para nombrar el archivo
        alfiles.document(uid).set(mapOf(
            "Activo" to true,
            "Alfil #" to "",
            "Calificación" to 10,
            "Nombre" to editText_Alfil_name.text.toString(),
            "Apellido_Paterno" to editText_Alfil_apellido_paterno.text.toString(),
            "Apellido_Materno" to editText_Alfil_apellido_materno.text.toString(),
            "Placas" to editText_Alfil_placas.text.toString(),
            "Licencia" to editText_Alfil_licencia.text.toString(),
            "email" to editText_Alfil_email.text.toString(),
            "foto" to imageStr,
            //Campos por default:
            "Acumulado" to 10.0,
            "vCompletados" to 0,
            "vRechazados" to 0,
            "Retardos" to 0,
            "Faltas" to 0,
            "Penalizaciones" to 0
        ))
        /** Experimental **/
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return
        //else
        val filename = UUID.randomUUID().toString() //Unic ID
        val ref = FirebaseStorage.getInstance().getReference("/alfil_profile_image/$filename") //Variable de referencia y localización dentro de la base de datos
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener { it ->
                //Toast.makeText(this, "Imagen de perfil guardada", Toast.LENGTH_SHORT).show()
                ref.downloadUrl.addOnSuccessListener {
                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {}
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val temp = FirebaseAuth.getInstance()
        val uid = temp.uid ?: ""//User ID
        val ref = FirebaseDatabase.getInstance().getReference("/users/alfiles/$uid")
        /** Experimental **/
        //Log.d("PhotoURL", "Imagen subida URL?: ${urlUserImage}")
        /** Experimental **/
        val user = User(
            uid,
            editText_Alfil_name.text.toString(),
            profileImageUrl
        )//esto es de la clase User
        /** EXPERIMENTAL **/
        uploadAlfilDataToFirebaseFirestore(uid, profileImageUrl)
        Toast.makeText(baseContext, "Usuario correctamente registrado", Toast.LENGTH_SHORT).show()
        /** Limpiar **/
        val intent = Intent(this, RegisterActivity::class.java)
        finish()
        startActivity(intent)
        /** EXPERIMENTAL **/
        ref.setValue(user)
            .addOnCompleteListener {
                //Toast.makeText(this, "User saved to FireBase Data", Toast.LENGTH_SHORT).show()
                //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)//agregar al Taxi Express code
            }
            .addOnFailureListener {
                Toast.makeText(this, "Falla al seleccionar valor a la base de datos", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.driver_register -> {
                val intent = Intent(this, RegisterActivity::class.java)
                finish()
                startActivity(intent)
            }
            R.id.route_register -> {
                //
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
}
