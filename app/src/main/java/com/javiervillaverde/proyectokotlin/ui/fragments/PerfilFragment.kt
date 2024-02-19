package com.javiervillaverde.proyectokotlin.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.javiervillaverde.proyectokotlin.R
import com.javiervillaverde.proyectokotlin.core.AuthManager
import com.javiervillaverde.proyectokotlin.core.FirestoreManager
import com.javiervillaverde.proyectokotlin.databinding.FragmentPerfilBinding

class PerfilFragment : Fragment(R.layout.fragment_perfil) {
    private var binding: FragmentPerfilBinding? = null
    private lateinit var auth: AuthManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPerfilBinding.bind(view)
        auth = AuthManager(requireContext())

        loadUserProfile()
        binding?.btnSignOut?.setOnClickListener {
            signOut()
        }
        binding?.btnUpdateProfile?.setOnClickListener {
            updateProfile()
        }
    }
    /**
     * Carga el perfil del usuario actual
     * Si el usuario no tiene id, no hace nada
     * Si el usuario tiene id, actualiza su perfil
     */

    private fun loadUserProfile() {
        val userId = auth.getCurrentUser()?.uid
        if (userId != null) {
            val firestoreManager = FirestoreManager(requireContext())
            firestoreManager.getUsuarioPorId(userId) { usuario ->
                binding?.textUserEmail?.text = usuario?.email ?: "No disponible"
                binding?.editUserName?.setText(usuario?.nombreUsuario ?: "")
                binding?.imageUserProfile?.let {
                    Glide.with(this).load(usuario?.imagenPerfil).into(
                        it
                    )
                }
            }
        }
    }

    /**
     * Actualiza el perfil del usuario actual
     * Si el usuario no tiene id, no hace nada
     * Si el usuario tiene id, actualiza su perfil
     */
    private fun updateProfile() {
        val nuevoNombre = binding?.editUserName?.text.toString()
        val nuevaUrlImagen = binding?.editUserImageUrl?.text.toString() // Asume que tienes un EditText para la URL de la imagen
        val userId = auth.getCurrentUser()?.uid

        if (userId != null) {
            val firestoreManager = FirestoreManager(requireContext())
            val datosActualizados = hashMapOf<String, Any>(
                "nombreUsuario" to nuevoNombre
            )

            if (nuevaUrlImagen.isNotEmpty()) {
                datosActualizados["imagenPerfil"] = nuevaUrlImagen
            }

            firestoreManager.updateUsuario(userId, datosActualizados) { exito, mensaje ->
                if (exito) {
                    Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                    binding?.imageUserProfile?.let { Glide.with(this).load(nuevaUrlImagen).into(it) }
                } else {
                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    /**
     * Método que se ejecuta cuando la vista es destruida
     */

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    /**
     * Metodos que se ejecutan al pulsar el boton de cerrar sesión
     * Cierra la sesión del usuario actual y lo redirige a la pantalla de inicio de sesión
     */
    private fun signOut() {
        auth.signOut()

        launchLoginActivity()
    }
    // Metodo que lo redirige a la pantalla de inicio de sesión
    private fun launchLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
