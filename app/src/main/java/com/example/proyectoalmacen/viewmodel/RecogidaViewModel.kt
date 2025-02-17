package com.example.proyectoalmacen.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoalmacen.model.DataClasses.Cliente
import com.example.proyectoalmacen.model.DataClasses.Estadillo
import com.example.proyectoalmacen.model.DataClasses.Expedicion
import com.example.proyectoalmacen.model.DataClasses.Recogida
import com.example.proyectoalmacen.model.DataClasses.TipoUsuario
import com.example.proyectoalmacen.model.DataClasses.Usuario
import com.example.proyectoalmacen.model.States.RecogidaState
import com.example.proyectoalmacen.viewmodel.repositories.RecogidaReopsitory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecogidaViewModel @Inject constructor(val recogidaRepository: RecogidaReopsitory) : ViewModel()
{
    var recogidasState by mutableStateOf(RecogidaState())
        private set
    fun getRecogidasByIdConductor(idConcutor: Int):List<Recogida>{
        viewModelScope.launch {
            recogidaRepository.getRecogidasByIdConductor(idConcutor).collectLatest { recogidas ->
                recogidasState = recogidasState.copy(
                    recogidas = recogidas
                )
            }
        }
        return recogidasState.recogidas
    }

}