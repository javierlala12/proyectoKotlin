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
import com.javiervillaverde.proyectokotlin.databinding.FragmentBuscadorBinding
import com.javiervillaverde.proyectokotlin.ui.adapters.RecetasAdapterBuscador

class BuscadorFragment : Fragment() {
    private var _binding: FragmentBuscadorBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestoreManager: FirestoreManager
    private lateinit var recetasAdapter: RecetasAdapterBuscador

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuscadorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestoreManager = FirestoreManager(requireContext())
        setupRecyclerView()

        binding.btnBuscar.setOnClickListener {
            buscarRecetas(binding.etBuscar.text.toString())
        }
    }

    private fun setupRecyclerView() {
        recetasAdapter = RecetasAdapterBuscador(emptyList()) { recetaId ->
            navigateToDetalleReceta(recetaId)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recetasAdapter
        }
    }
    /**
     * Método que navega a la pantalla de detalle de receta
     * @param recetaId String con el id de la receta que se pasa a detalle
     */

    private fun navigateToDetalleReceta(recetaId: String) {
        val bundle = Bundle().apply {
            putString("recetaId", recetaId)
        }
        findNavController().navigate(R.id.action_buscadorFragment_to_detalleFragment, bundle)

    }

    /**
     * Método que busca recetas en Firestore
     * @param query String con el texto a buscar
     */
    private fun buscarRecetas(query: String) {
        firestoreManager.buscarRecetas(query) { recetas ->
            recetas?.let {
                recetasAdapter.updateRecetas(it)
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
