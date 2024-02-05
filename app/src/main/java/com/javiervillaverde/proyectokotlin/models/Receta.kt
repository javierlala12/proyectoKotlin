package com.javiervillaverde.proyectokotlin.models



data class Receta(
    var recetaId: String = "",
    val tituloReceta: String,
    val descripcionReceta: String,
    val ingredientes: List<String>,
    val pasos: List<String>,
    val urlImagen: String,
    var autor: String, // ID del usuario que subió la receta
    val usuariosFavoritos: List<String> // Lista de IDs de usuarios que han marcado la receta como favorita
) {
    operator fun set(position: Int, value: Receta) {

    }

    constructor() : this("", "", "", listOf(), listOf(), "", "", listOf())
}


