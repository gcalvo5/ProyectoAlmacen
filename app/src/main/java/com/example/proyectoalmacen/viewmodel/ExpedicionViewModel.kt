package com.example.proyectoalmacen.viewmodel

import androidx.activity.result.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoalmacen.model.DataClasses.Bulto
import com.example.proyectoalmacen.model.DataClasses.Expedicion
import com.example.proyectoalmacen.model.States.ExpedicionState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.proyectoalmacen.model.DataClasses.Cliente
import com.example.proyectoalmacen.model.DataClasses.EstadoBulto
import com.example.proyectoalmacen.viewmodel.repositories.BultoRepository
import com.example.proyectoalmacen.viewmodel.repositories.ExpedicionRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class ExpedicionViewModel @Inject constructor(
    private val expedicionRepository: ExpedicionRepository,
    private val bultoRepository: BultoRepository
) : ViewModel() {
    var expedicionState by mutableStateOf(ExpedicionState())
        private set

    init {
        viewModelScope.launch {
            expedicionRepository.getAllExpediciones().collectLatest { expediciones ->
                expedicionState = expedicionState.copy(
                    expediciones = expediciones
                )
            }
        }
    }

    fun getExpedicionesByPlazas(codPlaza: Int): List<Expedicion> {
        viewModelScope.launch {
            expedicionRepository.getExpedicionesByPlazas(codPlaza).collectLatest { expediciones ->
                expedicionState = expedicionState.copy(
                    expediciones = expediciones
                )
            }
        }
        return expedicionState.expediciones
    }

    fun getExpedicionById(idExpedicion: String): Expedicion? {
        viewModelScope.launch {
            expedicionRepository.getExpedicionById(idExpedicion).collectLatest { expediciones ->
                expedicionState = expedicionState.copy(
                    expediciones = expediciones
                )
            }
        }
        return expedicionState.expediciones.firstOrNull()
    }

    fun getExpedicionesBultosRepasadosByPlazas(codPlaza: Int): Int {
        return expedicionState.expediciones.filter { it.codPlaza == codPlaza }.sumOf { expedicion ->
            expedicion.numBultosRepasados
        }
    }

    fun getExpedicionesBultosConfirmadosByPlazas(codPlaza: Int): Int {
        return expedicionState.expediciones.filter { it.codPlaza == codPlaza }.sumOf { expedicion ->
            expedicion.numBultosConfirmados
        }
    }
    fun getExpedicionesBultosCargadosByPlazas(codPlaza: Int): Int {
        return expedicionState.expediciones.filter { it.codPlaza == codPlaza }.sumOf { expedicion ->
            expedicion.numbultosCargados
        }
    }
    fun getExpedicionesBultosTotalesByPlazas(codPlaza: Int): Int {
        return expedicionState.expediciones.filter { it.codPlaza == codPlaza }.sumOf { expedicion ->
            expedicion.numBultos
        }
    }

    fun getExpdicionesBultosRepasadosTotales(): Int {
        return expedicionState.expediciones.sumOf { expedicion ->
            expedicion.numBultosRepasados
        }
    }
    fun getExpdicionesBultosConfirmadosTotales(): Int {
        return expedicionState.expediciones.sumOf { expedicion ->
            expedicion.numBultosConfirmados
        }
    }

    fun updateExpedicion(updatedExpedicion: Expedicion) {
        expedicionRepository.updateExpedicion(updatedExpedicion)
    }

    fun getExpedicionesByIdCliente(idCliente: Int): List<Expedicion> {
        viewModelScope.launch {
            expedicionRepository.getExpedicionesByIdCliente(idCliente)
                .collectLatest { expediciones ->
                    expedicionState = expedicionState.copy(
                        expediciones = expediciones
                    )
                }
        }
        return expedicionState.expediciones
    }
}