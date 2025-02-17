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
import com.example.proyectoalmacen.viewmodel.repositories.EstadilloRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EstadilloViewModel @Inject constructor(val estadilloRepository: EstadilloRepository) : ViewModel() {
    var estadilloState by mutableStateOf(EstadilloState())
        private set


    init{
        viewModelScope.launch {
            estadilloRepository.getAllEstadillos().collectLatest { estadillos ->
                estadilloState = estadilloState.copy(
                    estadillos = estadillos
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun crearEstadillo(muelle:String, conductor:String){
        viewModelScope.launch {
            estadilloState = estadilloState.copy(
                isLoading = true
            )
            estadilloRepository.crearEstadillo(muelle = muelle, conductor = conductor)
            estadilloState = estadilloState.copy(
                isLoading = false
            )
        }
    }

    fun getEstadilloByID(idEstadillo: Int):Estadillo?{
        viewModelScope.launch {
            estadilloRepository.getEstadilloByID(idEstadillo).collectLatest { estadillos ->
                estadilloState = estadilloState.copy(
                    estadillos = estadillos
                )
            }
        }
        return estadilloState.estadillos.firstOrNull()
    }
}