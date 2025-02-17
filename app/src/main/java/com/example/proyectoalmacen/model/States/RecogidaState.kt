package com.example.proyectoalmacen.model.States

import com.example.proyectoalmacen.model.DataClasses.Recogida

data class RecogidaState(
    val recogidas: List<Recogida> = emptyList(),
    val isLoading: Boolean = false
)
