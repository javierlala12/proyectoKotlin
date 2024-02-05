package com.javiervillaverde.proyectokotlin.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.GoogleAuthProvider
import com.javiervillaverde.proyectokotlin.core.AuthManager
import com.javiervillaverde.proyectokotlin.core.AuthRes
import com.javiervillaverde.proyectokotlin.databinding.ActivityLoginBinding
import com.javiervillaverde.proyectokotlin.ui.activities.CrearCuenta
import com.javiervillaverde.proyectokotlin.ui.activities.MainActivity
import com.javiervillaverde.proyectokotlin.ui.adapters.RecuperaContrasena
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private val auth = AuthManager(this)
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        setupGoogleSignIn()
        checkIfUserIsLoggedIn()
        setupUIListeners()
    }

    private fun setupGoogleSignIn() {
        val googleSignLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            handleGoogleSignInResult(result)
        }
        binding.inicioGoogle.setOnClickListener {
            auth.signInWithGoogle(googleSignLauncher)
        }
    }

    private fun handleGoogleSignInResult(result: androidx.activity.result.ActivityResult) {
        val account = auth.handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(result.data))
        if (account.isSuccess()) {
            handleSuccessfulGoogleSignIn(account as AuthRes.Success<GoogleSignInAccount>)
        } else if (account.isError()) {
            showSnackbar("Error al iniciar sesión")
        }
    }

    private fun handleSuccessfulGoogleSignIn(account: AuthRes.Success<GoogleSignInAccount>) {
        val credential = GoogleAuthProvider.getCredential(account.data?.idToken, null)
        lifecycleScope.launch {
            when (val firebaseUser = auth.googleSignInCredential(credential)) {
                is AuthRes.Success -> launchMainActivity()
                is AuthRes.Error -> showSnackbar("Error al iniciar sesión")
            }
        }
    }

    private fun checkIfUserIsLoggedIn() {
        if (auth.getCurrentUser() != null) {
            launchMainActivity()
        }
    }

    private fun setupUIListeners() {
        with(binding) {
            tvRegistro.setOnClickListener {

                startActivity(Intent(this@LoginActivity, CrearCuenta::class.java))
            }
            btnInicioSesion.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                emailPassSignIn(email, password)
            }
            tvOlvidasteContrasena.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RecuperaContrasena::class.java))
            }
        }
    }

    private fun emailPassSignIn(email: String, password: String) {
        if (email.isNotBlank() && password.isNotBlank()) {
            lifecycleScope.launch {
                when (val result = auth.signInWithEmailAndPassword(email, password)) {
                    is AuthRes.Success -> launchMainActivity()
                    is AuthRes.Error -> showSnackbar("Error al iniciar sesión")
                }
            }
        }
    }

    private fun launchMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}
