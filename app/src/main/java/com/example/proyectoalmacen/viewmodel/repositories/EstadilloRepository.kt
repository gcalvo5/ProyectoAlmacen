package com.example.proyectoalmacen.viewmodel.repositories


import android.os.Build
import androidx.annotation.RequiresApi
import com.example.proyectoalmacen.model.DataClasses.Estadillo
import com.example.proyectoalmacen.model.DataClasses.EstadoBulto
import com.example.proyectoalmacen.model.DataClasses.Expedicion
import com.example.proyectoalmacen.model.DataClasses.TipoUsuario
import com.example.proyectoalmacen.model.DataClasses.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.text.format

@Singleton
class EstadilloRepository @Inject constructor(private val expedicionRepository: ExpedicionRepository) {
    private val _estadillos = MutableStateFlow<List<Estadillo>>(
        listOf(
            Estadillo(1, usuario = Usuario(5,"Admin", TipoUsuario.ADMIN), conductor = Usuario(1,"Juan", TipoUsuario.CHOFER), fechaCreacion = "07/07/2025", tiempoCreacion = "13:00", muelle = 22, expediciones = emptyList()),
            Estadillo(2, usuario = Usuario(5,"Admin", TipoUsuario.ADMIN), conductor = Usuario(2, "Juan", TipoUsuario.CHOFER), fechaCreacion = "08/07/2025", tiempoCreacion = "13:34", muelle = 22, expediciones = emptyList()),
            Estadillo(3, usuario = Usuario(5,"Admin", TipoUsuario.ADMIN), conductor =  Usuario(3,"Juan", TipoUsuario.CHOFER), fechaCreacion = "09/07/2025", tiempoCreacion = "12:32", muelle = 22, expediciones = emptyList()),
            Estadillo(4, usuario = Usuario(5,"Admin", TipoUsuario.ADMIN), conductor =  Usuario(4,"Juan", TipoUsuario.CHOFER), fechaCreacion = "10/07/2025", tiempoCreacion = "11:44", muelle = 22, expediciones = emptyList()),
            Estadillo(5, usuario = Usuario(5,"Admin", TipoUsuario.ADMIN), conductor = Usuario(1,"Juan", TipoUsuario.CHOFER), fechaCreacion = "07/07/2025", tiempoCreacion = "13:00", muelle = 22, expediciones = emptyList()),
            Estadillo(6, usuario = Usuario(5,"Admin", TipoUsuario.ADMIN), conductor = Usuario(2, "Juan", TipoUsuario.CHOFER), fechaCreacion = "08/07/2025", tiempoCreacion = "13:34", muelle = 22, expediciones = emptyList()),
            Estadillo(7, usuario = Usuario(5,"Admin", TipoUsuario.ADMIN), conductor =  Usuario(3,"Juan", TipoUsuario.CHOFER), fechaCreacion = "09/07/2025", tiempoCreacion = "12:32", muelle = 22, expediciones = emptyList()),
            Estadillo(8, usuario = Usuario(5,"Admin", TipoUsuario.ADMIN), conductor =  Usuario(4,"Juan", TipoUsuario.CHOFER), fechaCreacion = "10/07/2025", tiempoCreacion = "11:44", muelle = 22, expediciones = emptyList())
        )
    )
    val estadillos = _estadillos.asStateFlow()

    init {
        getAllEstadillos().onEach {
            _estadillos.value = it
        }.launchIn(CoroutineScope(Dispatchers.IO))
    }

    fun getAllEstadillos(): Flow<List<Estadillo>> {
        return estadillos.combine(expedicionRepository.expediciones) { estadillos, expediciones ->
            estadillos.map { estadillo ->
                estadillo.copy(expediciones = expediciones.filter { it.idEstadillo == estadillo.numEst })
            }
            }.let {
                calculateBultosInEstadillos(it)
            }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun crearEstadillo(muelle: String, conductor: String, usuario: Usuario = Usuario(5,"Admin", TipoUsuario.ADMIN)) {
        val currentDateTime: LocalDateTime = LocalDateTime.now()
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val currentDate: String = currentDateTime.format(dateFormatter)
        val currentTime: String = currentDateTime.format(timeFormatter)
        val estadilloNuevo = Estadillo(muelle = muelle.toInt(), conductor = Usuario(1, conductor, TipoUsuario.CHOFER), numEst = getLastEstadilloId() + 1, usuario = usuario, fechaCreacion = currentDate, tiempoCreacion = currentTime)
        val currentEstadillos = _estadillos.value.toMutableList()
        currentEstadillos.add(estadilloNuevo)
        _estadillos.value = currentEstadillos.toList()
    }

    fun getEstadilloByID(idEstadillo: Int): Flow<List<Estadillo>> {
        return estadillos.map { estadillos ->
            estadillos.filter { it.numEst == idEstadillo }
        }
    }

    private fun getLastEstadilloId():Int{
        val lastEstadillo = _estadillos.value.last()
        val lastEstadilloId = lastEstadillo.numEst
        return lastEstadilloId

    }

    fun calculateBultosInEstadillos(estadillos: Flow<List<Estadillo>>): Flow<List<Estadillo>> {
        return estadillos.map { estadilloList ->
            estadilloList.map { estadillo ->
                var numBultos = 0
                var numBultosConfirmados = 0
                var numBultosRepasados = 0
                var numbultosCargados = 0
                estadillo.expediciones.forEach { expedicion ->
                    numBultos += expedicion.numBultos
                    numBultosConfirmados += expedicion.numBultosConfirmados
                    numBultosRepasados += expedicion.numBultosRepasados
                    numbultosCargados += expedicion.numbultosCargados
                }
                estadillo.copy(
                    numBultosTotal = numBultos,
                    numBultosConfirmados = numBultosConfirmados,
                    numBultosRepasados = numBultosRepasados,
                    numBultosCargados = numbultosCargados
                )
            }
        }
    }
}