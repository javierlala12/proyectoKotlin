package com.javiervillaverde.proyectokotlin.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.javiervillaverde.proyectokotlin.R
import com.javiervillaverde.proyectokotlin.databinding.ViewRecetasFavoritasBinding
import com.javiervillaverde.proyectokotlin.models.Receta

/**
 * Adaptador para el RecyclerView de recetas en la pantalla principal
 * @property recetas Lista de recetas a mostrar
 * @property onRecetaClicked Función que se ejecuta al hacer clic en una receta
 */
class RecetasAdapterHome(
    private var recetas: List<Receta>,
    private val onRecetaClicked: (String) -> Unit,
) : RecyclerView.Adapter<RecetasAdapterHome.ViewHolder>() {
    inner class ViewHolder(private val binding: ViewRecetasFavoritasBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(receta: Receta) {
            with(binding) {
                nombre.text = receta.tituloReceta
                Glide.with(imagenReceta.context)
                    .load(receta.urlImagen)
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_broken_image)
                    .into(imagenReceta)
            }
            itemView.setOnClickListener { onRecetaClicked(receta.recetaId) }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetasAdapterHome.ViewHolder {
        val binding = ViewRecetasFavoritasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: RecetasAdapterHome.ViewHolder, position: Int) {
        holder.bind(recetas[position])
    }
    override fun getItemCount(): Int = recetas.size
    fun updateRecetas(newRecetas: List<Receta>) {
        recetas = newRecetas
        notifyDataSetChanged()
    }
}