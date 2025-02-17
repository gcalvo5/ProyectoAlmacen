package com.example.proyectoalmacen.viewmodel.repositories

import com.example.proyectoalmacen.model.DataClasses.Cliente
import com.example.proyectoalmacen.model.DataClasses.Expedicion
import com.example.proyectoalmacen.model.DataClasses.Recogida
import com.example.proyectoalmacen.model.DataClasses.TipoUsuario
import com.example.proyectoalmacen.model.DataClasses.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecogidaReopsitory @Inject constructor(private val expedicionRepository: ExpedicionRepository) {
    //Para cambiar a coger de futuro repositorio de clientes y futuro repositorio de usuarios
    private val clienteRecogida = Cliente(idCliente = 1, nombreCliente = "Univar")
    private val clienteRecogida2 = Cliente(idCliente = 2, nombreCliente = "Proquimia")
    private val conductor = Usuario(numUsuario = 2, nombre = "Juan", tipoUsuario = TipoUsuario.CHOFER)

    private val _recogidas = MutableStateFlow<List<Recogida>>(
        listOf(
            Recogida(idRecogida = 1,
                clienteRecogida = clienteRecogida,
                conductorRecogida = conductor
            ),
            Recogida(idRecogida = 2,
                clienteRecogida = clienteRecogida2,
                conductorRecogida = conductor
            )
        )
    )
    val recogidas = _recogidas.asStateFlow()

    fun getRecogidasByIdConductor(idConductor: Int): Flow<List<Recogida>> {
        return recogidas.map { recogidas ->
            recogidas.filter { it.conductorRecogida.numUsuario == idConductor }
        }
    }
}