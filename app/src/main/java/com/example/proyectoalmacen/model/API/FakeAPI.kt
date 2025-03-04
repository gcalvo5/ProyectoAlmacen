package com.example.proyectoalmacen.model.API

import com.example.proyectoalmacen.model.DataClasses.Bulto
import com.example.proyectoalmacen.model.DataClasses.Cliente
import com.example.proyectoalmacen.model.DataClasses.Estadillo
import com.example.proyectoalmacen.model.DataClasses.EstadoBulto
import com.example.proyectoalmacen.model.DataClasses.Expedicion
import com.example.proyectoalmacen.model.DataClasses.HojaCarga
import com.example.proyectoalmacen.model.DataClasses.TipoUsuario
import com.example.proyectoalmacen.model.DataClasses.Usuario
import kotlinx.coroutines.delay
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAPI @Inject constructor() {
    val bultosInicial = mutableListOf<Bulto>(
        Bulto(idBulto = "00TLNKC354C9BD01A5B8", idExpedicion = "2", estadoBulto = EstadoBulto.CREADO),
        Bulto(idBulto = "00TLNKC62C89B82BB835", idExpedicion = "3", estadoBulto = EstadoBulto.CREADO),
        Bulto(idBulto = "00TLNKC354C9BD01A348", idExpedicion = "1", estadoBulto = EstadoBulto.CREADO),
        Bulto(idBulto = "00TLNKC62C89B82BB555", idExpedicion = "5", estadoBulto = EstadoBulto.CREADO),
        Bulto(idBulto = "00TLNKC354C9BD01A668", idExpedicion = "5", estadoBulto = EstadoBulto.CREADO),
        Bulto(idBulto = "00TLNKC62C89B8277835", idExpedicion = "4", estadoBulto = EstadoBulto.CREADO),
        Bulto(idBulto = "00TLNKC354C9BD0885B8", idExpedicion = "5", estadoBulto = EstadoBulto.CREADO),
        Bulto(idBulto = "00TLNKC62C89B8299835", idExpedicion = "2", estadoBulto = EstadoBulto.CREADO),
        Bulto(idBulto = "00TLNKC354C9BD00A5B8", idExpedicion = "4", estadoBulto = EstadoBulto.CREADO),
        Bulto(idBulto = "00TLNKC726DD1D61B8B0", idExpedicion = "3", estadoBulto = EstadoBulto.CREADO),
        Bulto(idBulto = "00TLNKC354C9BD22A5B8", idExpedicion = "2", estadoBulto = EstadoBulto.CREADO),
        Bulto(idBulto = "00TLNKC62C89B833B835", idExpedicion = "1", estadoBulto = EstadoBulto.CREADO),

        )
    var bultos = bultosInicial
    val expedicionesInicial = mutableListOf(
        Expedicion(idExpedicion = "1", idEstadillo = 1, provincia = "Valencia", cliente = Cliente(155, "Univar"), bultos = emptyList(), codPlaza = 2, referenciaCliente = "ABC"),
        Expedicion(idExpedicion = "2", idEstadillo = 1, provincia = "Murcia", cliente = Cliente(233, "Proquimia"), bultos = emptyList(), codPlaza = 5, referenciaCliente = "DEF"),
        Expedicion(idExpedicion = "3", idEstadillo = 1, provincia = "Valencia", cliente = Cliente(233, "Proquimia"), bultos = emptyList(), codPlaza = 2, referenciaCliente = "GHI"),
        Expedicion(idExpedicion = "4", idEstadillo = 1, provincia = "Albacete", cliente = Cliente(155, "Univar"), bultos = emptyList(), codPlaza = 4, referenciaCliente = "JKL"),
        Expedicion(idExpedicion = "5", idEstadillo = 1, provincia = "Alicante", cliente = Cliente(233, "Proquimia"), bultos = emptyList(), codPlaza = 3, referenciaCliente = "MNO")
    )
    var expediciones = expedicionesInicial
    val estadillosInicial = mutableListOf(
        Estadillo(1, usuario = Usuario(5,"Admin", TipoUsuario.ADMIN), conductor = Usuario(1,"Juan", TipoUsuario.CHOFER), fechaCreacion = "07/07/2025", tiempoCreacion = "13:00", muelle = 22, expediciones = emptyList()),
        Estadillo(2, usuario = Usuario(5,"Admin", TipoUsuario.ADMIN), conductor = Usuario(2, "Juan", TipoUsuario.CHOFER), fechaCreacion = "08/07/2025", tiempoCreacion = "13:34", muelle = 22, expediciones = emptyList()),
        Estadillo(3, usuario = Usuario(5,"Admin", TipoUsuario.ADMIN), conductor =  Usuario(3,"Juan", TipoUsuario.CHOFER), fechaCreacion = "09/07/2025", tiempoCreacion = "12:32", muelle = 22, expediciones = emptyList()),
        Estadillo(4, usuario = Usuario(5,"Admin", TipoUsuario.ADMIN), conductor =  Usuario(4,"Juan", TipoUsuario.CHOFER), fechaCreacion = "10/07/2025", tiempoCreacion = "11:44", muelle = 22, expediciones = emptyList()),
        Estadillo(5, usuario = Usuario(5,"Admin", TipoUsuario.ADMIN), conductor = Usuario(1,"Juan", TipoUsuario.CHOFER), fechaCreacion = "07/07/2025", tiempoCreacion = "13:00", muelle = 22, expediciones = emptyList()),
        Estadillo(6, usuario = Usuario(5,"Admin", TipoUsuario.ADMIN), conductor = Usuario(2, "Juan", TipoUsuario.CHOFER), fechaCreacion = "08/07/2025", tiempoCreacion = "13:34", muelle = 22, expediciones = emptyList()),
        Estadillo(7, usuario = Usuario(5,"Admin", TipoUsuario.ADMIN), conductor =  Usuario(3,"Juan", TipoUsuario.CHOFER), fechaCreacion = "09/07/2025", tiempoCreacion = "12:32", muelle = 22, expediciones = emptyList()),
        Estadillo(8, usuario = Usuario(5,"Admin", TipoUsuario.ADMIN), conductor =  Usuario(4,"Juan", TipoUsuario.CHOFER), fechaCreacion = "10/07/2025", tiempoCreacion = "11:44", muelle = 22, expediciones = emptyList())
    )
    var estadillos = estadillosInicial
    val hojasCargaInicial = mutableListOf(
        HojaCarga(numHojaCarga = 1, muelle = 22, idUsuario = 1, idPlazas = listOf(1,2,3), tiempoCreacion = "13:00"),
        HojaCarga(numHojaCarga = 2, muelle = 22, idUsuario = 1, idPlazas = listOf(3,4,5), tiempoCreacion = "12:00"),
        HojaCarga(numHojaCarga = 3, muelle = 22, idUsuario = 1, idPlazas = listOf(6,7,8), tiempoCreacion = "11:00"),
        HojaCarga(numHojaCarga = 4, muelle = 22, idUsuario = 1, idPlazas = listOf(9,10,11), tiempoCreacion = "10:00"),
        HojaCarga(numHojaCarga = 5, muelle = 22, idUsuario = 1, idPlazas = listOf(12,13,14), tiempoCreacion = "09:00"),
        HojaCarga(numHojaCarga = 6, muelle = 22, idUsuario = 1, idPlazas = listOf(15,16,17), tiempoCreacion = "08:00"),
    )
    val usuariosInicial = mutableListOf<Usuario>(
        Usuario(numUsuario = 1, nombre = "Admin", tipoUsuario = TipoUsuario.CHOFER),
        Usuario(numUsuario = 2, nombre = "Juan", tipoUsuario = TipoUsuario.CHOFER),
        )
    var usuarios = usuariosInicial

    var hojasCarga = hojasCargaInicial
    suspend fun getBultos(query: String): Response<List<Bulto>> {
        if (query.isNotEmpty()) {
            bultos = bultosInicial.filter { it.idBulto.equals(query) }.toMutableList()
        }else{
            bultos = bultosInicial
        }
        delay(1000)
        return Response.success(bultos)
    }

    suspend fun getExpediciones(): Response<List<Expedicion>> {
        delay(1000)
        return Response.success(expedicionesInicial)
    }
    suspend fun getExpedicionesFiltered(idExpedicion: String, codPlaza:Int, idCliente:Int, codPlazasMultiple:List<String>, idClientesMultiple:List<String>): Response<List<Expedicion>> {
        if (idExpedicion.isNotEmpty() || codPlaza != 0 || idCliente != 0 || codPlazasMultiple.isNotEmpty()) {
            expediciones = expedicionesInicial.filter { it.idExpedicion.equals(idExpedicion) || it.codPlaza == codPlaza || it.cliente.idCliente == idCliente || it.codPlaza.toString() in codPlazasMultiple || it.cliente.idCliente.toString() in idClientesMultiple}.toMutableList()
        }else{
            expediciones = expedicionesInicial
        }
        delay(1000)
        return Response.success(expediciones)
    }

    suspend fun getEstadillos(query: String):Response<List<Estadillo>>{
        if (query.isNotEmpty()) {
            estadillos = estadillosInicial.filter { it.numEst.toString().equals(query)}.toMutableList()
        }
        else{
            estadillos = estadillosInicial
        }
        delay(1000)
        return Response.success(estadillos)
    }

    suspend fun getHojasCarga(query: String): Response<List<HojaCarga>> {
        if (query.isNotEmpty()) {
            hojasCarga = hojasCargaInicial.filter { it.numHojaCarga.toString() == query }.toMutableList()
        }else{
            hojasCarga = hojasCargaInicial
        }
        delay(1000)
        return Response.success(hojasCarga)
    }

    suspend fun getUsuarios(query: String): Response<List<Usuario>> {
        if (query.isNotEmpty()) {
            usuarios = usuariosInicial.filter { it.nombre.equals(query) }.toMutableList()
        }else{
            usuarios = usuariosInicial
        }
        delay(1000)
        return Response.success(usuarios)
    }

    suspend fun postEstadillo(estadillo: Estadillo):Response<Estadillo>{
        estadillosInicial.add(estadillo)
        delay(1000)
        return Response.success(estadillo)
    }

    suspend fun postBulto(bulto: Bulto):Response<Bulto>{
        bultosInicial.add(bulto)
        delay(1000)
        return Response.success(bulto)
    }

    suspend fun postExpedicion(expedicion: Expedicion):Response<Expedicion>{
        expedicionesInicial.add(expedicion)
        delay(1000)
        return Response.success(expedicion)
    }

    suspend fun postHojaCarga(hojaCarga: HojaCarga):Response<HojaCarga>{
        hojasCargaInicial.add(hojaCarga)
        delay(1000)
        return Response.success(hojaCarga)
    }

    suspend fun putBulto(bulto: Bulto):Response<Bulto>{
        bultosInicial.indexOfFirst { it.idBulto == bulto.idBulto }.let {
            bultosInicial[it] = bulto
        }
        delay(1000)
        return Response.success(bulto)
    }

    suspend fun putExpedicion(expedicion: Expedicion):Response<Expedicion>{
        expedicionesInicial.indexOfFirst { it.idExpedicion == expedicion.idExpedicion }.let {
            expedicionesInicial[it] = expedicion
        }
        delay(1000)
        return Response.success(expedicion)
    }

    suspend fun putEstadillo(estadillo: Estadillo):Response<Estadillo> {
        estadillosInicial.indexOfFirst { it.numEst == estadillo.numEst }.let {
            estadillos[it] = estadillo
        }
        delay(1000)
        return Response.success(estadillo)
    }

    suspend fun putHojasCarga(hojaCarga: HojaCarga):Response<HojaCarga> {
        hojasCargaInicial.indexOfFirst { it.numHojaCarga == hojaCarga.numHojaCarga }.let {
            hojasCarga[it] = hojaCarga
        }
        delay(1000)
        return Response.success(hojaCarga)
    }

}