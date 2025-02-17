package com.example.proyectoalmacen.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoalmacen.model.DataClasses.Bulto
import com.example.proyectoalmacen.model.DataClasses.EstadoBulto
import com.example.proyectoalmacen.model.States.BultoState
import com.example.proyectoalmacen.model.States.EstadilloState
import com.example.proyectoalmacen.viewmodel.repositories.BultoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BultoViewModel @Inject constructor(private val bultoRepository: BultoRepository) : ViewModel() {
    var bultoState by mutableStateOf(BultoState())
        private set


    fun getBultosByExpedicion(idExpedicion: String): List<Bulto> {
        viewModelScope.launch {
            bultoRepository.getBultosByExpedicion(idExpedicion).collectLatest { bultos ->
                bultoState = bultoState.copy(
                    bultos = bultos
                )
            }
        }
        return bultoState.bultos
    }
    fun getBultosByIdBulto(idBulto: String): List<Bulto> {
        viewModelScope.launch {
            bultoRepository.getBultosByidBulto(idBulto).collectLatest { bultos ->
                bultoState = bultoState.copy(
                    bultos = bultos
                )
            }
        }
        return bultoState.bultos
    }

    fun updateBulto(updatedBulto: Bulto) {
        bultoRepository.updateBulto(updatedBulto)
    }
}