package com.javiervillaverde.proyectokotlin


import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.javiervillaverde.proyectokotlin.databinding.FragmentDetalleBinding

class DetalleFragment : Fragment(R.layout.fragment_detalle) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetalleBinding.bind(view).apply {
            val receta = arguments?.getParcelable<Receta>("receta")

            // Mostrar la imagen con Glide
            Glide.with(requireContext())
                .load(receta?.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imagen)

            // Mostrar el nombre
            nombre.text = receta?.nombre

            // Mostrar la descripción
            descripcion.text = receta?.descripcion

            // Mostrar los pasos
            pasos.text = receta?.pasos?.joinToString("\n") ?: "No hay pasos disponibles"

            // Manejar el evento del botón para volver a la lista
            val btnVolver: Button = view.findViewById(R.id.btnVolver)
            btnVolver.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}