package com.javiervillaverde.proyectokotlin.core

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.javiervillaverde.proyectokotlin.App
import com.javiervillaverde.proyectokotlin.models.Receta
import com.google.firebase.auth.FirebaseUser
import com.javiervillaverde.proyectokotlin.models.Usuario

class FirestoreManager(context: Context) {
    private val firestore = FirebaseFirestore.getInstance()
    private val authManager = (context.applicationContext as App).auth
    private val userId = authManager.getCurrentUser()?.uid

    companion object {
        private const val COLECCION_RECETAS = "recetas"
        private const val COLECCION_USUARIOS = "usuarios"
    }

    /**
     * Obtiene el ID del usuario actualmente autenticado.
     * @return ID del usuario o null si no está autenticado.
     */
    fun getUserId(): String? = userId

    /**
     * Guarda la información del usuario en Firestore.
     *@param user Usuario de Firebase con información de autenticación.
     *@param onComplete Callback para manejar el resultado de la operación.
     */
    fun saveUserInformation(user: FirebaseUser, onComplete: (Boolean, String) -> Unit) {
        val email = user.email ?: ""
        val nombreUsuario = email.substringBefore('@')
        val usuario = Usuario(
            userId = user.uid,
            email = email,
            imagenPerfil = "https://cdn.pixabay.com/photo/2014/04/03/00/42/chef-hat-309146_1280.png",
            nombreUsuario = nombreUsuario,
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
     * Obtiene la información de un usuario por su ID.
     * @param userId ID del usuario a buscar.
     * @param onComplete Callback con el usuario encontrado o null.
     */

    fun getUsuarioPorId(userId: String, onComplete: (Usuario?) -> Unit) {
        firestore.collection("usuarios").document(userId).get()
            .addOnSuccessListener { documento ->
                val usuario = documento.toObject(Usuario::class.java)
                onComplete(usuario)
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    /**
     * Actualiza la información de un usuario en Firestore.
     * @param userId ID del usuario a actualizar.
     * @param camposModificados Mapa con los campos a actualizar.
     * @param onComplete Callback para manejar el resultado de la operación.
     */
    fun updateUsuario(
        userId: String,
        camposModificados: Map<String, Any>,
        onComplete: (Boolean, String) -> Unit
    ) {
        firestore.collection(COLECCION_USUARIOS)
            .document(userId)
            .update(camposModificados)
            .addOnSuccessListener {
                onComplete(true, "Información del usuario actualizada con éxito.")
            }
            .addOnFailureListener { e ->
                onComplete(
                    false,
                    e.localizedMessage
                        ?: "Error desconocido al actualizar la información del usuario."
                )
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
                        onComplete(true, generatedId)
                    }
                    .addOnFailureListener { e ->
                        onComplete(false, e.message ?: "Error al añadir la receta.")
                    }
            }
    }

    /**
     * Recupera todas las recetas del usuario actualmente autenticado.
     *@param receta Objeto Receta a agregar.
     *@param onComplete Callback con el resultado de la operación.
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
     * @param recetaId ID de la receta a actualizar.
     * @param camposModificados Mapa con los campos a actualizar.
     * @param onComplete Callback para manejar el resultado de la operación.
     */
    fun updateReceta(
        recetaId: String,
        camposModificados: Map<String, Any>,
        onComplete: (Boolean, String) -> Unit
    ) {
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
     * @param recetaId ID de la receta a eliminar.
     * @param onComplete Callback para manejar el resultado de la operación.
     * @return vuelve a la pantalla de inicio
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

    /**
     * Obtiene una receta específica por su ID.
     * @param recetaId ID de la receta a buscar.
     * @param onComplete Callback con la receta encontrada o null.
     */
    fun getReceta(recetaId: String, onComplete: (Receta?) -> Unit) {
        firestore.collection(COLECCION_RECETAS).document(recetaId).get()
            .addOnSuccessListener { documentSnapshot ->
                val receta = documentSnapshot.toObject(Receta::class.java)
                onComplete(receta)
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    /**
     * Agrega o elimina una receta de la lista de favoritos del usuario.
     * @param recetaId ID de la receta a agregar o eliminar.
     * @param userId ID del usuario actualmente autenticado.
     * @param onComplete Callback con el resultado de la operación.
     */

    fun toggleFavorito(recetaId: String, userId: String, onComplete: (Boolean) -> Unit) {
        val recetaRef = firestore.collection(COLECCION_RECETAS).document(recetaId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(recetaRef)
            val favoritos =
                snapshot.get("usuariosFavoritos") as? MutableList<String> ?: mutableListOf()

            if (favoritos.contains(userId)) {
                favoritos.remove(userId)
            } else {
                favoritos.add(userId)
            }

            transaction.update(recetaRef, "usuariosFavoritos", favoritos)
            true // Indica éxito
        }.addOnSuccessListener {
            onComplete(true)
        }.addOnFailureListener {
            onComplete(false)
        }
    }

    /**
     * Obtiene las recetas favoritas del usuario que esta utilizando la app en esos momentos.
     * @param userId ID del usuario que esta utilizando la app en esos momentos.
     * @param onComplete Callback con la lista de recetas favoritas o null.
     */

    fun getRecetasFavoritas(userId: String, onComplete: (List<Receta>?) -> Unit) {
        firestore.collection(COLECCION_RECETAS)
            .whereArrayContains("usuariosFavoritos", userId)
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
     * Obtiene todas las recetas de Firestore.
     * @param onComplete Callback con la lista de recetas o null.
     */

    fun getRecetasHome(onComplete: (List<Receta>?) -> Unit) {
        firestore.collection(COLECCION_RECETAS)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val recetasList =
                    querySnapshot.documents.mapNotNull { it.toObject(Receta::class.java) }

                val sortedRecetasList = recetasList.sortedByDescending { it.usuariosFavoritos.size }
                onComplete(sortedRecetasList)
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    /**
     * Busca recetas en Firestore por título o ingredientes.
     * @param query Texto a buscar en el título o ingredientes de las recetas.
     * @param onComplete Callback con la lista de recetas encontradas o null.
     */
    fun buscarRecetas(query: String, onComplete: (List<Receta>?) -> Unit) {
        val recetasRef = firestore.collection("recetas")

        //Buscar por título exacto
        recetasRef.whereEqualTo("tituloReceta", query).get().addOnSuccessListener { documents ->
            val recetasPorNombre = documents.toObjects(Receta::class.java)

            // Buscar por ingredientes
            recetasRef.whereArrayContainsAny("ingredientes", listOf(query)).get()
                .addOnSuccessListener { documentsIngredientes ->
                    val recetasPorIngredientes = documentsIngredientes.toObjects(Receta::class.java)
                    // Combinar resultados y eliminar duplicados
                    val resultadosCombinados =
                        (recetasPorNombre + recetasPorIngredientes).distinctBy { it.recetaId }

                    onComplete(resultadosCombinados)
                }.addOnFailureListener { e ->

                onComplete(null)
            }
        }.addOnFailureListener { e ->

            onComplete(null)
        }
    }


}
