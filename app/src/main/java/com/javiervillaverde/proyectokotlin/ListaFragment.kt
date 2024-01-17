package com.javiervillaverde.proyectokotlin
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.javiervillaverde.proyectokotlin.databinding.FragmentListaBinding
import kotlinx.coroutines.launch

class ListaFragment : Fragment(R.layout.fragment_lista) {
    private lateinit var binding: FragmentListaBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentListaBinding.bind(view).apply {
            val recetas = RecetaManager.recetas
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = RecetasAdapter(recetas, this@ListaFragment) { receta ->
                findNavController().navigate(
                    R.id.action_listaFragment_to_detalleFragment,
                    bundleOf("receta" to receta)
                )
            }

            floatingActionButton.setOnClickListener {
                findNavController().navigate(
                    R.id.action_listaFragment_to_modificarFragment,
                    bundleOf(ModificarFragment.POS to -1)
                )
            }
        }
    }


    fun onBorrar(posicion: Int) {
        lifecycleScope.launch {
            RecetaManager.deleteRecipe(posicion)
            binding.recyclerView.adapter?.notifyItemRemoved(posicion)
        }
    }

    fun onModificar(posicion: Int) {
        findNavController().navigate(
            R.id.action_listaFragment_to_modificarFragment,
            bundleOf(ModificarFragment.POS to posicion)
        )
    }
}