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
import com.javiervillaverde.proyectokotlin.databinding.FragmentFavoritosBinding
import com.javiervillaverde.proyectokotlin.ui.adapters.RecetasAdapterFavoritos

class FavoritosFragment : Fragment(R.layout.fragment_favoritos) {
    private var _binding: FragmentFavoritosBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestoreManager: FirestoreManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoritosBinding.bind(view)

        firestoreManager = FirestoreManager(requireContext())

        setupRecyclerView()
        loadFavoritos()
    }

    private fun setupRecyclerView() {
        var recetasAdapterFavoritos = RecetasAdapterFavoritos(
            emptyList(),
            onRecetaClicked = { recetaId ->
                navigateToRecetaDetalle(recetaId)

            })
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recetasAdapterFavoritos
        }
    }

    /**
     * Método que navega a la pantalla de detalle de receta
     * @param recetaId String con el id de la receta que se pasa a detalle
     */
    private fun navigateToRecetaDetalle(recetaId: String) {
        val bundle = Bundle().apply {
            putString("recetaId", recetaId)
        }
        findNavController().navigate(R.id.action_favoritosFragment_to_detalleFragment, bundle)
    }
    /**
     * Método que carga las recetas favoritas del usuario y los muestra en el recyclerview
     */
    private fun loadFavoritos() {
        firestoreManager.getUserId()?.let { userId ->
            firestoreManager.getRecetasFavoritas(userId) { recetas ->
                recetas?.let {
                    (binding.recyclerView.adapter as RecetasAdapterFavoritos).updateRecetas(it)
                }
            }
        }
    }
    /**
     * Método que se ejecuta cuando la vista es destruida
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}