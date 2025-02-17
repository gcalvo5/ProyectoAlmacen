package com.example.proyectoalmacen.model.States

import com.example.proyectoalmacen.model.DataClasses.Bulto

data class BultoState(
    val bultos:List<Bulto> = emptyList(),
    val isLoading: Boolean = false,
    )
