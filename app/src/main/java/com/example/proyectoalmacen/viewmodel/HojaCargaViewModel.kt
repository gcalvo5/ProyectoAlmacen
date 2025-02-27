package com.example.proyectoalmacen.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoalmacen.model.DataClasses.Usuario
import com.example.proyectoalmacen.model.States.ExpedicionState
import com.example.proyectoalmacen.model.States.HojaCargaState
import com.example.proyectoalmacen.model.States.UiState
import com.example.proyectoalmacen.viewmodel.DataManagers.DataManager
import com.example.proyectoalmacen.viewmodel.repositories.BultoRepository
import com.example.proyectoalmacen.viewmodel.repositories.ExpedicionRepository
import com.example.proyectoalmacen.viewmodel.repositories.HojaCargaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HojaCargaViewModel @Inject constructor(private val hojaCargaRepository: HojaCargaRepository, private val dataManager: DataManager ) : ViewModel() {
    val hojasCargaList = dataManager.hojasCargaListState

    init {
        loadHojasCargaList()
    }

    fun loadHojasCargaList() {
        viewModelScope.launch {
            hojaCargaRepository.fetchHojasCarga("").collect { state ->
                dataManager.updateHojasCargaList(state)
            }
        }
    }

    fun createHojasCarga(muelle:Int, idPlazas:List<Int>){
        viewModelScope.launch {
            hojaCargaRepository.createHojasCarga(muelle, idPlazas).collect { state ->
                if (state is UiState.Success) (state as? UiState.Success)?.data?.let { dataManager.addHojasCarga(it) }
            }
        }

    }

}