package com.example.proyectoalmacen.viewmodel.repositories

import androidx.compose.animation.core.copy
import androidx.compose.runtime.derivedStateOf
import com.example.proyectoalmacen.model.API.FakeAPI

import com.example.proyectoalmacen.model.DataClasses.Bulto
import com.example.proyectoalmacen.model.DataClasses.Cliente
import com.example.proyectoalmacen.model.DataClasses.EstadoBulto
import com.example.proyectoalmacen.model.DataClasses.Expedicion
import com.example.proyectoalmacen.model.States.UiState
import com.example.proyectoalmacen.viewmodel.DataManagers.DataManager
import com.example.proyectoalmacen.viewmodel.UseCases.UseCaseGetExpedicionesNumBultosTotales
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Singleton

@Singleton
class ExpedicionRepository @Inject constructor(private val api: FakeAPI, private val dataManager: DataManager) {
    fun fetchExpediciones():Flow<UiState<List<Expedicion>>> = flow {
        emit(UiState.Loading)
        try {

            val response = api.getExpediciones()
            val bultos = dataManager.bultosListState.value as? UiState.Success<List<Bulto>>
            if (response.isSuccessful){
                response.body()!!.forEach { expedicion ->
                    expedicion.bultos = bultos?.data?.filter { it.idExpedicion == expedicion.idExpedicion } ?: emptyList()
                    expedicion.numBultos = expedicion.bultos.size
                    expedicion.numBultosConfirmados = expedicion.bultos.count { it.estadoBulto == EstadoBulto.DESCARGADO }
                    expedicion.numBultosRepasados = expedicion.bultos.count { it.estadoBulto == EstadoBulto.REPASADO }
                    expedicion.numbultosCargados = expedicion.bultos.count { it.estadoBulto == EstadoBulto.CARGADO }
                }
                emit(UiState.Success(response.body()!! ))
            }else{
                emit(UiState.Error( response.message()))
            }
        }catch (e: Exception){
            emit(UiState.Error(e.message.toString() ))
        }
    }
    fun fetchExpedicionesFiltered(idExpedicion:String = "", codPlaza:Int = 0,idClienteMultiple:List<String> = emptyList(), codPlazasMultiple:List<String> = emptyList(), idCliente:Int = 0):Flow<UiState<List<Expedicion>>> = flow {
        emit(UiState.Loading)
        try {
            val response = api.getExpedicionesFiltered(idExpedicion, codPlaza, idCliente, codPlazasMultiple, idClienteMultiple)
            val bultos = dataManager.bultosListState.value as? UiState.Success<List<Bulto>>
            if (response.isSuccessful){
                response.body()!!.forEach { expedicion ->
                    expedicion.bultos = bultos?.data?.filter { it.idExpedicion == expedicion.idExpedicion } ?: emptyList()
                    expedicion.numBultos = expedicion.bultos.size
                    expedicion.numBultosConfirmados = expedicion.bultos.count { it.estadoBulto == EstadoBulto.DESCARGADO }
                    expedicion.numBultosRepasados = expedicion.bultos.count { it.estadoBulto == EstadoBulto.REPASADO }
                    expedicion.numbultosCargados = expedicion.bultos.count { it.estadoBulto == EstadoBulto.CARGADO }
                }
                emit(UiState.Success(response.body()!! ))
            }else{
                emit(UiState.Error( response.message()))
            }
        }catch (e: Exception){
            emit(UiState.Error(e.message.toString() ))
        }
    }


}