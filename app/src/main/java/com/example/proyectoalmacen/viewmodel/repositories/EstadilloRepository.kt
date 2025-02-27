package com.example.proyectoalmacen.viewmodel.repositories


import android.os.Build
import androidx.annotation.RequiresApi
import com.example.proyectoalmacen.model.API.FakeAPI
import com.example.proyectoalmacen.model.DataClasses.Estadillo
import com.example.proyectoalmacen.model.DataClasses.EstadoBulto
import com.example.proyectoalmacen.model.DataClasses.Expedicion
import com.example.proyectoalmacen.model.DataClasses.TipoUsuario
import com.example.proyectoalmacen.model.DataClasses.Usuario
import com.example.proyectoalmacen.model.States.UiState
import com.example.proyectoalmacen.viewmodel.DataManagers.DataManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.text.format

@Singleton
class EstadilloRepository @Inject constructor(private val api: FakeAPI) {
    fun fetchEstadillos(query:String):Flow<UiState<List<Estadillo>>> = flow {
        emit(UiState.Loading)
        try {
            val response = api.getEstadillos(query)
            if (response.isSuccessful){
                emit(UiState.Success(response.body()!! ))
            }else{
                emit(UiState.Error( response.message()))
            }
        }catch (e: Exception){
            emit(UiState.Error(e.message.toString() ))
        }
    }
    fun createEstadillo(muelle:Int, conductor:Usuario):Flow<UiState<Estadillo>> = flow{
        emit(UiState.Loading)
        try {
            //Implementar llamada a la API y coger usuario real y tiempo real
            val response = api.postEstadillo(Estadillo(muelle = muelle, numEst = 1, conductor = conductor, usuario = Usuario(1,"Admin", TipoUsuario.ADMIN), expediciones = emptyList(), fechaCreacion = "", tiempoCreacion = ""))
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