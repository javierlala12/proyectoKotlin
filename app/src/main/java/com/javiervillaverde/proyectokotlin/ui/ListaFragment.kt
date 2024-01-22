package com.javiervillaverde.proyectokotlin.ui
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.javiervillaverde.proyectokotlin.R
import com.javiervillaverde.proyectokotlin.databinding.FragmentListaBinding
import kotlinx.coroutines.launch

class ListaFragment : Fragment(R.layout.fragment_lista) {
    private lateinit var binding: FragmentListaBinding
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentListaBinding.bind(view)
        navController = findNavController()

        val recetas = RecetaManager.recetas
        val recetasAdapter = RecetasAdapter(recetas, this) { receta ->
            navController.navigate(
                R.id.action_listaFragment_to_detalleFragment,
                bundleOf("receta" to receta)
            )
        }


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = recetasAdapter

        binding.floatingActionButton.setOnClickListener {
            navController.navigate(
                R.id.action_listaFragment_to_modificarFragment,
                bundleOf(ModificarFragment.POS to -1)
            )
        }
    }

    fun onBorrar(posicion: Int) {
        lifecycleScope.launch {
            RecetaManager.deleteRecipe(posicion)
            binding.recyclerView.adapter?.notifyItemRemoved(posicion)
        }
    }

    fun onModificar(posicion: Int) {
        navController.navigate(
            R.id.action_listaFragment_to_modificarFragment,
            bundleOf(ModificarFragment.POS to posicion)
        )
    }
}
