package com.example.proyectoalmacen.model.DataClasses

enum class EstadoEstadillo{
    CREADO,
    DESCARGADO,
    REPASADO,
    CARGADO,
}

data class Estadillo(
    val numEst: Int,
    val conductor: Usuario,
    val muelle: Int,
    val usuario: Usuario,
    val date: String = "",
    val time: String = "",
    var expediciones: List<Expedicion> = emptyList(),
    var numBultosTotal: Int = 0,
    var numBultosConfirmados: Int = 0,
    val numBultosNoConfirmados: Int = 0,
    var numBultosRepasados: Int = 0,
    var numBultosCargados: Int = 0,
    val clientes:List<Cliente> = emptyList(),
    val fechaCreacion: String,
    val fechaFinalizacion: String = "",
    val tiempoCreacion: String,
    val tiempoFinalizacion: String = "",
    val estadoEstadillo: EstadoEstadillo = EstadoEstadillo.CREADO,
    val incidencias: List<Incidencia> = emptyList()
)
