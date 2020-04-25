package com.example.taxiexpress2.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.taxiexpress2.MapaViajes
import com.example.taxiexpress2.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    //private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        /*val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapeo = Intent(activity,MapaViajes::class.java)
        btn_aceptar_viaje.setOnClickListener {
            Toast.makeText(activity,"Aceptar Viaje",Toast.LENGTH_SHORT).show()
        }
        btn_rechazar_viaje.setOnClickListener {
            Toast.makeText(activity,"Rechazar Viaje",Toast.LENGTH_SHORT).show()
        }
        //navController = Navigation.findNavController(view)
        btn_map.setOnClickListener {
            Toast.makeText(activity,"Cargando mapa",Toast.LENGTH_SHORT).show()
            startActivity(mapeo)
        }

    }

}
