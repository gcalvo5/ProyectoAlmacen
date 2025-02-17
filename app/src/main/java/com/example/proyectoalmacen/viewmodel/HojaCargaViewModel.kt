package com.example.proyectoalmacen.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoalmacen.model.States.ExpedicionState
import com.example.proyectoalmacen.model.States.HojaCargaState
import com.example.proyectoalmacen.viewmodel.repositories.BultoRepository
import com.example.proyectoalmacen.viewmodel.repositories.ExpedicionRepository
import com.example.proyectoalmacen.viewmodel.repositories.HojaCargaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HojaCargaViewModel @Inject constructor(
    private val hojaCargaRepository: HojaCargaRepository,
) : ViewModel() {
    var hojaCargaState by mutableStateOf(HojaCargaState())
        private set
    init{
        viewModelScope.launch {
            hojaCargaRepository.getAllHojaCarga().collectLatest { hojaCarga ->
                hojaCargaState = hojaCargaState.copy(
                    hojasCarga = hojaCarga
                )
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun crearHojaCarga(muelle: String, listaPlazasId:List<Int>, idUsuario:Int = 1) {
        hojaCargaRepository.crearHojaCarga(muelle, listaPlazasId, idUsuario)
    }

}