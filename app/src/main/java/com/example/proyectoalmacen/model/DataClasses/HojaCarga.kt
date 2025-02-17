package com.example.proyectoalmacen.model.DataClasses

data class HojaCarga (
    val numHojaCarga: Int,
    val conductor: Int = 0,
    val muelle: Int,
    val idUsuario: Int,
    val expediciones: List<Expedicion> = emptyList(),
    val numBultosTotal: Int = 0,
    val numBultosCargados: Int = 0,
    val clientes:List<Cliente> = emptyList(),
    val fechaCreacion: String = "",
    val fechaFinalizacion: String = "",
    val tiempoCreacion: String = "",
    val tiempoFinalizacion: String = "",
    val finalizado: Boolean = false,
    val incidencias: List<Incidencia> = emptyList(),
    val idPlazas:List<Int>
)