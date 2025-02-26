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
import com.example.proyectoalmacen.model.States.UiState
import com.example.proyectoalmacen.viewmodel.DataManagers.DataManager
import com.example.proyectoalmacen.viewmodel.UseCases.UseCaseGetExpedicionesNumBultosTotales
import com.example.proyectoalmacen.viewmodel.repositories.BultoRepository
import com.example.proyectoalmacen.viewmodel.repositories.ExpedicionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

enum class ExpedicionesQueryType{
    ID, CODPLAZA, IDCLIENTE
}

@HiltViewModel
class ExpedicionViewModel @Inject constructor(
    private val expedicionRepository: ExpedicionRepository, private val dataManager: DataManager, private val useCaseGetExpedicionesNumBultosTotales: UseCaseGetExpedicionesNumBultosTotales
) : ViewModel() {
    val expedicionList = dataManager.expedicionesListState
    val expedicionListFiltred = dataManager.expedicionesListStateFiltred
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery
    private val _queryType = MutableStateFlow(ExpedicionesQueryType.ID)
    val queryType: StateFlow<ExpedicionesQueryType> = _queryType
    private val _expedicionNoUpdate = MutableStateFlow<UiState<List<Expedicion>>>(UiState.Success(emptyList()))
    val expedicionNoUpdate: StateFlow<UiState<List<Expedicion>>> = _expedicionNoUpdate

    init {
        loadExpedicionList()
    }

    fun loadExpedicionList() {
        viewModelScope.launch {
            expedicionRepository.fetchExpediciones().collect { state ->
                dataManager.updateExpedicionList(state)
                dataManager.updateExpedicionListFiltred(state)
            }
        }
    }
    fun loadExpedicionListFiltered() {
        viewModelScope.launch {
            when (_queryType.value) {
                ExpedicionesQueryType.ID -> {
                    expedicionRepository.fetchExpedicionesFiltered(idExpedicion = searchQuery.value).collect { state ->
                        dataManager.updateExpedicionListFiltred(state)
                    }
                }
                ExpedicionesQueryType.CODPLAZA -> {
                    expedicionRepository.fetchExpedicionesFiltered(codPlaza = searchQuery.value.toInt()).collect { state ->
                        dataManager.updateExpedicionListFiltred(state)
                    }
                }
                ExpedicionesQueryType.IDCLIENTE -> {
                    expedicionRepository.fetchExpedicionesFiltered(idCliente = searchQuery.value.toInt()).collect { state ->
                        dataManager.updateExpedicionListFiltred(state)
                    }
                }
            }
            _searchQuery.value = ""
        }
    }
    // devuelve las expediciones filtradas pero no actualiza el state, sirve si solo tenemos que usar esas expediciones 1 vez no deben estar atentas a actualizaciones
    fun loadExpedicionListFilteredJustReturn(){
        viewModelScope.launch {

            when (_queryType.value) {
                ExpedicionesQueryType.ID -> {
                    expedicionRepository.fetchExpedicionesFiltered(idExpedicion = searchQuery.value).collect { state ->
                        _expedicionNoUpdate.value = state
                    }
                }
                ExpedicionesQueryType.CODPLAZA -> {
                    expedicionRepository.fetchExpedicionesFiltered(codPlaza = searchQuery.value.toInt()).collect { state ->
                        _expedicionNoUpdate.value = state
                    }
                }
                ExpedicionesQueryType.IDCLIENTE -> {
                    expedicionRepository.fetchExpedicionesFiltered(idCliente = searchQuery.value.toInt()).collect { state ->
                        _expedicionNoUpdate.value = state
                    }
                }
            }
            _searchQuery.value = ""

        }
    }
    fun getListNumBultosTotales(codPlaza:Int = 0):List<Int>{
        var listaActualizada = emptyList<Int>()
        viewModelScope.launch {
        useCaseGetExpedicionesNumBultosTotales.execute(codPlaza).collectLatest {
            when (it) {
                is UiState.Success -> {
                    listaActualizada = it.data
                }

                is UiState.Error -> {

                }

                is UiState.Loading -> {

                }

            }
        }
        }
        return listaActualizada
    }
    fun setSearchQuery(query: String, queryType: ExpedicionesQueryType, justReturn: Boolean = false) {
        _queryType.value = queryType
        _searchQuery.value = query
        if (justReturn){
            loadExpedicionListFilteredJustReturn()
        }else {
            loadExpedicionListFiltered()
        }
    }
}