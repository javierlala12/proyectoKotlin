package com.javiervillaverde.proyectokotlin.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.javiervillaverde.proyectokotlin.R
import com.javiervillaverde.proyectokotlin.databinding.FragmentFavoritosBinding

class FavoritosFragment : Fragment(R.layout.fragment_favoritos) {
    private lateinit var binding: FragmentFavoritosBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFavoritosBinding.bind(view).apply {



            }
        }
    }