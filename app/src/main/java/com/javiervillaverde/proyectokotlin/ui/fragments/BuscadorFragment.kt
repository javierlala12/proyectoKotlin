package com.javiervillaverde.proyectokotlin.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.javiervillaverde.proyectokotlin.R
import com.javiervillaverde.proyectokotlin.databinding.FragmentBuscadorBinding

class BuscadorFragment : Fragment(R.layout.fragment_buscador) {
    private lateinit var binding: FragmentBuscadorBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentBuscadorBinding.bind(view).apply {



}}}
