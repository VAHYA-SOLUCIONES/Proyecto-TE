package com.example.taxiexpress2.ui.slideshow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.taxiexpress2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.fragment_slideshow.*

class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProviders.of(this).get(SlideshowViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
        /*val textView: TextView = root.findViewById(R.id.text_slideshow)
        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
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
            val Acumulado = it.result?.get("Acumulado").toString()
            val vCompletados = it.result?.get("vCompletados").toString()
            val vRechazados = it.result?.get("vRechazados").toString()
            val Retardos = it.result?.get("Retardos").toString()
            val Faltas = it.result?.get("Faltas").toString()
            val Penalizaciones = it.result?.get("Penalizaciones").toString()
            textView35.text = "$ ${Acumulado}" //Acumulado de $$$$
            textView37.text = "Viajes Completados: ${vCompletados}" //Viajes Completados
            textView38.text = "Viajes Rechazados: ${vRechazados}" //Viajes Rechazados
            textView39.text = "Retardos: ${Retardos}" //Retardos
            textView40.text = "Faltas: ${Faltas}" //Faltas
            textView42.text = "Rechazados:$ ${Penalizaciones}" //Penalizaciones
            //textView_gallery_alfil_name.text = it.result?.get("Nombre").toString()
        }
        /** Experimental **/
        /** Probando poner la imagen de usuario **/
    }
}
