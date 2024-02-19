package com.javiervillaverde.proyectokotlin.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.javiervillaverde.proyectokotlin.R
import com.javiervillaverde.proyectokotlin.databinding.ViewRecetasBinding
import com.javiervillaverde.proyectokotlin.models.Receta

/**
 * Adaptador para la lista de recetas.
 * @property recetas Lista de recetas a mostrar
 * @property onRecetaClicked Función que se ejecuta cuando se hace clic en una receta
 * @property onModificarClicked Función que se ejecuta cuando se hace clic en el botón de modificar
 * @property onBorrarClicked Función que se ejecuta cuando se hace clic en el botón de borrar
 * @constructor Crea un adaptador para la lista de recetas
 * @param recetas Lista de recetas a mostrar
 * @param onRecetaClicked Función que se ejecuta cuando se hace clic en una receta
 * @param onModificarClicked Función que se ejecuta cuando se hace clic en el botón de modificar
 * @param onBorrarClicked Función que se ejecuta cuando se hace clic en el botón de borrar
 */
class RecetasAdapter(
    private var recetas: List<Receta>,
    private val onRecetaClicked: (String) -> Unit,
    private val onModificarClicked: (String) -> Unit,
    private val onBorrarClicked: (String) -> Unit
) : RecyclerView.Adapter<RecetasAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ViewRecetasBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(receta: Receta, onRecetaClicked: (String) -> Unit, onModificarClicked: (String) -> Unit, onBorrarClicked: (String) -> Unit) {
            with(binding) {
                nombre.text = receta.tituloReceta
                Glide.with(imagenTarea.context)
                    .load(receta.urlImagen)
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_broken_image)
                    .into(imagenTarea)

                itemView.setOnClickListener { onRecetaClicked(receta.recetaId) }
                btnModificar.setOnClickListener { onModificarClicked(receta.recetaId) }
                btnBorrar.setOnClickListener { onBorrarClicked(receta.recetaId) }
            }
        }
    }

    /**
     * Actualiza el conjunto de recetas en el adaptador y notifica cualquier cambio.
     * @param newRecetas Nueva lista de recetas
     */
    fun updateRecetas(newRecetas: List<Receta>) {
        recetas = newRecetas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewRecetasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recetas[position], onRecetaClicked, onModificarClicked, onBorrarClicked)
    }

    override fun getItemCount(): Int = recetas.size
}
