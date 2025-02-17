package com.example.proyectoalmacen.model.DataClasses

data class Cliente(
    val idCliente: Int,
    val nombreCliente: String,
    val direccion: String = "",
    val poblacion: String = "",
    val provincia: String = "",
    val codigoPostal: String = "",
)
