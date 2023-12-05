package com.pjurado.ejercicio0608

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pjurado.ejercicio0608.databinding.ViewImagenBinding

class FotosAdapter(val listaFotos: List<String>, val listener: (String) -> Unit)
    :RecyclerView.Adapter<FotosAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ViewImagenBinding.bind(view)

        fun bind(foto: String){
            Glide.with(binding.imagen).load(foto).into(binding.imagen)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_imagen, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = listaFotos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listaFotos[position])
        holder.itemView.setOnClickListener { listener(listaFotos[position]) }

    }
}


