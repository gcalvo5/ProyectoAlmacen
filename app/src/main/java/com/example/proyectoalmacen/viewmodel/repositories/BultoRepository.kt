package com.example.proyectoalmacen.viewmodel.repositories


import com.example.proyectoalmacen.model.API.FakeAPI
import com.example.proyectoalmacen.model.DataClasses.Bulto
import com.example.proyectoalmacen.model.DataClasses.EstadoBulto
import com.example.proyectoalmacen.model.States.UiState
import com.example.proyectoalmacen.viewmodel.DataManagers.DataManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BultoRepository @Inject constructor(private val fakeAPI: FakeAPI) {
    fun fetchBultos(query:String):Flow<UiState<List<Bulto>>> = flow {
        try {
            val response = fakeAPI.getBultos(query)
            if (response.isSuccessful){
                emit(UiState.Success(response.body()!! ))
            }else{
                emit(UiState.Error( response.message()))
            }
        }catch (e: Exception){
            emit(UiState.Error(e.message.toString() ))
        }
    }
    fun updateBulto(bulto: Bulto):Flow<UiState<Bulto>> = flow {
        try {
            val response = fakeAPI.putBulto(bulto)
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