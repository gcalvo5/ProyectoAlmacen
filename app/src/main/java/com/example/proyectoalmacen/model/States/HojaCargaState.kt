package com.example.proyectoalmacen.model.States

import com.example.proyectoalmacen.model.DataClasses.HojaCarga


data class HojaCargaState (
    val hojasCarga: List<HojaCarga> = emptyList(),
    val isLoading: Boolean = false,
)