package com.example.taxiexpress2.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.taxiexpress2.R
import com.example.taxiexpress2.models.Alfil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.fragment_gallery.*

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /** Probando poner la imagen de usuario **/
        /** Experimental **/
        val fromId = FirebaseAuth.getInstance().uid
        val alfilex = FirebaseFirestore.getInstance()
        val alfilData = alfilex.collection("Alfil").document(fromId.toString())
        alfilData.get().addOnCompleteListener {
            //AlfilName.text = it.result?.get("Nombre").toString()
            textView_gallery_alfil_name.text = it.result?.get("Nombre").toString()
            textView_gallery_alfil_calificacion.text = it.result?.get("Calificación").toString()
            textView_gallery_alfil_licencia.text = it.result?.get("Placas").toString()
            if (it.result?.get("Activo") == false) textView_gallery_alfil_estado.text = "Inactivo"
            else textView_gallery_alfil_estado.text = "Activo"
            //Foto
            val ref = it.result?.get("foto").toString() //dirección de la foto
            Log.d("Checando", "foto URL: ${ref}")
            //
            //imageView_gallery_alfil_image
            Picasso.get().load(ref).into(imageView_gallery_alfil_image)
        }
        /** Experimental **/
        /** Probando poner la imagen de usuario **/
    }
}
