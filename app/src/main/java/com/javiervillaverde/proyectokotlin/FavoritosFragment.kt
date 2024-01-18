package com.javiervillaverde.proyectokotlin

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.javiervillaverde.proyectokotlin.databinding.FragmentFavoritosBinding

class FavoritosFragment : Fragment(R.layout.fragment_favoritos) {
    private lateinit var binding: FragmentFavoritosBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFavoritosBinding.bind(view).apply {
            val recetas = RecetaManager.recetas
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = RecetasAdapterFavoritos(recetas, this@FavoritosFragment) { receta ->
                findNavController().navigate(
                    R.id.action_listaFragment_to_detalleFragment,
                    bundleOf("receta" to receta)
                )
            }
        }
    }
}