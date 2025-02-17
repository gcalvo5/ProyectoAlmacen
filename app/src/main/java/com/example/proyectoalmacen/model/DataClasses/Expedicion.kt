package com.example.proyectoalmacen.model.DataClasses

enum class EstadoExpedicion{
    PENDIENTE,
    CONFIRMADO,
    REPASADO,
    ENTREGADO
}

data class Expedicion(
    val idExpedicion: String,
    var numBultos: Int = 0,
    var numBultosConfirmados: Int = 0,
    val numBultosNoConfirmados: Int = 0,
    var numBultosRepasados: Int = 0,
    var numbultosCargados: Int = 0,
    val cliente: Cliente,
    val bultos: List<Bulto> = emptyList(),
    val driver: String = "",
    val dateConfirmado: String = "",
    val timeConfirmado: String= "",
    val pesoTotal: Double = 0.0,
    val volumenTotal: Double = 0.0,
    val poblacion: String = "",
    val provincia: String = "",
    val codPlaza: Int = 0,
    val codigoPostal: String = "",
    val direccionDestinatario: String = "",
    val referenciaCliente: String = "",
    val idEstadillo: Int = 0,
    val estadoExpedicion: EstadoExpedicion = EstadoExpedicion.PENDIENTE,
    val incidencia:Incidencia? = null
)
