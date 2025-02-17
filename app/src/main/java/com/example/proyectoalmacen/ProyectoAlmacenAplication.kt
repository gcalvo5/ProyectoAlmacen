package com.example.proyectoalmacen

import android.app.Application
import com.example.proyectoalmacen.core.preferences.Preferens
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ProyectoAlmacenAplication: Application(){
    companion object{
        lateinit var preferens: Preferens
    }
    override fun onCreate() {
        super.onCreate()
        preferens = Preferens(applicationContext)
    }
}