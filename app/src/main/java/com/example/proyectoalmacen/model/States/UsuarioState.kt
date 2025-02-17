package com.example.proyectoalmacen.model.States

import com.example.proyectoalmacen.model.DataClasses.Usuario

data class UsuarioState (
    val usuarios: List<Usuario> = emptyList(),
    val isLoading: Boolean = false
)
