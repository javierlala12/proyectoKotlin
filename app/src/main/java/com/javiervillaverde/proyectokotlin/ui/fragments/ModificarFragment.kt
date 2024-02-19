package com.javiervillaverde.proyectokotlin.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.javiervillaverde.proyectokotlin.R
import com.javiervillaverde.proyectokotlin.databinding.FragmentModificarBinding
import com.javiervillaverde.proyectokotlin.core.FirestoreManager
import com.javiervillaverde.proyectokotlin.models.Receta
import kotlinx.coroutines.launch

class ModificarFragment : Fragment(R.layout.fragment_modificar) {

    companion object {
        const val RECETA_ID = "receta_id"
    }

    private lateinit var binding: FragmentModificarBinding
    private lateinit var firestoreManager: FirestoreManager
    private var recetaId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentModificarBinding.bind(view)
        firestoreManager = FirestoreManager(requireContext())
        recetaId = arguments?.getString(RECETA_ID)

        binding.btnGuardar.setOnClickListener {
            lifecycleScope.launch {
                guardarOModificarReceta()
            }
        }

        binding.btnCancelar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    /**
     * Guarda o modifica una receta en Firestore
     * Si recetaId es null, se guarda una nueva receta
     * Si recetaId no es null, se modifica la receta existente
     * @param recetaId id de la receta
     * @param tituloReceta titulo de la receta
     * @param urlImagen url de la imagen de la receta
     * @param descripcionReceta descripcion de la receta
     * @param pasos pasos de la receta
     * @param ingredientes ingredientes de la receta
     * @param autor autor de la receta
     * @param usuariosFavoritos lista de usuarios que han marcado la receta como favorita
     */
    private fun guardarOModificarReceta() {
        val tituloReceta = binding.nombre.text.toString()
        val urlImagen = binding.imagenUrl.text.toString()
        val descripcionReceta = binding.descripcion.text.toString()
        val pasos = binding.pasos.text.toString().lines()
        val ingredientes = binding.ingredientes.text.toString()
            .split("\n")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
        val receta = Receta(
            recetaId = recetaId ?: "",
            tituloReceta = tituloReceta,
            descripcionReceta = descripcionReceta,
            ingredientes = ingredientes, // Asume que defines cómo manejar esto
            pasos = pasos,
            urlImagen = urlImagen,
            autor = firestoreManager.getUserId() ?: "",
            usuariosFavoritos = listOf()
        )
        if (recetaId.isNullOrEmpty()) {
            firestoreManager.addReceta(receta) { exito, nuevoRecetaId ->
                if (exito) {
                    activity?.runOnUiThread {
                        showToast("Receta guardada exitosamente")
                        parentFragmentManager.popBackStack()
                    }
                } else {
                    showToast("Error al guardar la receta")
                }
            }
        } else {
            // Actualizar una receta existente
            val camposModificados = mapOf(

                "tituloReceta" to tituloReceta,
                "urlImagen" to urlImagen,
                "descripcionReceta" to descripcionReceta,
                "pasos" to pasos,
                "ingredientes" to ingredientes
            )
            firestoreManager.updateReceta(recetaId!!, camposModificados) { exito, _ ->
                if (exito) {
                    // Manejar éxito, posiblemente navegando de regreso o mostrando un mensaje
                    activity?.runOnUiThread {
                        parentFragmentManager.popBackStack()
                    }
                } else {
                    showToast("Error al actualizar la receta")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
