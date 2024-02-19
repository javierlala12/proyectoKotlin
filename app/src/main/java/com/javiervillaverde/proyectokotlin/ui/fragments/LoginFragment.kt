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
import com.javiervillaverde.proyectokotlin.core.FirestoreManager
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

    /**
     * Método que se encarga de configurar el inicio de sesión con Google
     * Se encarga de configurar el botón de inicio de sesión con Google
     * Se encarga de manejar el resultado de la autenticación con Google
     */

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

    /**
     * Métodos que se encarga de manejar el resultado de la autenticación con Google
     * Se encarga de manejar el resultado de la autenticación con Google
     * Si la autenticación es exitosa, se obtiene el usuario de Firebase y se guarda en Firestore
     * Si la autenticación es exitosa y el usuario ya existe en Firestore, se lanza la actividad principal
     * Si la autenticación es exitosa y el usuario no existe en Firestore, se guarda el usuario en Firestore y se lanza la actividad principal
     * Si la autenticación no es exitosa, se muestra un mensaje de error
     */
    private fun handleGoogleSignInResult(result: androidx.activity.result.ActivityResult) {
        val account =
            auth.handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(result.data))
        if (account.isSuccess()) {
            handleSuccessfulGoogleSignIn(account as AuthRes.Success<GoogleSignInAccount>)
        } else if (account.isError()) {
            showSnackbar("Error al iniciar sesión")
        }
    }

    private fun handleSuccessfulGoogleSignIn(account: AuthRes.Success<GoogleSignInAccount>) {
        val credential = GoogleAuthProvider.getCredential(account.data.idToken, null)
        lifecycleScope.launch {
            when (val firebaseUserResult = auth.googleSignInCredential(credential)) {
                is AuthRes.Success -> {
                    firebaseUserResult.data?.let { firebaseUser ->
                        val firestoreManager = FirestoreManager(this@LoginActivity)
                        firestoreManager.getUsuarioPorId(firebaseUser.uid) { usuario ->
                            if (usuario == null) {
                                firestoreManager.saveUserInformation(firebaseUser) { success, message ->
                                    if (success) {
                                        launchMainActivity()
                                    } else {
                                        showSnackbar("Error al guardar la información del usuario: $message")
                                    }
                                }
                            } else {
                                launchMainActivity()
                            }
                        }
                    }
                }

                is AuthRes.Error -> showSnackbar("Error al iniciar sesión con Google: ${firebaseUserResult.errorMessage}")
            }
        }
    }

    /**
     * Método que se encarga de verificar si el usuario ya está logueado
     * Se encarga de verificar si el usuario ya está logueado
     * Si el usuario ya está logueado, se lanza la actividad principal
     */


    private fun checkIfUserIsLoggedIn() {
        if (auth.getCurrentUser() != null) {
            launchMainActivity()
        }
    }

    /**
     * Método que se encarga de configurar los listeners de la interfaz de usuario
     * Se encarga de configurar los listeners de la interfaz de usuario
     * Se encarga de manejar el evento de clic en el botón de inicio de sesión
     * Se encarga de manejar el evento de clic en el botón de registro
     * Se encarga de manejar el evento de clic en el botón de recuperar contraseña
     */

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

    /**
     * Método que se encarga de iniciar sesión con correo y contraseña
     * Se encarga de iniciar sesión con correo y contraseña
     * Si el correo y la contraseña no están vacíos, se inicia sesión con correo y contraseña
     * Si la autenticación es exitosa, se obtiene el usuario de Firebase y se guarda en Firestore
     * Si la autenticación es exitosa y el usuario ya existe en Firestore, se lanza la actividad principal
     * Si la autenticación es exitosa y el usuario no existe en Firestore, se guarda el usuario en Firestore y se lanza la actividad principal
     * Si la autenticación no es exitosa, se muestra un mensaje de error
     */

    private fun emailPassSignIn(email: String, password: String) {
        if (email.isNotBlank() && password.isNotBlank()) {
            lifecycleScope.launch {
                when (val result = auth.signInWithEmailAndPassword(email, password)) {
                    is AuthRes.Success -> {
                        result.data?.let { firebaseUser ->
                            val firestoreManager = FirestoreManager(this@LoginActivity)
                            firestoreManager.saveUserInformation(firebaseUser) { success, message ->
                                if (success) {
                                    launchMainActivity()
                                } else {
                                    showSnackbar(message)
                                }
                            }
                        }
                    }

                    is AuthRes.Error -> showSnackbar("Error al iniciar sesión: ${result.errorMessage}")
                }
            }
        }
    }

    /**
     * Método que se encarga de lanzar la actividad principal
     */
    private fun launchMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}
