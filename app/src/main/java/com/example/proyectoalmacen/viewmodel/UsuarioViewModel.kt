package com.example.proyectoalmacen.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoalmacen.model.DataClasses.Cliente
import com.example.proyectoalmacen.model.DataClasses.Estadillo
import com.example.proyectoalmacen.model.DataClasses.Expedicion
import com.example.proyectoalmacen.model.DataClasses.Plazas
import com.example.proyectoalmacen.model.DataClasses.Recogida
import com.example.proyectoalmacen.model.DataClasses.TipoUsuario
import com.example.proyectoalmacen.model.DataClasses.Usuario
import com.example.proyectoalmacen.model.States.RecogidaState
import com.example.proyectoalmacen.model.States.UsuarioState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor() : ViewModel()
{
    var usuarioState by mutableStateOf(UsuarioState())
        private set

    init{
        usuarioState = usuarioState.copy(
            isLoading = true
        )
        usuarioState = usuarioState.copy(
            usuarios = listOf(
                Usuario(numUsuario = 1, nombre = "Admin", tipoUsuario = TipoUsuario.CHOFER),
                Usuario(numUsuario = 2, nombre = "Juan", tipoUsuario = TipoUsuario.CHOFER),
            ),
            isLoading = false
        )
    }

    fun getUsuarioByName(nombre: String):List<Usuario>{
        return usuarioState.usuarios.filter { it.nombre == nombre }
    }
    fun getUsuarioById(id:Int): Usuario {
        return usuarioState.usuarios.first { it.numUsuario == id }
    }

}