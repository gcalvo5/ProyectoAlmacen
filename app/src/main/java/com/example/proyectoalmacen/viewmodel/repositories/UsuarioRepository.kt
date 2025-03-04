package com.example.proyectoalmacen.viewmodel.repositories

import com.example.proyectoalmacen.model.API.FakeAPI
import com.example.proyectoalmacen.model.DataClasses.HojaCarga
import com.example.proyectoalmacen.model.DataClasses.Usuario
import com.example.proyectoalmacen.model.States.UiState
import com.example.proyectoalmacen.viewmodel.DataManagers.DataManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UsuarioRepository @Inject constructor(val api: FakeAPI, dataManager: DataManager){
    fun fetchUsuarios(query:String): Flow<UiState<List<Usuario>>> = flow {
        emit(UiState.Loading)
        try {
            val response = api.getUsuarios(query)
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