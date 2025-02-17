package com.example.proyectoalmacen.viewmodel.repositories


import com.example.proyectoalmacen.model.DataClasses.Bulto
import com.example.proyectoalmacen.model.DataClasses.EstadoBulto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BultoRepository @Inject constructor() {
    private val _bultos = MutableStateFlow<List<Bulto>>(
        listOf(
            Bulto(idBulto = "00TLNKC354C9Bff1A5B8", idExpedicion = "2", estadoBulto = EstadoBulto.DESCARGADO),
            Bulto(idBulto = "00TLNKC62C89B82BB835", idExpedicion = "3", estadoBulto = EstadoBulto.DESCARGADO),
            Bulto(idBulto = "00TLNKC354C9BD01A348", idExpedicion = "1", estadoBulto = EstadoBulto.DESCARGADO),
            Bulto(idBulto = "00TLNKC62C89B82BB555", idExpedicion = "5", estadoBulto = EstadoBulto.DESCARGADO),
            Bulto(idBulto = "00TLNKC354C9BD01A668", idExpedicion = "5", estadoBulto = EstadoBulto.DESCARGADO),
            Bulto(idBulto = "00TLNKC62C89B8277835", idExpedicion = "4", estadoBulto = EstadoBulto.DESCARGADO),
            Bulto(idBulto = "00TLNKC354C9BD0885B8", idExpedicion = "5", estadoBulto = EstadoBulto.DESCARGADO),
            Bulto(idBulto = "00TLNKC62C89B8299835", idExpedicion = "2", estadoBulto = EstadoBulto.DESCARGADO),
            Bulto(idBulto = "00TLNKC354C9BD00A5B8", idExpedicion = "4", estadoBulto = EstadoBulto.DESCARGADO),
            Bulto(idBulto = "00TLNKC354C9BD01A5B8", idExpedicion = "3", estadoBulto = EstadoBulto.DESCARGADO),
            Bulto(idBulto = "00TLNKC354C9BD22A5B8", idExpedicion = "2", estadoBulto = EstadoBulto.DESCARGADO),
            Bulto(idBulto = "00TLNKC62C89B833B835", idExpedicion = "1", estadoBulto = EstadoBulto.DESCARGADO),
        )
    )
    val bultos = _bultos.asStateFlow()

    fun getBultosByExpedicion(idExpedicion: String): Flow<List<Bulto>> {
        return bultos.map { bultos ->
            bultos.filter { it.idExpedicion == idExpedicion }
        }
    }

    fun getBultosByidBulto(idBulto: String): Flow<List<Bulto>> {
        return bultos.map { bultos ->
            bultos.filter { it.idBulto == idBulto }
        }
    }

    fun updateBulto(updatedBulto: Bulto) {
        //println("Updating bulto: ${updatedBulto.idBulto}")
        val currentBultos = _bultos.value.toMutableList()
        //println("Current bultos: $currentBultos")
        val index = currentBultos.indexOfFirst { it.idBulto == updatedBulto.idBulto }
        //println("Index of bulto to update: $index")
        if (index != -1) {
            currentBultos[index] = updatedBulto.copy()
            _bultos.value = currentBultos.toList()
            //println("Updated bultos: ${_bultos.value}")
        }
    }
}