package com.javiervillaverde.proyectokotlin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.javiervillaverde.proyectokotlin.R
import com.javiervillaverde.proyectokotlin.core.FirestoreManager

import com.javiervillaverde.proyectokotlin.databinding.FragmentHomeBinding
import com.javiervillaverde.proyectokotlin.ui.adapters.RecetasAdapterHome

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding?= null
    private val binding get() = _binding!!
    private lateinit var firestoreManager: FirestoreManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)
        firestoreManager = FirestoreManager(requireContext())

        setupRecyclerView()
        loadRecetasHome()

    }
    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = RecetasAdapterHome(
                emptyList(),
                onRecetaClicked = { recetaId ->
                    navigateToRecetaDetalle(recetaId)
                }
            )
        }
    }
    /**
     * Navega a la pantalla de detalle de la receta
     * @param recetaId id de la receta que se quiere ver en detalle
     */
    private fun navigateToRecetaDetalle(recetaId: String) {
        val bundle = Bundle().apply {
            putString("recetaId", recetaId)
        }
        findNavController().navigate(R.id.action_favoritosFragment_to_detalleFragment, bundle)
    }

    /**
     * Carga las recetas de la base de datos.
     * Las recetas son cargadas en orden descendente de usuarios favoritos.
     */

    private fun loadRecetasHome() {
        firestoreManager.getRecetasHome {
            if (it != null) {
                (binding.recyclerView.adapter as RecetasAdapterHome).updateRecetas(it)
            }
        }
    }
}