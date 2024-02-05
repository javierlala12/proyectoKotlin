package com.javiervillaverde.proyectokotlin.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.javiervillaverde.proyectokotlin.R
import com.javiervillaverde.proyectokotlin.databinding.FragmentListaBinding
import com.javiervillaverde.proyectokotlin.ui.adapters.RecetasAdapter
import com.javiervillaverde.proyectokotlin.core.FirestoreManager
import kotlinx.coroutines.launch

class ListaFragment : Fragment() {

    private var _binding: FragmentListaBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestoreManager: FirestoreManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestoreManager = FirestoreManager(requireContext())
        setupRecyclerView()
        loadRecetasFromFirestore()

        // Configura el listener para el botón de añadir nueva receta
        binding.floatingActionButton.setOnClickListener {
            navigateToAddNewReceta()
        }
    }

    private fun setupRecyclerView() {
        // Inicializa el adaptador con callbacks vacíos o con acciones específicas
        val recetasAdapter = RecetasAdapter(
            emptyList(),
            onRecetaClicked = { recetaId ->
                // Aquí puedes implementar la acción al hacer clic en una receta
                /**navigateToRecetaDetalle(recetaId)*/
            },
            onModificarClicked = { recetaId ->
                // Aquí puedes implementar la acción al hacer clic en modificar
                navigateToModificarReceta(recetaId)
            },
            onBorrarClicked = { recetaId ->
                deleteReceta(recetaId)
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recetasAdapter
        }
    }

    private fun loadRecetasFromFirestore() {
        firestoreManager.getUserId()?.let { userId ->
            lifecycleScope.launch {
                firestoreManager.getRecetasDelUsuario(userId) { recetas ->
                    recetas?.let {
                        (binding.recyclerView.adapter as RecetasAdapter).updateRecetas(it)
                    }
                }
            }
        }
    }
    private fun navigateToModificarReceta(recetaId: String) {
        val bundle = Bundle().apply {
            putString("recetaId", recetaId)
        }
        findNavController().navigate(R.id.action_listaFragment_to_modificarFragment, bundle)
    }


    private fun navigateToRecetaDetalle(recetaId: String) {
        val bundle = Bundle().apply {
            putString("recetaId", recetaId)
        }
        findNavController().navigate(R.id.action_listaFragment_to_detalleFragment, bundle)
    }

    private fun navigateToAddNewReceta() {
        findNavController().navigate(R.id.action_listaFragment_to_modificarFragment)
    }

    private fun deleteReceta(recetaId: String) {
        lifecycleScope.launch {
            firestoreManager.deleteReceta(recetaId) { isSuccess, mensajeError ->
                if (isSuccess) {
                    loadRecetasFromFirestore() // Recargar la lista después de borrar
                } else {
                    mensajeError?.let {
                        // Muestra un mensaje de error o realiza acciones de manejo de errores aquí
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpieza para evitar fugas de memoria
    }
}
