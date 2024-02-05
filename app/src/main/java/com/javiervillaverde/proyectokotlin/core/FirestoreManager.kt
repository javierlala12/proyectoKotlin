package com.javiervillaverde.proyectokotlin.core

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.javiervillaverde.proyectokotlin.App
import com.javiervillaverde.proyectokotlin.models.Receta
import com.google.firebase.auth.FirebaseUser
import com.javiervillaverde.proyectokotlin.models.Usuario

class FirestoreManager(context: Context) {
    private val firestore = FirebaseFirestore.getInstance()
    private val authManager = (context.applicationContext as App).auth
    private val userId = authManager.getCurrentUser()?.uid

    // Constantes para nombres de colección
    companion object {
        private const val COLECCION_RECETAS = "recetas"
        private const val COLECCION_USUARIOS = "usuarios"
    }

    /**
     * Obtiene el ID del usuario actualmente autenticado.
     */
    fun getUserId(): String? = userId

    /**
     * Guarda la información del usuario en Firestore.
     */
    fun saveUserInformation(user: FirebaseUser, onComplete: (Boolean, String) -> Unit) {
        val usuario = Usuario(
            userId = user.uid,
            email = user.email ?: "",
            imagenPerfil = user.photoUrl?.toString() ?: "",
            nombreUsuario = user.displayName ?: "",
            recetasFavoritas = listOf(),
            recetasSubidas = listOf()
        )

        firestore.collection(COLECCION_USUARIOS)
            .document(user.uid)
            .set(usuario)
            .addOnSuccessListener {
                onComplete(true, "Información del usuario guardada con éxito.")
            }
            .addOnFailureListener { e ->
                onComplete(false, e.message ?: "Error al guardar la información del usuario.")
            }
    }

    /**
     * Agrega una nueva receta a Firestore.
     */
    fun addReceta(receta: Receta, onComplete: (Boolean, String) -> Unit) {
        firestore.collection(COLECCION_RECETAS)
            .add(receta)
            .addOnSuccessListener { documentReference ->
                val generatedId = documentReference.id
                firestore.collection(COLECCION_RECETAS).document(generatedId)
                    .update("recetaId", generatedId)
                    .addOnSuccessListener {
                        // Ahora el documento en Firestore tiene un campo 'recetaId' con su ID
                        onComplete(true, generatedId)
                    }
            .addOnFailureListener { e ->
                onComplete(false, e.message ?: "Error al añadir la receta.")
            }
    }}

    /**
     * Recupera todas las recetas del usuario actualmente autenticado.
     */
    fun getRecetasDelUsuario(userId: String, onComplete: (List<Receta>?) -> Unit) {
        firestore.collection(COLECCION_RECETAS)
            .whereEqualTo("autor", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val recetasList = querySnapshot.toObjects(Receta::class.java)
                onComplete(recetasList)
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    /**
     * Actualiza una receta específica en Firestore.
     */
    fun updateReceta(recetaId: String, camposModificados: Map<String, Any>, onComplete: (Boolean, String) -> Unit) {
        firestore.collection(COLECCION_RECETAS).document(recetaId)
            .update(camposModificados)
            .addOnSuccessListener {
                onComplete(true, "Receta actualizada con éxito.")
            }
            .addOnFailureListener { e ->
                onComplete(false, "Error al actualizar la receta: ${e.message}")
            }
    }

    /**
     * Elimina una receta específica de Firestore.
     */
    fun deleteReceta(recetaId: String, onComplete: (Boolean, String) -> Unit) {
        if (recetaId.isBlank()) {
            onComplete(false, "El ID de la receta está vacío.")
            return
        }
        val recetaDocument = firestore.collection(COLECCION_RECETAS).document(recetaId)

        recetaDocument.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                recetaDocument.delete().addOnSuccessListener {
                    onComplete(true, "Receta eliminada con éxito.")
                }.addOnFailureListener { e ->
                    onComplete(false, "Error al eliminar la receta: ${e.message}")
                }
            } else {
                onComplete(false, "La receta con ID $recetaId no existe.")
            }
        }.addOnFailureListener { e ->
            onComplete(false, "Error al obtener la receta: ${e.message}")
        }
    }

    fun getReceta(recetaId: String, any: Any) {

    }
}
