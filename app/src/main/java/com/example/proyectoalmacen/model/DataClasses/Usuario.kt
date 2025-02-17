package com.example.proyectoalmacen.model.DataClasses
enum class TipoUsuario{
    ADMIN,
    MOZO,
    CHOFER,
    CLIENTE
}
data class Usuario(
    val numUsuario: Int,
    val nombre: String,
    val tipoUsuario: TipoUsuario,
    val recogidas: List<Recogida> = emptyList(),
)
