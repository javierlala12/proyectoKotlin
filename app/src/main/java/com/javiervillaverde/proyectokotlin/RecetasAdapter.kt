package com.javiervillaverde.proyectokotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.javiervillaverde.proyectokotlin.databinding.ViewRecetasBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecetasAdapter(
    val recetas: List<Receta>,
    val context: ListaFragment,
    val listener: (Receta) -> Unit
) : RecyclerView.Adapter<RecetasAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ViewRecetasBinding.bind(view)

        fun bind(receta: Receta) {
            binding.nombre.text = receta.nombre

            // Utilizamos corrutinas para cargar imágenes de forma asíncrona
            loadImageFromUrl(receta.imageUrl)

            // Puedes agregar más lógica para mostrar la descripción, pasos, etc.
        }

        // Utilizamos corrutinas para cargar imágenes de forma asíncrona
        private fun loadImageFromUrl(imageUrl: String) {
            GlobalScope.launch {
                val bitmap = withContext(Dispatchers.IO) {
                    Glide.with(binding.root.context)
                        .asBitmap()
                        .load(imageUrl)
                        .submit()
                        .get()
                }

                withContext(Dispatchers.Main) {
                    binding.imagenTarea.setImageBitmap(bitmap)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_recetas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recetas[position])
        holder.binding.nombre.setOnClickListener { listener(recetas[position]) }
        holder.binding.btnBorrar.setOnClickListener {
            context.lifecycleScope.launch {
                listener(recetas[position])
                context.onBorrar(position)
            }
        }
        holder.binding.btnModificar.setOnClickListener { context.onModificar(position) }
    }

    override fun getItemCount(): Int {
        return recetas.size
    }
}