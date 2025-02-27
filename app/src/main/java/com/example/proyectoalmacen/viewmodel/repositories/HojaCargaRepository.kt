package com.example.proyectoalmacen.viewmodel.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.proyectoalmacen.model.API.FakeAPI
import com.example.proyectoalmacen.model.DataClasses.Cliente
import com.example.proyectoalmacen.model.DataClasses.Estadillo
import com.example.proyectoalmacen.model.DataClasses.Expedicion
import com.example.proyectoalmacen.model.DataClasses.HojaCarga
import com.example.proyectoalmacen.model.DataClasses.TipoUsuario
import com.example.proyectoalmacen.model.DataClasses.Usuario
import com.example.proyectoalmacen.model.States.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HojaCargaRepository @Inject constructor(val api: FakeAPI) {
    fun fetchHojasCarga(query:String):Flow<UiState<List<HojaCarga>>> = flow {
        emit(UiState.Loading)
        try {
            val response = api.getHojasCarga(query)
            if (response.isSuccessful){
                emit(UiState.Success(response.body()!! ))
            }else{
                emit(UiState.Error( response.message()))
            }
        }catch (e: Exception){
            emit(UiState.Error(e.message.toString() ))
        }
    }
    fun createHojasCarga(muelle:Int, idPlazas:List<Int>):Flow<UiState<HojaCarga>> = flow{
        emit(UiState.Loading)
        try {
            //Implementar llamada a la API y coger usuario real y tiempo real
            val response = api.postHojaCarga(HojaCarga(muelle = muelle, numHojaCarga = 1, idUsuario = 1, expediciones = emptyList(), fechaCreacion = "26/02/2025", tiempoCreacion = "16:45", idPlazas = idPlazas))
            if (response.isSuccessful){
                emit(UiState.Success(response.body()!! ))
            }else{
                emit(UiState.Error( response.message()))
            }
        }catch (e: Exception){
            emit(UiState.Error(e.message.toString() ))
        }
    }
}