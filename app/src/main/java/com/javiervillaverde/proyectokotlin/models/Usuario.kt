package com.javiervillaverde.proyectokotlin.models

/**
 * Clase modelo para los usuarios
 * @property userId Identificador del usuario
 * @property email Correo electrónico del usuario
 * @property imagenPerfil URL de la imagen de perfil del usuario
 * @property nombreUsuario Nombre del usuario
 * @property recetasSubidas Lista de IDs de recetas subidas por el usuario
 */
data class Usuario(
    val userId: String, // O asumiendo que usas el ID proporcionado por Firebase Auth
    val email: String,
    val imagenPerfil: String,
    val nombreUsuario: String,
    val recetasSubidas: List<String> // IDs de recetas subidas por el usuario
) {
    constructor() : this("", "", "", "", listOf())
}
