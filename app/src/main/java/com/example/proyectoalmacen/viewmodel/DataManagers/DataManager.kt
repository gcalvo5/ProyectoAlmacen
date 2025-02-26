package com.example.proyectoalmacen.viewmodel.DataManagers

import androidx.compose.animation.core.copy
import com.example.proyectoalmacen.model.DataClasses.Bulto
import com.example.proyectoalmacen.model.DataClasses.Estadillo
import com.example.proyectoalmacen.model.DataClasses.EstadoBulto
import com.example.proyectoalmacen.model.DataClasses.Expedicion
import com.example.proyectoalmacen.model.States.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManager @Inject constructor(private val externalScope: CoroutineScope) {
    private val _expedicionesListState = MutableStateFlow<UiState<List<Expedicion>>>(UiState.Loading)

    private val _estadillosListState = MutableStateFlow<UiState<List<Estadillo>>>(UiState.Loading)

    private val _bultosListState = MutableStateFlow<UiState<List<Bulto>>>(UiState.Loading)

    private val _expedicionesListStateFiltred = MutableStateFlow<UiState<List<Expedicion>>>(UiState.Loading)

    private val _estadillosListStateFiltred = MutableStateFlow<UiState<List<Estadillo>>>(UiState.Loading)

    private val _bultosListStateFiltred = MutableStateFlow<UiState<List<Bulto>>>(UiState.Loading)



    val expedicionesListState: StateFlow<UiState<List<Expedicion>>> = combine(_expedicionesListState, _bultosListState) { expedicionesState, bultosState ->
        if (expedicionesState is UiState.Success && bultosState is UiState.Success) {
            val updatedExpediciones = expedicionesState.data.map { expedicion ->
                val filteredBultos = bultosState.data.filter { it.idExpedicion == expedicion.idExpedicion }
                expedicion.copy(
                    bultos = filteredBultos,
                    numBultos = filteredBultos.size,
                    numBultosConfirmados = filteredBultos.count { it.estadoBulto == EstadoBulto.DESCARGADO },
                    numBultosRepasados = filteredBultos.count { it.estadoBulto == EstadoBulto.REPASADO },
                    numbultosCargados = filteredBultos.count { it.estadoBulto == EstadoBulto.CARGADO }
                )
            }
            updatedExpediciones.toMutableList().sortWith(compareByDescending<Expedicion> { it.idEstadillo }.thenBy { it.idExpedicion })
            UiState.Success(updatedExpediciones)
        } else if (expedicionesState is UiState.Loading || bultosState is UiState.Loading) {
            UiState.Loading
        } else {
            UiState.Error("Error loading data")
        }
    }.stateIn(scope = externalScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading)

    val estadillosListState: StateFlow<UiState<List<Estadillo>>> = combine(_estadillosListState, expedicionesListState) { estadillosState, expedicionesState ->
        if (estadillosState is UiState.Success && expedicionesState is UiState.Success) {
            val updatedEstadillos = estadillosState.data.map { estadillo ->
                val filteredExpediciones = expedicionesState.data.filter { it.idEstadillo == estadillo.numEst }
                estadillo.copy(
                    expediciones = filteredExpediciones,
                    numBultosTotal = filteredExpediciones.sumOf { expedicion -> expedicion.numBultos },
                    numBultosConfirmados = filteredExpediciones.sumOf { expedicion -> expedicion.numBultosConfirmados },
                    numBultosRepasados = filteredExpediciones.sumOf { expedicion -> expedicion.numBultosRepasados },
                    numBultosCargados = filteredExpediciones.sumOf { expedicion -> expedicion.numbultosCargados }
                )
            }
            UiState.Success(updatedEstadillos)
        } else if (expedicionesState is UiState.Loading || estadillosState is UiState.Loading) {
            UiState.Loading
        } else {
            UiState.Error("Error loading data")
        }
    }.stateIn(scope = externalScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading)
    val bultosListState: StateFlow<UiState<List<Bulto>>> = _bultosListState
    val expedicionesListStateFiltred: StateFlow<UiState<List<Expedicion>>> = combine(_expedicionesListStateFiltred, _bultosListState) { expedicionesState, bultosState ->
        if (expedicionesState is UiState.Success && bultosState is UiState.Success) {
            val updatedExpediciones = expedicionesState.data.map { expedicion ->
                val filteredBultos = bultosState.data.filter { it.idExpedicion == expedicion.idExpedicion }
                expedicion.copy(
                    bultos = filteredBultos,
                    numBultos = filteredBultos.size,
                    numBultosConfirmados = filteredBultos.count { it.estadoBulto == EstadoBulto.DESCARGADO },
                    numBultosRepasados = filteredBultos.count { it.estadoBulto == EstadoBulto.REPASADO },
                    numbultosCargados = filteredBultos.count { it.estadoBulto == EstadoBulto.CARGADO }
                )
            }
            updatedExpediciones.toMutableList().sortWith(compareByDescending<Expedicion> { it.idEstadillo }.thenBy { it.idExpedicion })
            UiState.Success(updatedExpediciones)
        } else if (expedicionesState is UiState.Loading || bultosState is UiState.Loading) {
            UiState.Loading
        } else {
            UiState.Error("Error loading data")
        }
    }.stateIn(scope = externalScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading)
    val estadillosListStateFiltred: StateFlow<UiState<List<Estadillo>>> = combine(_estadillosListStateFiltred, expedicionesListState) { estadillosState, expedicionesState ->
        if (estadillosState is UiState.Success && expedicionesState is UiState.Success) {
            val updatedEstadillos = estadillosState.data.map { estadillo ->
                val filteredExpediciones = expedicionesState.data.filter { it.idEstadillo == estadillo.numEst }
                estadillo.copy(
                    expediciones = filteredExpediciones,
                    numBultosTotal = filteredExpediciones.sumOf { expedicion -> expedicion.numBultos },
                    numBultosConfirmados = filteredExpediciones.sumOf { expedicion -> expedicion.numBultosConfirmados },
                    numBultosRepasados = filteredExpediciones.sumOf { expedicion -> expedicion.numBultosRepasados },
                    numBultosCargados = filteredExpediciones.sumOf { expedicion -> expedicion.numbultosCargados }
                )
            }
            UiState.Success(updatedEstadillos)
        } else if (expedicionesState is UiState.Loading || estadillosState is UiState.Loading) {
            UiState.Loading
        } else {
            UiState.Error("Error loading data")
        }
    }.stateIn(scope = externalScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading)
    val bultosListStateFiltred: StateFlow<UiState<List<Bulto>>> = _bultosListStateFiltred



    fun updateExpedicionList(expediciones: UiState<List<Expedicion>>) {
        _expedicionesListState.value = expediciones
    }

    fun updateEstadillosList(estadillos: UiState<List<Estadillo>>) {
        _estadillosListState.value = estadillos
    }

    fun updateBultosList(bultos: UiState<List<Bulto>>) {
        _bultosListState.value = bultos
    }
    fun updateExpedicionListFiltred(expediciones: UiState<List<Expedicion>>) {
        _expedicionesListStateFiltred.value = expediciones
    }

    fun updateEstadillosListFiltred(estadillos: UiState<List<Estadillo>>) {
        _estadillosListStateFiltred.value = estadillos
    }

    fun updateBultosListFiltred(bultos: UiState<List<Bulto>>) {
        _bultosListStateFiltred.value = bultos
    }
    fun addEstadillo(estadillo: Estadillo){
        val estadillos = (_estadillosListState.value as? UiState.Success)?.data?.toMutableList() ?: mutableListOf()
        estadillos.add(estadillo)
        _estadillosListState.value = UiState.Success(estadillos)
    }
    fun addBulto(bulto: Bulto){
        val bultos = (_bultosListState.value as? UiState.Success)?.data?.toMutableList() ?: mutableListOf()
        bultos.add(bulto)
        _bultosListState.value = UiState.Success(bultos)

    }
    fun addExpedicion(expedicion: Expedicion){
        val expediciones = (_expedicionesListState.value as? UiState.Success)?.data?.toMutableList() ?: mutableListOf()
        expediciones.add(expedicion)
        _expedicionesListState.value = UiState.Success(expediciones)
    }
    fun updateBulto(bulto: Bulto) {
        val currentBultosState = _bultosListState.value
        if (currentBultosState is UiState.Success) {
            val currentBultos = currentBultosState.data
            val index = currentBultos.indexOfFirst { it.idBulto == bulto.idBulto }
            if (index != -1) {
                // Create a new list with the updated Bulto
                val newBultos = currentBultos.toMutableList()
                newBultos[index] = bulto
                newBultos.sortWith(compareByDescending<Bulto> { it.idExpedicion }.thenBy { it.idBulto })
                // Update the StateFlow with the new list wrapped in UiState.Success
                _bultosListState.value = UiState.Success(newBultos.toList())
            }
        } else if (currentBultosState is UiState.Error){
            _bultosListState.value = currentBultosState
        } else if (currentBultosState is UiState.Loading){
            _bultosListState.value = currentBultosState
        }
    }
    fun updateExpedicion(expedicion: Expedicion){
        val expediciones = (_expedicionesListState.value as? UiState.Success)?.data?.toMutableList() ?: mutableListOf()
        val index = expediciones.indexOfFirst { it.idExpedicion == expedicion.idExpedicion }
        if (index != -1) {
            expediciones[index] = expedicion
            _expedicionesListState.value = UiState.Success(expediciones)
        }
    }
    fun updateEstadillo(estadillo: Estadillo){
        val estadillos = (_estadillosListState.value as? UiState.Success)?.data?.toMutableList() ?: mutableListOf()
        val index = estadillos.indexOfFirst { it.numEst == estadillo.numEst }
        if (index != -1) {
            estadillos[index] = estadillo
            _estadillosListState.value = UiState.Success(estadillos)
        }
    }
}