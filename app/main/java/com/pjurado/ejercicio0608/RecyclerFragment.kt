package com.pjurado.ejercicio0608

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.pjurado.ejercicio0608.databinding.FragmentRecyclerBinding


class RecyclerFragment : Fragment(R.layout.fragment_recycler) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentRecyclerBinding.bind(view).apply {
            recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recycler.adapter = FotosAdapter(listaFotos){ urlImagen ->
                val fragmentImagen = ImagenFragment()
                fragmentImagen.arguments = bundleOf("url" to urlImagen)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.detalleimagen, fragmentImagen)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private val listaFotos = (1..100).map {
        "https://picsum.photos/200/300?random=$it"
    }
}