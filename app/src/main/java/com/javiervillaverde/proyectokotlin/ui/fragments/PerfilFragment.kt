package com.javiervillaverde.proyectokotlin.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.javiervillaverde.proyectokotlin.R
import com.javiervillaverde.proyectokotlin.core.AuthManager
import com.javiervillaverde.proyectokotlin.databinding.FragmentPerfilBinding

class PerfilFragment : Fragment(R.layout.fragment_perfil) {
    private var binding: FragmentPerfilBinding? = null
    private lateinit var auth: AuthManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPerfilBinding.bind(view)
        auth = AuthManager(requireContext())

        binding?.btnSignOut?.setOnClickListener {
            signOut()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun signOut() {
        auth.signOut()

        launchLoginActivity()
    }

    private fun launchLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
