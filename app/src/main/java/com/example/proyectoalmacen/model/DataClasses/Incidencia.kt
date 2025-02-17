package com.example.proyectoalmacen.model.DataClasses

enum class TipoIncidencia{
    FALTA,
    SOBRA,
    ROTURA,
    BLOQUEO
}

data class Incidencia(
    val idIncidencia: Int,
    val descripcion: String = "",
    val expedicion: Expedicion,
    val estadillo: Estadillo,
    val tipoIncidencia: TipoIncidencia
)
