package com.javiervillaverde.proyectokotlin.core


import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.javiervillaverde.proyectokotlin.R
import kotlinx.coroutines.tasks.await

sealed class AuthRes<out T> {
    data class Success<T>(val data: T) : AuthRes<T>()
    data class Error(val errorMessage: String) : AuthRes<Nothing>()

    fun isSuccess() = this is Success<T>
    fun isError() = this is Error
}

class AuthManager(context: Context) {
    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }

    /**
     * 1. Crear un usuario con correo y contraseña
     * 2. Iniciar sesión con correo y contraseña
     * 3. Restablecer contraseña
     * 4. Cerrar sesión
     * 5. Obtener el usuario actual
     * 6. Iniciar sesión con Google
    * */

    /**
     * 1. Crear un usuario con correo y contraseña
     * @param email correo del usuario
     * @param password contraseña del usuario
     * */
    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): AuthRes<FirebaseUser?> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            AuthRes.Success(authResult.user)
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al crear el usuario")
        }
    }

    /**
     * 2. Iniciar sesión con correo y contraseña
     *@param email correo del usuario
     * @param password contraseña del usuario
     * */
    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): AuthRes<FirebaseUser?> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            AuthRes.Success(authResult.user)
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al iniciar sesión")
        }
    }

    /**
     * 3. Restablecer contraseña
     * @param email correo del usuario
     * */
    suspend fun resetPassword(email: String): AuthRes<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            AuthRes.Success(Unit)
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al enviar el correo")
        }
    }

    /**
     * 4. Cerrar sesión
     * */

    fun signOut() {
        firebaseAuth.signOut()
        googleSignInClient.signOut()
    }

    /**
     * 5. Obtener el usuario actual
     * */

    fun getCurrentUser() = firebaseAuth.currentUser

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    /**
     * 6.Maneja el inicio de sesión con Google
     * */

    fun handleSignInResult(task: Task<GoogleSignInAccount>): AuthRes<GoogleSignInAccount?> {
        return try {
            val account = task.getResult(ApiException::class.java)
            AuthRes.Success(account)
        } catch (e: ApiException) {
            AuthRes.Error(e.message ?: "Error al iniciar sesión con Google")
        }
    }

    suspend fun googleSignInCredential(credential: AuthCredential): AuthRes<FirebaseUser?> {
        return try {
            val firebaseUser = firebaseAuth.signInWithCredential(credential).await()
            firebaseUser.user?.let {
                AuthRes.Success(it)
            } ?: throw Exception("User is null")
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al iniciar sesión con Google")
        }
    }


    /**
     * 6. Iniciar sesión con Google
     * */
    fun signInWithGoogle(googleSignInLauncher: ActivityResultLauncher<Intent>) {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

}
