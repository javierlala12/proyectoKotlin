package com.javiervillaverde.proyectokotlin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class RecipeProvider {
    companion object {
        suspend fun getRecetas(): List<Receta> {
            delay(2000)
            return withContext(Dispatchers.IO) {
                RecetaManager.recetas.toList()
            }
        }
    }
}
