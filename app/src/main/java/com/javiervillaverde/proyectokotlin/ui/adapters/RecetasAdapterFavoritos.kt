package com.javiervillaverde.proyectokotlin.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.javiervillaverde.proyectokotlin.databinding.ViewRecetasFavoritasBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.javiervillaverde.proyectokotlin.models.Receta
import com.javiervillaverde.proyectokotlin.ui.fragments.FavoritosFragment

// Asegúrate de que la clase RecetasAdapterFavoritos tenga los parámetros correctos
class RecetasAdapterFavoritos(
    private val recetas: List<Receta>,
    private val context: FavoritosFragment,
    private val listener: (Receta) -> Unit
) : RecyclerView.Adapter<RecetasAdapterFavoritos.ViewHolder>() {

    inner class ViewHolder(private val binding: ViewRecetasFavoritasBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(receta: Receta) {
            binding.nombre.text = receta.tituloReceta
            binding.root.setOnClickListener { listener(receta) }

            // Usar lifecycleScope del Fragmento para cargar imágenes
            context.lifecycleScope.launch {
                Glide.with(context.requireContext())
                    .load(receta.urlImagen)
                    .into(binding.imagenTarea)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewRecetasFavoritasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recetas[position])
    }

    override fun getItemCount() = recetas.size
}
