package com.example.proyectoalmacen.model.DataClasses

enum class TipoBulto{
    PALET,
    BULTO,
    BIDON,
    CONTENEDOR,
    ROLLO
}
enum class EstadoBulto{
    CREADO,
    DESCARGADO,
    REPASADO,
    CARGADO,
}
data class Bulto(
    val idBulto: String,
    val dateConfirmado: String = "",
    val timeConfirmado: String = "",
    val peso:Double = 0.0,
    val volumen:Double = 0.0,
    val idExpedicion: String,
    val tipoBulto: TipoBulto = TipoBulto.PALET,
    val bultosDentroDePalet: Int = 0,
    var estadoBulto: EstadoBulto = EstadoBulto.CREADO
)
