package com.example.proyectoalmacen.model.States

import com.example.proyectoalmacen.model.DataClasses.Expedicion

data class ExpedicionState(
    val expediciones: List<Expedicion> = emptyList(),
    val isLoading: Boolean = false
)
