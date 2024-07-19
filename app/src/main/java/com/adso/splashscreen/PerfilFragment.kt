package com.adso.splashscreen


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController


class PerfilFragment : Fragment() {

    private lateinit var btnAtras: ImageButton
    private lateinit var btngo : ImageButton

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_perfil, container, false)
        val root = inflater.inflate(R.layout.fragment_perfil, container,false)


        val btnAtras = view.findViewById<ImageButton>(R.id.buttonBack2)

        // Configurar el clic del botón
        btnAtras.setOnClickListener {

            //findNavController().navigate(PerfilFragmentDirections.actionPerfilFragmentToUbiMap())
            val intent = Intent(requireContext(), UbiMap::class.java)
            startActivity(intent)
        }

        return view

        val btngo = root.findViewById<ImageButton>(R.id.btngo)

        btngo.setOnClickListener {
            findNavController().navigate(PerfilFragmentDirections.actionPerfilFragmentToDatosFragment( name = "Mis Datos"))
            /*val intent = Intent(requireContext(), PerfilFragmentDirections.actionPerfilFragmentToDatosFragment()::class.java)
            startActivity(intent)*/
        }

        return root






       
    }

}