package com.javiervillaverde.proyectokotlin.ui.fragments

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.javiervillaverde.proyectokotlin.R
import com.javiervillaverde.proyectokotlin.models.Receta
import com.javiervillaverde.proyectokotlin.databinding.FragmentDetalleBinding
import com.javiervillaverde.proyectokotlin.core.FirestoreManager

class DetalleFragment : Fragment(R.layout.fragment_detalle) {
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
                receta?.let {
                    showRecetaDetails(it)
                    configurarBotonFavorito(id)
                }
            }
        }

        binding.btnVolver.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    /**
     * Método que configura el botón de favoritos
     * @param recetaId String con el id de la receta
     */

    private fun configurarBotonFavorito(recetaId: String) {
        val userId = firestoreManager.getUserId() ?: return

        firestoreManager.getReceta(recetaId) { receta ->
            receta?.let {
                val esFavorita = it.usuariosFavoritos.contains(userId)
                actualizarIconoFavorito(esFavorita)

                binding.btnFavorito.setOnClickListener {
                    firestoreManager.toggleFavorito(recetaId, userId) { exito ->
                        if (exito) {
                            // Actualizar el estado del botón de favoritos
                            val nuevoEstado = !esFavorita
                            actualizarIconoFavorito(nuevoEstado)
                        } else {
                            // Mostrar un mensaje de error
                        }
                    }
                }
            }
        }
    }

    /**
     * Método que actualiza el icono del botón de favoritos
     * @param esFavorita Boolean que indica si la receta es favorita
     */
    private fun actualizarIconoFavorito(esFavorita: Boolean) {
        if (esFavorita) {
            binding.btnFavorito.setImageResource(R.drawable.estrella_rellena)
        } else {
            binding.btnFavorito.setImageResource(R.drawable.favoritos)
        }
        binding.btnFavorito.tag = esFavorita
    }

    /**
     * Método que muestra los detalles de la receta
     * @param receta Receta con los datos de la receta
     */

    private fun showRecetaDetails(receta: Receta) {
        // Mostrar la imagen con Glide
        Glide.with(requireContext())
            .load(receta.urlImagen)
            .placeholder(R.drawable.ic_launcher_background)
            .into(binding.imagen)

        //Subrayar nombre de la receta
        val nombreReceta = receta.tituloReceta
        val spannableNombre = SpannableString(nombreReceta).apply {
            setSpan(UnderlineSpan(), 0, nombreReceta.length, 0)
        }
        // Mostrar el nombre
        binding.nombre.text = spannableNombre

        // Mostrar la descripción
        binding.descripcion.text = receta.descripcionReceta

        // Mostrar los ingredientes
        binding.ingredientes?.text = receta.ingredientes.joinToString("\n"){ it }

        // Mostrar los pasos
        binding.pasos.text = receta.pasos.joinToString("\n") { it }

        // Mostrar el nombre de usuario del autor
        receta.autor.let { autor ->
            firestoreManager.getUsuarioPorId(autor) { usuario ->
                binding.autor.text = usuario?.nombreUsuario ?: "Autor desconocido"
            }
        }
    }
}
