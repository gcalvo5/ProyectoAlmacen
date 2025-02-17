package com.example.proyectoalmacen.viewmodel.repositories

import androidx.compose.animation.core.copy

import com.example.proyectoalmacen.model.DataClasses.Bulto
import com.example.proyectoalmacen.model.DataClasses.Cliente
import com.example.proyectoalmacen.model.DataClasses.EstadoBulto
import com.example.proyectoalmacen.model.DataClasses.Expedicion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Singleton

@Singleton
class ExpedicionRepository @Inject constructor(private val bultoRepository: BultoRepository) {
    private val _expediciones = MutableStateFlow<List<Expedicion>>(
        listOf(
            Expedicion(idExpedicion = "1", idEstadillo = 1, provincia = "Valencia", cliente = Cliente(1, "Univar"), bultos = emptyList(), codPlaza = 2),
            Expedicion(idExpedicion = "2", idEstadillo = 1, provincia = "Murcia", cliente = Cliente(2, "Proquimia"), bultos = emptyList(), codPlaza = 5),
            Expedicion(idExpedicion = "3", idEstadillo = 1, provincia = "Valencia", cliente = Cliente(2, "Proquimia"), bultos = emptyList(), codPlaza = 2),
            Expedicion(idExpedicion = "4", idEstadillo = 1, provincia = "Albacete", cliente = Cliente(1, "Univar"), bultos = emptyList(), codPlaza = 4),
            Expedicion(idExpedicion = "5", idEstadillo = 1, provincia = "Alicante", cliente = Cliente(2, "Proquimia"), bultos = emptyList(), codPlaza = 3)
        )
    )
    val expediciones = _expediciones.asStateFlow()

    init {
        getAllExpediciones().onEach {
            _expediciones.value = it
        }.launchIn(CoroutineScope(Dispatchers.IO))
    }

    fun getExpedicionesByPlazas(codPlaza: Int): Flow<List<Expedicion>> {
        return expediciones.map { expediciones ->
            expediciones.filter { it.codPlaza == codPlaza }
        }.combine(bultoRepository.bultos) { expediciones, bultos ->
            expediciones.map { expedicion ->
                expedicion.copy(bultos = bultos.filter { it.idExpedicion == expedicion.idExpedicion })
            }
        }.let { calculateBultosInExpedicion(it) }
    }

    fun getExpedicionById(idExpedicion: String): Flow<List<Expedicion>> {
        return expediciones.map { expediciones ->
            expediciones.filter { it.idExpedicion.equals(idExpedicion) }
        }.combine(bultoRepository.bultos) { expediciones, bultos ->
            expediciones.map { expedicion ->
                expedicion.copy(bultos = bultos.filter { it.idExpedicion == expedicion.idExpedicion })
            }
        }.let { calculateBultosInExpedicion(it) }
    }

    fun getAllExpediciones(): Flow<List<Expedicion>> {
        return expediciones.combine(bultoRepository.bultos) { expediciones, bultos ->
            expediciones.map { expedicion ->
                expedicion.copy(bultos = bultos.filter { it.idExpedicion == expedicion.idExpedicion })
            }
        }.let {
            calculateBultosInExpedicion(it)
        }
    }

    fun updateExpedicion(updatedExpedicion: Expedicion) {
        val currentExpediciones = _expediciones.value.toMutableList()
        val index = currentExpediciones.indexOfFirst { it.idExpedicion == updatedExpedicion.idExpedicion }
        if (index != -1) {
            currentExpediciones[index] = updatedExpedicion
            _expediciones.value = currentExpediciones
        }
    }

    fun calculateBultosInExpedicion(expediciones: Flow<List<Expedicion>>): Flow<List<Expedicion>> {
        return expediciones.map { expedicionList ->
            expedicionList.map { expedicion ->
                var numBultos = 0
                var numBultosConfirmados = 0
                var numBultosRepasados = 0
                var numbultosCargados = 0
                expedicion.bultos.forEach { bulto ->
                    numBultos++
                    when (bulto.estadoBulto) {
                        EstadoBulto.DESCARGADO -> numBultosConfirmados++
                        EstadoBulto.REPASADO -> numBultosRepasados++
                        EstadoBulto.CARGADO -> numbultosCargados++
                        else -> {}
                    }
                }
                expedicion.copy(
                    numBultos = numBultos,
                    numBultosConfirmados = numBultosConfirmados,
                    numBultosRepasados = numBultosRepasados,
                    numbultosCargados = numbultosCargados
                )
            }
        }
    }

    fun getExpedicionesByIdCliente(idCliente: Int): Flow<List<Expedicion>>
    {
        return expediciones.map { expediciones ->
            expediciones.filter { it.cliente.idCliente == idCliente }
        }
    }
}