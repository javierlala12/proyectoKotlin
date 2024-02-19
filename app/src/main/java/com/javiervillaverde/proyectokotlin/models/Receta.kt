package com.javiervillaverde.proyectokotlin.models

/**
 * Clase modelo para las recetas
 * @property recetaId Identificador la receta
 * @property tituloReceta Título de la receta
 * @property descripcionReceta Descripción de la receta
 * @property ingredientes Lista de ingredientes de la receta
 * @property pasos Lista de pasos para preparar la receta
 * @property urlImagen URL de la imagen de la receta
 * @property autor ID del usuario que subió la receta
 * @property usuariosFavoritos Lista de IDs de usuarios que han marcado la receta como favorita
 */

data class Receta(
    var recetaId: String = "",
    val tituloReceta: String,
    val descripcionReceta: String,
    val ingredientes: List<String>,
    val pasos: List<String>,
    val urlImagen: String,
    var autor: String,
    val usuariosFavoritos: List<String>
) {
    operator fun set(position: Int, value: Receta) {

    }

    constructor() : this("", "", "", listOf(), listOf(), "", "", listOf())
}


