package com.example.taxiexpress2.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.taxiexpress2.R
import com.example.taxiexpress2.inicio.NextTravel
import com.example.taxiexpress2.maps.MapboxMap
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.ruta_nueva.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    //val adapter = GroupAdapter<GroupieViewHolder>() /** Experimental 25/08/2020 **/
    val novos_viajes: NextTravel? = null/** Experimental **/
    val TAG = "LatestMessages"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /** Experimental **/
        val adapter = GroupAdapter<GroupieViewHolder>() // Es posible gracias a la clase UserItem

        for(i in 0..5) adapter.add(UserItem())

        recyclerview_viajes_disponibles.adapter = adapter // recyclerview de los mapas
        //Línea de separación de chats, útil para la parte de separar viajes
        recyclerview_viajes_disponibles.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        //
        adapter.setOnItemClickListener { item, view ->
            val mapeo = Intent(activity, MapboxMap::class.java)
            //val row = item as
            startActivity(mapeo)
        }

        fetchViajes() // Pon los viajes
        /** Experimental **/

        btn_aceptar_viaje.setOnClickListener {
            //Toast.makeText(activity,"Aceptar Viaje",Toast.LENGTH_SHORT).show()
            /** EXPERIMENTAL **/
            Snackbar.make(menu_viajes,"Aceptar Viaje", Snackbar.LENGTH_LONG).show()
            // Fuente: https://www.youtube.com/watch?v=PQP3xTlaSRE
            /** EXPERIMENTAL **/
        }
        btn_rechazar_viaje.setOnClickListener {
            Toast.makeText(activity,"Rechazar Viaje",Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchViajes() {
        //val fromId = FirebaseAuth.getInstance().uid
        val viajex = FirebaseFirestore.getInstance()
        //val viajeData = viajex.collection("TripDetail").document(fromId.toString()) //geopoint
        val viajeData = viajex.collection("Viaje")
            .document("RDDxHHI9m4fcwYuznrx1")// Este es el bueno
        /** MEGA EXPERIMENTAL **/
        // Leer todos los documentos de una colección !
        viajex.collection("solar_system").get()
            .addOnSuccessListener {
                it.forEach {
                    Log.d("Checando", "Documentos dentro de solar_system: ${it.id}")
                }
            }
        /** MEGA EXPERIMENTAL **/
        viajeData.get().addOnCompleteListener {
            //textView_gallery_alfil_name.text = it.result?.get("Nombre").toString()
            textViewID_viaje.text = "RDDxHHI9m4fcwYuznrx1"
            textViewppartida_viaje.text = it.result?.get("Inicio_Ciudad").toString()+", "+
                    it.result?.get("Inicio_Colonia").toString()+", "+it.result?.get("Inicio_Calle").toString()
            textViewpdestino_viaje.text = it.result?.get("Destino_Ciudad").toString()+", "+
                    it.result?.get("Destino_Colonia").toString()+", "+it.result?.get("Destino_Calle").toString()
            textViewpasajero_viaje.text = it.result?.get("Pasajero").toString()
        }
    }

    class UserItem: Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            //Será llamada en nuestra lista para cada usuario (objeto) después...
            /*viewHolder.itemView.username_textview_new_message.text = user.username //Asigna el nombre
            //Imagenes
            Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageview_new_message)
            Log.d("Picaso", "user.profileImageUrl: ${user.profileImageUrl}")
            //Log.d("Picaso", "user.profileImageUrl: ${user.profileImageUrl}")
            */
        }
        override fun getLayout(): Int {
            return R.layout.ruta_nueva
        }
    }
    /** Experimental, tal vez lo quitemos, **/
    /*private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid //Al usuario que hayas accedido
        if (uid == null) {
            val intent = Intent(this, MainActivity::class.java)
            //Esto va a ser muy útil, limpiar el stack de actividades anteriores
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)//agregar al Taxi Express code
            startActivity(intent)
        }
    }*/
}
