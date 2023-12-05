package com.javiervillaverde.proyectokotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.javiervillaverde.proyectokotlin.databinding.FragmentModificarBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ModificarFragment : Fragment(R.layout.fragment_modificar) {

    companion object {
        const val POS = "posicion"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentModificarBinding.bind(view)

        // Obtén la posición de los argumentos
        val posicion = arguments?.getInt(POS)

        // Configura el OnClickListener para el botón "Guardar"
        binding.btnGuardar.setOnClickListener {
            // Verifica si es una modificación o una nueva receta
            GlobalScope.launch {
                if (posicion != -1) {
                    // Accede a las vistas dentro del hilo principal
                    val nuevaReceta = Receta(
                        binding.nombre.text.toString(),
                        binding.imagenUrl.text.toString(),
                        binding.descripcion.text.toString(),
                        binding.pasos.text.toString().lines() // Dividir los pasos en líneas
                    )

                    // Llama a modifyRecipe dentro de una coroutine
                    RecetaManager.modifyRecipe(posicion!!, nuevaReceta)
                } else {
                    // Accede a las vistas dentro del hilo principal
                    val nuevaReceta = Receta(
                        binding.nombre.text.toString(),
                        binding.imagenUrl.text.toString(),
                        binding.descripcion.text.toString(),
                        binding.pasos.text.toString().lines() // Dividir los pasos en líneas
                    )

                    // Llama a addRecipe dentro de una coroutine
                    RecetaManager.addRecipe(nuevaReceta)
                }

                // Regresa al fragmento anterior
                withContext(Dispatchers.Main) {
                    parentFragmentManager.popBackStack()
                }
            }
        }

        binding.btnCancelar.setOnClickListener {
            // Regresa al fragmento anterior
            parentFragmentManager.popBackStack()
        }
    }
}