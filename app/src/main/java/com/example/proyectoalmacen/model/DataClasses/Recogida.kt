package com.example.proyectoalmacen.model.DataClasses

data class Recogida (
    val idRecogida: Int,
    val clienteRecogida: Cliente,
    val conductorRecogida: Usuario
)