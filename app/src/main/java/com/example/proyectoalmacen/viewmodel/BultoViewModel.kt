package com.example.proyectoalmacen.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoalmacen.model.DataClasses.Bulto
import com.example.proyectoalmacen.model.DataClasses.EstadoBulto
import com.example.proyectoalmacen.model.States.EstadilloState
import com.example.proyectoalmacen.viewmodel.repositories.BultoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.proyectoalmacen.model.States.UiState
import com.example.proyectoalmacen.viewmodel.DataManagers.DataManager
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class BultoViewModel @Inject constructor(private val bultoRepository: BultoRepository, private val dataManager: DataManager) : ViewModel() {

    val bultosList = dataManager.bultosListState
    val bultosListFiltred = dataManager.bultosListStateFiltred
    private val _searchQuery = MutableStateFlow("")
    val searchQuery:StateFlow<String> = _searchQuery
    init {
        loadBultosList()
    }

    fun loadBultosList() {
        viewModelScope.launch {
            bultoRepository.fetchBultos("").collect { state ->
                dataManager.updateBultosList(state)
                dataManager.updateBultosListFiltred(state)
            }
        }
    }

    private fun loadBultosListFiltered() {
        viewModelScope.launch {
            bultoRepository.fetchBultos(searchQuery.value).collect { state ->
                dataManager.updateBultosListFiltred(state)
            }
            _searchQuery.value = ""
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        loadBultosListFiltered()
    }

    fun updateBulto(bulto: Bulto) {
        viewModelScope.launch {
            bultoRepository.updateBulto(bulto).collect { state ->
                if (state is UiState.Success) dataManager.updateBulto((state as UiState.Success).data)
            }
        }
    }
}