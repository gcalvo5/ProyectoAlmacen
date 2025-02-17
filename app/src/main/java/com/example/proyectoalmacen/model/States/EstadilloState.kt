package com.example.proyectoalmacen.model.States

import com.example.proyectoalmacen.model.DataClasses.Estadillo

data class EstadilloState(
    val estadillos: List<Estadillo> = emptyList(),
    val isLoading: Boolean = false,
    )