package com.javiervillaverde.proyectokotlin

import android.app.Application
import com.javiervillaverde.proyectokotlin.core.AuthManager


class App: Application() {
    lateinit var auth: AuthManager


    override fun onCreate() {
        super.onCreate()
        auth = AuthManager(this)
    }
}
