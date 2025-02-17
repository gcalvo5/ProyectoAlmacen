package com.example.proyectoalmacen.viewmodel.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.proyectoalmacen.model.DataClasses.Cliente
import com.example.proyectoalmacen.model.DataClasses.Estadillo
import com.example.proyectoalmacen.model.DataClasses.Expedicion
import com.example.proyectoalmacen.model.DataClasses.HojaCarga
import com.example.proyectoalmacen.model.DataClasses.TipoUsuario
import com.example.proyectoalmacen.model.DataClasses.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HojaCargaRepository @Inject constructor(val expedicionRepository: ExpedicionRepository) {
    private val _hojasCarga = MutableStateFlow<List<HojaCarga>>(
        listOf(
            HojaCarga(numHojaCarga = 1, muelle = 22, idUsuario = 1, idPlazas = listOf(1,2,3), tiempoCreacion = "13:00"),
            HojaCarga(numHojaCarga = 2, muelle = 22, idUsuario = 1, idPlazas = listOf(3,4,5), tiempoCreacion = "12:00"),
            HojaCarga(numHojaCarga = 3, muelle = 22, idUsuario = 1, idPlazas = listOf(6,7,8), tiempoCreacion = "11:00"),
            HojaCarga(numHojaCarga = 4, muelle = 22, idUsuario = 1, idPlazas = listOf(9,10,11), tiempoCreacion = "10:00"),
            HojaCarga(numHojaCarga = 5, muelle = 22, idUsuario = 1, idPlazas = listOf(12,13,14), tiempoCreacion = "09:00"),
            HojaCarga(numHojaCarga = 6, muelle = 22, idUsuario = 1, idPlazas = listOf(15,16,17), tiempoCreacion = "08:00"),
        )
    )
    val hojasCarga = _hojasCarga.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun crearHojaCarga(muelle: String, listaPlazasId:List<Int>, idUsuario:Int = 1) {
        val currentDateTime: LocalDateTime = LocalDateTime.now()
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val currentDate: String = currentDateTime.format(dateFormatter)
        val currentTime: String = currentDateTime.format(timeFormatter)
        val hojaCargaNueva = HojaCarga(muelle = muelle.toInt(), idPlazas = listaPlazasId, idUsuario = idUsuario, numHojaCarga = getLastHojaCargaId() + 1, fechaCreacion = currentDate, tiempoCreacion = currentTime)
        val currentHojasCarga = _hojasCarga.value.toMutableList()
        currentHojasCarga.add(hojaCargaNueva)
        _hojasCarga.value = currentHojasCarga.toList()
    }

    fun getAllHojaCarga(): Flow<List<HojaCarga>> {
        return hojasCarga
    }

    private fun getLastHojaCargaId():Int{
        val lastHojaCarga = _hojasCarga.value.last()
        val lastHojaCargaId = lastHojaCarga.numHojaCarga
        return lastHojaCargaId

    }
}