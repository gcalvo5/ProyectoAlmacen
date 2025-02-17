package com.example.proyectoalmacen.model.States

import com.example.proyectoalmacen.model.DataClasses.Plazas

data class PlazasState(
    val plazas: List<Plazas> = emptyList(),
    val isLoading: Boolean = false
)
