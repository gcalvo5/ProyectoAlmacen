package com.example.proyectoalmacen.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoalmacen.model.DataClasses.Cliente
import com.example.proyectoalmacen.model.DataClasses.Estadillo
import com.example.proyectoalmacen.model.DataClasses.Expedicion
import com.example.proyectoalmacen.model.DataClasses.Plazas
import com.example.proyectoalmacen.model.DataClasses.Recogida
import com.example.proyectoalmacen.model.DataClasses.TipoUsuario
import com.example.proyectoalmacen.model.DataClasses.Usuario
import com.example.proyectoalmacen.model.States.RecogidaState
import com.example.proyectoalmacen.model.States.UsuarioState
import com.example.proyectoalmacen.viewmodel.DataManagers.DataManager
import com.example.proyectoalmacen.viewmodel.repositories.BultoRepository
import com.example.proyectoalmacen.viewmodel.repositories.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor(private val dataManager: DataManager, private val usuarioRepository: UsuarioRepository) : ViewModel()
{
    val usuariosList = dataManager.usuariosListState
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery
    init {
        loadBultosList()
    }

    fun loadBultosList() {
        viewModelScope.launch {
            usuarioRepository.fetchUsuarios("").collect { state ->
                dataManager.updateUsuariosList(state)
            }
        }
    }

    private fun loadBultosListFiltered() {
        viewModelScope.launch {
            usuarioRepository.fetchUsuarios(searchQuery.value).collect { state ->
                dataManager.updateUsuariosList(state)
            }
            _searchQuery.value = ""
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        loadBultosListFiltered()
    }

}