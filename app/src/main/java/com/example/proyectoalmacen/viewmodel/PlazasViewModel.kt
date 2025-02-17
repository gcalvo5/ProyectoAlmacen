package com.example.proyectoalmacen.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectoalmacen.model.DataClasses.Plazas
import com.example.proyectoalmacen.model.States.PlazasState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PlazasViewModel @Inject constructor() : ViewModel() {
    var plazasState by mutableStateOf(PlazasState())
    private set
    init{
        plazasState = plazasState.copy(
            isLoading = true
        )
        plazasState = plazasState.copy(
            plazas = listOf(
                Plazas(1, "Castellon"),
                Plazas(2, "Valencia"),
                Plazas(3, "Alicante"),
                Plazas(4, "Albacete"),
                Plazas(5, "Murcia"),
                Plazas(6, "Almeria"),
                Plazas(7, "Granada"),
                Plazas(8, "Malaga"),
                Plazas(9, "Jaen"),
                Plazas(10, "Cordoba"),
                Plazas(11, "Sevilla"),
                Plazas(12, "Cadiz"),
                Plazas(13, "Huelva"),
                Plazas(14, "Extremadura"),
                Plazas(15, "Toledo"),
                Plazas(16, "Madrid"),
                Plazas(17, "Zaragoza"),
                Plazas(18, "Salamanca"),
                Plazas(19, "Bilbao"),
            ),
            isLoading = false
        )
    }
    fun getPlazaById(id:Int): Plazas {
        return plazasState.plazas.first { it.codPlazas == id }
    }
    fun getPlazaByNombre(nombre:String): Plazas {
        return plazasState.plazas.first { it.nombrePlaza.equals(nombre) }
    }
}