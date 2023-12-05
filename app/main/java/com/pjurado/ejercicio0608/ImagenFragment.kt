package com.pjurado.ejercicio0608

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.pjurado.ejercicio0608.databinding.FragmentImagenBinding

class ImagenFragment : Fragment(R.layout.fragment_imagen) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentImagenBinding.bind(view).apply {
            val url = arguments?.getString("url")
            if (url != null) {
                Glide.with(this@ImagenFragment)
                    .load(url)
                    .into(imagenGrande)
            }
        }
    }
}