package com.javiervillaverde.proyectokotlin.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Receta(val nombre: String, val imageUrl: String, val descripcion: String, val pasos: List<String>) : Parcelable

