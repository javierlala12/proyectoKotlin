package com.javiervillaverde.proyectokotlin.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.javiervillaverde.proyectokotlin.R
import com.javiervillaverde.proyectokotlin.databinding.ViewRecetasBuscadorBinding
import com.javiervillaverde.proyectokotlin.models.Receta

/**
 * Adaptador para la lista de recetas en el buscador.
 * @property recetas Lista de recetas a mostrar
 * @property onRecetaClicked Función que se ejecuta cuando se hace clic en una receta
 * @constructor Crea un adaptador para la lista de recetas en el buscador
 * @param recetas Lista de recetas a mostrar
 * @param onRecetaClicked Función que se ejecuta cuando se hace clic en una receta
 */
class RecetasAdapterBuscador(
    private var recetas: List<Receta>,
    private val onRecetaClicked: (String) -> Unit,
) : RecyclerView.Adapter<RecetasAdapterBuscador.ViewHolder>() {

    inner class ViewHolder(private val binding: ViewRecetasBuscadorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(receta: Receta) {
            with(binding) {
                nombre.text = receta.tituloReceta
                Glide.with(imagenReceta.context)
                    .load(receta.urlImagen)
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_broken_image)
                    .into(imagenReceta)


                itemView.setOnClickListener { onRecetaClicked(receta.recetaId) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewRecetasBuscadorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Se enlaza cada receta con el ViewHolder
        holder.bind(recetas[position])
    }

    override fun getItemCount(): Int = recetas.size

    fun updateRecetas(newRecetas: List<Receta>) {
        recetas = newRecetas

        notifyDataSetChanged()
    }
}
