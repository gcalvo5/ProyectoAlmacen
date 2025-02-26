package com.example.proyectoalmacen.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoalmacen.model.DataClasses.Estadillo
import com.example.proyectoalmacen.model.DataClasses.TipoUsuario
import com.example.proyectoalmacen.model.DataClasses.Usuario
import com.example.proyectoalmacen.model.States.EstadilloState
import com.example.proyectoalmacen.model.States.UiState
import com.example.proyectoalmacen.viewmodel.DataManagers.DataManager
import com.example.proyectoalmacen.viewmodel.repositories.EstadilloRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EstadilloViewModel @Inject constructor(private val estadilloRepository: EstadilloRepository, private val dataManager: DataManager) : ViewModel() {
    val estadillosList = dataManager.estadillosListState
    val estadillosListFiltred = dataManager.estadillosListStateFiltred
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        loadEstadillosList()
    }

    fun loadEstadillosList() {
        viewModelScope.launch {
            estadilloRepository.fetchEstadillos("").collect { state ->
                dataManager.updateEstadillosList(state)
                dataManager.updateEstadillosListFiltred(state)
            }
        }
    }
    fun loadEstadillosListFiltered() {
        viewModelScope.launch {
            estadilloRepository.fetchEstadillos(searchQuery.value).collect { state ->
                dataManager.updateEstadillosListFiltred(state)
            }
            _searchQuery.value = ""
        }
    }

    fun createEstadillo(muelle:Int, conductor:Usuario){
        viewModelScope.launch {
            estadilloRepository.createEstadillo(muelle, conductor).collect { state ->
                if (state is UiState.Success) (state as? UiState.Success)?.data?.let { dataManager.addEstadillo(it) }
            }
        }

    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        loadEstadillosListFiltered()
    }
}