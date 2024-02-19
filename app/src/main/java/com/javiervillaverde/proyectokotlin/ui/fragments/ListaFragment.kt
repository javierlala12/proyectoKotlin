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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestoreManager = FirestoreManager(requireContext())
        setupRecyclerView()
        loadRecetasFromFirestore()

        binding.floatingActionButton.setOnClickListener {
            navigateToAddNewReceta()
        }
    }

    private fun setupRecyclerView() {
        val recetasAdapter = RecetasAdapter(
            emptyList(),
            onRecetaClicked = { recetaId ->
                navigateToRecetaDetalle(recetaId)
            },
            onModificarClicked = { recetaId ->
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
    /**
     * Carga las recetas del usuario desde Firestore
     * Estas recetas en autor tienen que tener el mismo id del usuario
     */

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
    /**
     * Navega a la pantalla de modificar receta
     *@param recetaId pasa el id de la receta como argumento
     * Este metodo se ejecuta al pulsar el boton de modificar
     */

    private fun navigateToModificarReceta(recetaId: String) {
        val bundle = Bundle().apply {
            putString("recetaId", recetaId)
        }
        findNavController().navigate(R.id.action_listaFragment_to_modificarFragment, bundle)
    }
    /**
     * Navega a la pantalla de detalle receta
     *@param recetaId pasa el id de la receta como argumento
     * Este metodo se ejecuta al pulsar una receta
     */
    private fun navigateToRecetaDetalle(recetaId: String) {
        val bundle = Bundle().apply {
            putString("recetaId", recetaId)
        }
        findNavController().navigate(R.id.action_listaFragment_to_detalleFragment, bundle)
    }
    /**
     * Navega a la pantalla de agregar nueva receta
     * Este metodo se ejecuta al pulsar el floatingActionButton
     */
    private fun navigateToAddNewReceta() {
        findNavController().navigate(R.id.action_listaFragment_to_modificarFragment)
    }
    /**
     * Elimina la receta de Firestore
     *@param recetaId pasa el id de la receta como argumento
     * Este metodo se ejecuta al pulsar el boton de borrar
     */
    private fun deleteReceta(recetaId: String) {
        lifecycleScope.launch {
            firestoreManager.deleteReceta(recetaId) { isSuccess, mensajeError ->
                if (isSuccess) {
                    loadRecetasFromFirestore()
                } else {
                    mensajeError.let {

                    }
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
