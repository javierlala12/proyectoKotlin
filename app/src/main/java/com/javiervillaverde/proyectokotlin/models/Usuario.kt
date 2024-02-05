package com.javiervillaverde.proyectokotlin.models

data class Usuario(
    val userId: String, // O asumiendo que usas el ID proporcionado por Firebase Auth
    val email: String,
    val imagenPerfil: String,
    val nombreUsuario: String,
    val recetasFavoritas: List<String>, // IDs de recetas favoritas
    val recetasSubidas: List<String> // IDs de recetas subidas por el usuario
) {
    constructor() : this("", "", "", "", listOf(), listOf())
}
