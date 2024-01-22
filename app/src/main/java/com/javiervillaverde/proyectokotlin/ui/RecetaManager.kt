package com.javiervillaverde.proyectokotlin.ui

import com.javiervillaverde.proyectokotlin.model.Receta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecetaManager {
    companion object {
        val recetas = mutableListOf<Receta>(
            Receta(
                "Macarrones carbonara",
                "https://www.divinacocina.es/wp-content/uploads/macarrones-carbonara-3.jpg",
                "Una receta clásica italiana que combina la cremosidad del huevo con la salinidad del tocino y la bondad del queso parmesano.",
                listOf(
                    "1. Cocina los macarrones en agua con sal hasta que estén al dente.",
                    "2. En una sartén, cocina el tocino hasta que esté crujiente. Reserva la grasa.",
                    "3. En un tazón aparte, mezcla y bate huevos con queso parmesano rallado.",
                    "4. Escurre los macarrones y agrégales la grasa del tocino. Mezcla bien.",
                    "5. Agrega la mezcla de huevo y queso a los macarrones, revolviendo rápidamente para que la salsa se adhiera.",
                    "6. Agrega el tocino crujiente por encima y espolvorea con más queso parmesano si lo deseas.",
                    "7. Sirve caliente y disfruta de estos deliciosos macarrones carbonara."
                )
            ),
            Receta(
                "Pollo al curry",
                "https://www.divinacocina.es/wp-content/uploads/2017/07/pollo-al-curry-cazuela-arroz.jpg.webp",
                "Una receta deliciosa de pollo con una mezcla de especias al curry.",
                listOf("Marinar el pollo con curry", "Cocinar el pollo en salsa de curry")
            ),
            Receta(
                "Ensalada César",
                "https://www.divinacocina.es/wp-content/uploads/ensalada-cesar-rapida-facil.jpg",
                "Una ensalada clásica con aderezo César y crutones.",
                listOf("Preparar la lechuga y los ingredientes", "Agregar aderezo y mezclar")
            ),
            Receta(
                "Lasaña de carne",
                "https://www.divinacocina.es/wp-content/uploads/lasana-de-patatas-plato.jpg",
                "Una lasaña tradicional con capas de carne, queso y salsa de tomate.",
                listOf("Cocinar la carne y la salsa", "Armar las capas de lasaña")
            ),
            Receta(
                "Tacos de carnitas",
                "https://www.divinacocina.es/wp-content/uploads/2022/12/TACO-POCKETS-PIZZA-C.jpg.webp",
                "Tacos mexicanos con cerdo desmenuzado, cebolla y cilantro.",
                listOf("Cocinar las carnitas", "Armar los tacos con cebolla y cilantro")
            ),
            Receta(
                "Pastel de chocolate",
                "https://www.divinacocina.es/wp-content/uploads/pastel-chocolate-microndas-2.jpg",
                "Un postre delicioso con capas de bizcocho de chocolate y glaseado.",
                listOf("Hornear el bizcocho", "Cubrir con glaseado de chocolate")
            ),
            Receta(
                "Paella española",
                "https://www.divinacocina.es/wp-content/uploads/paella-marinera-1.jpg",
                "Un plato español con arroz, mariscos y azafrán.",
                listOf("Preparar el sofrito", "Cocinar el arroz con mariscos")
            ),
            Receta(
                "Hamburguesa clásica",
                "https://www.divinacocina.es/wp-content/uploads/hamburguesa-vegetariana.jpg",
                "Una hamburguesa jugosa con queso, lechuga y tomate.",
                listOf("Preparar la carne de hamburguesa", "Ensamblar con queso y vegetales")
            ),
            Receta(
                "Pasta primavera",
                "https://danzadefogones.com/wp-content/uploads/2018/03/Pasta-primavera-2.jpg",
                "Pasta fresca con una variedad de verduras de temporada.",
                listOf("Cocinar la pasta al dente", "Saltear las verduras y mezclar con la pasta")
            )
        )

        suspend fun addRecipe(receta: Receta) {
            withContext(Dispatchers.IO) {
                recetas.add(receta)
            }
        }

        suspend fun deleteRecipe(position: Int) {
            withContext(Dispatchers.IO) {
                recetas.removeAt(position)
            }
        }

        suspend fun modifyRecipe(position: Int, receta: Receta) {
            withContext(Dispatchers.IO) {
                recetas[position] = receta
            }
        }
    }
}