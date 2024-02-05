package com.javiervillaverde.proyectokotlin.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.javiervillaverde.proyectokotlin.R
import com.javiervillaverde.proyectokotlin.models.Receta
import com.javiervillaverde.proyectokotlin.databinding.FragmentDetalleBinding
import com.javiervillaverde.proyectokotlin.core.FirestoreManager
class DetalleFragment : Fragment(R.layout.fragment_detalle) {
/**
    private lateinit var firestoreManager: FirestoreManager
    private lateinit var binding: FragmentDetalleBinding
    private var recetaId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestoreManager = FirestoreManager(requireContext())
        binding = FragmentDetalleBinding.bind(view)

        recetaId = arguments?.getString("recetaId")

        recetaId?.let { id ->
            firestoreManager.getReceta(id) { receta ->
                receta?.let { showRecetaDetails(it) }
            }
        }

        binding.btnVolver.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showRecetaDetails(receta: Receta) {
        // Mostrar la imagen con Glide
        Glide.with(requireContext())
            .load(receta.urlImagen)
            .placeholder(R.drawable.ic_launcher_background)
            .into(binding.imagen)

        // Mostrar el nombre
        binding.nombre.text = receta.tituloReceta

        // Mostrar la descripción
        binding.descripcion.text = receta.descripcionReceta

        // Mostrar los pasos
        binding.pasos.text = receta.pasos.joinToString("\n") { it }

        // Puedes acceder a otros datos de la receta aquí si es necesario
  }*/
}
