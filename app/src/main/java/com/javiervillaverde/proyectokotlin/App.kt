package com.javiervillaverde.proyectokotlin

import android.app.Application
import com.javiervillaverde.proyectokotlin.core.AuthManager
import com.javiervillaverde.proyectokotlin.core.FirestoreManager

class App: Application() {
    lateinit var auth: AuthManager
    lateinit var firestore: FirestoreManager

    override fun onCreate() {
        super.onCreate()
        auth = AuthManager(this)
        firestore = FirestoreManager(this)
    }
}
