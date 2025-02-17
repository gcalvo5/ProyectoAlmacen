package com.example.proyectoalmacen.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class Estadillo(
    val numEstadillo: Int,
    val nombreChofer: String,
    @Transient val calculatedValue: Int = 0,
)

@Serializable
data class Repaso(
    val numPlaza: Int,
    @Transient val calculatedValue: Int = 0,
)

@Serializable
data class Carga(
    val plazas: List<Int>,
    @Transient val calculatedValue: Int = 0,
)