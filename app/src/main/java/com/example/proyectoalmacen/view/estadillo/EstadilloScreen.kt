package com.example.proyectoalmacen.view.estadillo

import CustomTextView
import TextViewConfig
import TextViewType
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.proyectoalmacen.R
import com.example.proyectoalmacen.model.DataClasses.Bulto
import com.example.proyectoalmacen.model.DataClasses.TipoBulto
import com.example.proyectoalmacen.model.DataClasses.Estadillo
import com.example.proyectoalmacen.model.DataClasses.EstadoBulto
import com.example.proyectoalmacen.model.DataClasses.Expedicion
import com.example.proyectoalmacen.model.DataClasses.Recogida
import com.example.proyectoalmacen.model.DataClasses.Usuario
import com.example.proyectoalmacen.model.States.UiState
import com.example.proyectoalmacen.view.commons.basicComponents.CustomComparationText
import com.example.proyectoalmacen.view.commons.basicComponents.CustomIconButton
import com.example.proyectoalmacen.view.commons.basicComponents.CustomInputField
import com.example.proyectoalmacen.view.commons.basicComponents.CustomLoader
import com.example.proyectoalmacen.view.commons.basicComponents.CustomSegmentedButtons
import com.example.proyectoalmacen.view.commons.basicComponents.CustomTopBar
import com.example.proyectoalmacen.view.commons.basicComponents.DialogoBulto
import com.example.proyectoalmacen.view.commons.basicComponents.DialogoBultoType
import com.example.proyectoalmacen.view.commons.basicComponents.EstadoDialogo
import com.example.proyectoalmacen.view.commons.basicComponents.InputFieldType
import com.example.proyectoalmacen.view.commons.basicComponents.RowExpedicionType
import com.example.proyectoalmacen.view.commons.basicComponents.RowExpediciones
import com.example.proyectoalmacen.view.commons.basicComponents.SegmentedButtonOption
import com.example.proyectoalmacen.viewmodel.BultoViewModel
import com.example.proyectoalmacen.viewmodel.EstadilloViewModel
import com.example.proyectoalmacen.viewmodel.ExpedicionViewModel
import com.example.proyectoalmacen.viewmodel.ExpedicionesQueryType
import com.example.proyectoalmacen.viewmodel.RecogidaViewModel
import com.example.proyectoalmacen.viewmodel.UsuarioViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted

@Composable
fun EstadilloScreen(bultoViewModel: BultoViewModel = hiltViewModel(), expedicionViewModel: ExpedicionViewModel = hiltViewModel(), recogidaViewModel: RecogidaViewModel = hiltViewModel(), usuarioViewModel: UsuarioViewModel = hiltViewModel(), navController: NavController, idEstadillo: Int, nombreChofer: String){

    var myTextFieldValue by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    var showDialog by remember { mutableStateOf(false) }
    var tipoBultoSeleccionado by remember { mutableStateOf(TipoBulto.PALET) }
    val uiStateUsuarios by usuarioViewModel.usuariosList.collectAsState()
    var loadingUsuarios by remember { mutableStateOf(false) }
    val usuarios by remember {
        derivedStateOf {
            (uiStateUsuarios as? UiState.Success<List<Usuario>>)?.data ?: emptyList()
        }
    }
    when (uiStateUsuarios) {
        is UiState.Loading -> {
            loadingUsuarios = true
            CustomLoader(loadingUsuarios)
        }
        is UiState.Success -> {
            loadingUsuarios = false
            Log.i("Expediciones Success", "${(uiStateUsuarios as UiState.Success<*>).data}")
        }
        is UiState.Error -> Log.e("Expediciones Error,", (uiStateUsuarios as UiState.Error).message)
    }
    val conductor:Usuario? = usuarios.find { it.nombre == nombreChofer }
    val recogidas = conductor?.let { recogidaViewModel.getRecogidasByIdConductor(it.numUsuario) }
    var estadoDialogo by remember { mutableStateOf(EstadoDialogo.ENCONTRADO) }

    val expandedStates = remember { mutableStateListOf<Boolean>().apply {
        if (recogidas != null) {
            addAll(List(recogidas!!.size) { false })
        }
    } }
    if (recogidas != null && expandedStates.isEmpty()) {
        expandedStates.addAll(List(recogidas!!.size) { false })
    }
    val uiStateExpedicionEscaneada by expedicionViewModel.expedicionNoUpdate.collectAsState()
    var loadingBultos by remember { mutableStateOf(false) }
    var loadingExpedicionEscaneada by remember { mutableStateOf(false) }
    val expedicionEscaneada by remember {
        derivedStateOf {
            (uiStateExpedicionEscaneada as? UiState.Success<List<Expedicion>>)?.data ?: emptyList()
        }
    }
    when (uiStateExpedicionEscaneada) {
        is UiState.Loading -> {
            loadingExpedicionEscaneada = true
            CustomLoader(loadingExpedicionEscaneada)
        }
        is UiState.Success ->{
            Log.i("Expedicion Escaneada Success", "${(uiStateExpedicionEscaneada as UiState.Success<List<Expedicion>>).data}")
            loadingExpedicionEscaneada = false
        }
        is UiState.Error -> Log.e("Expedicion Escaneada Error,", (uiStateExpedicionEscaneada as UiState.Error).message)
    }
    val uiStateBultos by bultoViewModel.bultosListFiltred.collectAsState()
    val bultos by remember {
        derivedStateOf {
            (uiStateBultos as? UiState.Success<List<Bulto>>)?.data ?: emptyList()
        }
    }
    when (uiStateBultos) {
        is UiState.Loading -> {
            loadingBultos = true
            CustomLoader(loadingBultos)
        }
        is UiState.Success ->{
            Log.i("Bultos Success", "${(uiStateBultos as UiState.Success<List<Bulto>>).data}")
            loadingBultos = false
        }
        is UiState.Error -> Log.e("Bultos Error,", (uiStateBultos as UiState.Error).message)
    }

    LaunchedEffect(expandedStates) {
        val idClientes = recogidas?.joinToString(",")
        idClientes?.let { expedicionViewModel.setSearchQuery(it, ExpedicionesQueryType.MULTIPLEIDCLIENTE) }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = Modifier.imePadding(),
        topBar = { CustomTopBar(navController = navController, title = stringResource(R.string.estadillo_text), showConfigButton = false, showHomeButton = true) },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                CustomInputField(focusRequester = focusRequester,value = myTextFieldValue ,type = InputFieldType.READ_ONLY, onValueChange = { newText ->

                    val modifiedText = newText.replace("\n", "").trim()

                    val limitedString = if (modifiedText.length > 20) {
                        modifiedText.takeLast(20)
                    } else {
                        modifiedText
                    }
                    myTextFieldValue = limitedString



                })
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
                LaunchedEffect(key1 = myTextFieldValue) {
                    if (!loadingBultos && myTextFieldValue.isNotEmpty()) {
                        bultoViewModel.setSearchQuery(myTextFieldValue)
                    }
                }

                LaunchedEffect(key1 = bultos) {
                    if(!loadingBultos && myTextFieldValue.isNotEmpty()){
                        if (bultos.firstOrNull() != null) {
                            estadoDialogo =
                                if (bultos.first().estadoBulto == EstadoBulto.DESCARGADO) EstadoDialogo.REPETIDO else EstadoDialogo.ENCONTRADO
                        } else {
                            estadoDialogo = EstadoDialogo.NOENCONTRADO
                        }
                        Log.i("Pruebas Dialogo", "flags puestos a true")
                        if (bultos.firstOrNull() != null) {
                            expedicionViewModel.setSearchQuery(
                                bultos.first().idExpedicion,
                                ExpedicionesQueryType.ID,
                                true
                            )
                        }
                    }
                }
                LaunchedEffect(key1 = expedicionEscaneada) {
                    if(expedicionEscaneada.isNotEmpty() && !loadingExpedicionEscaneada){
                        if ( bultos.firstOrNull() != null && bultos.first().estadoBulto == EstadoBulto.CREADO) bultoViewModel.updateBulto(
                            bultos.first().copy(estadoBulto = EstadoBulto.DESCARGADO, tipoBulto = tipoBultoSeleccionado)
                        )
                        showDialog = true
                    }

                }

                if (showDialog && expedicionEscaneada.isNotEmpty()) {
                    DialogoBulto(
                        onDismissRequest = {
                            showDialog = false
                        },
                        estadoDialogo = estadoDialogo,
                        expedicion = expedicionEscaneada.first(),
                        dialogoBultoType = DialogoBultoType.DESCARGA,
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                val options = listOf(
                    SegmentedButtonOption("", Icons.Filled.Home),
                    SegmentedButtonOption("", Icons.Filled.Favorite),
                    SegmentedButtonOption("", Icons.Filled.AccountBox),
                    SegmentedButtonOption("", Icons.Filled.Build),
                    SegmentedButtonOption("", Icons.Filled.LocationOn))
                CustomSegmentedButtons(options = options
                    , onOptionSelected = {
                        tipoBultoSeleccionado = when(options[it].icon){
                            Icons.Filled.Home -> TipoBulto.PALET
                            Icons.Filled.Favorite -> TipoBulto.BULTO
                            Icons.Filled.AccountBox -> TipoBulto.CONTENEDOR
                            Icons.Filled.Build -> TipoBulto.BIDON
                            Icons.Filled.LocationOn -> TipoBulto.ROLLO
                            else -> TipoBulto.PALET
                        }
                    })
            }
            Row(modifier = Modifier.fillMaxSize().navigationBarsPadding()){
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                ) {
                    if(recogidas != null) {
                        itemsIndexed(recogidas!!) { index, item ->
                            ItemRowEstadillo(
                                item = item,
                                index = index,
                                expandedStates = expandedStates
                            )
                        }
                    }
                }
            }
        }


    }
}

@Composable
fun ItemRowEstadillo(expedicionViewModel: ExpedicionViewModel = hiltViewModel(),index: Int, item: Recogida, expandedStates: MutableList<Boolean>) {
    var iconoDesplegable by remember { mutableStateOf(Icons.Filled.KeyboardArrowDown) }
    val uiStateExpedicion by expedicionViewModel.expedicionListFiltred.collectAsState()
    val expediciones by remember {
        derivedStateOf {
            (uiStateExpedicion as? UiState.Success<List<Expedicion>>)?.data ?: emptyList()
        }
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        CustomIconButton(
            onClick = {
                iconoDesplegable =
                    if (expandedStates[index]) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp
                expandedStates[index] = !expandedStates[index]
            },
            text = item.clienteRecogida.nombreCliente,
            icon = iconoDesplegable,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            shape = RectangleShape,
            containerColor = colorScheme.primaryContainer,
        )

        if (expandedStates[index]) {
            val listaDeProvinciasActuales:MutableList<String> = mutableListOf()
            val diccionarioExpediciones: MutableMap<String, MutableList<Expedicion>> = mutableMapOf()
            // Expanded content
            Column {
                expediciones.filter { expedicion -> expedicion.cliente.idCliente == item.clienteRecogida.idCliente }.forEach() {
                    if (!listaDeProvinciasActuales.contains(it.provincia)){
                        listaDeProvinciasActuales.add(it.provincia)
                        diccionarioExpediciones.put(it.provincia, mutableListOf(it))
                    }else{
                        diccionarioExpediciones[it.provincia]?.add(it)
                    }
                }
                val expandedStatesProvincias = remember { mutableStateListOf<Boolean>().apply { addAll(List(listaDeProvinciasActuales.size) { false }) } }
                listaDeProvinciasActuales.forEachIndexed { index, item ->
                    ItemRowListaProvincias(item = item, expandedStates = expandedStatesProvincias, diccionarioExpedicionesProvincia = diccionarioExpediciones, index = index)
                }

            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun ItemRowListaProvincias(item: String,expandedStates: MutableList<Boolean>, diccionarioExpedicionesProvincia: MutableMap<String, MutableList<Expedicion>>, index: Int){


        var iconoDesplegable by remember { mutableStateOf(Icons.Filled.KeyboardArrowDown) }
        Column(modifier = Modifier.fillMaxWidth()) {
            CustomIconButton(
                onClick = {
                    iconoDesplegable =
                        if (expandedStates[index]) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp
                    expandedStates[index] = !expandedStates[index]
                },
                text = item,
                icon = iconoDesplegable,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                shape = RectangleShape,
                containerColor = colorScheme.secondaryContainer
            )

            if (expandedStates[index]) {
                // Expanded content
                Column (Modifier.fillMaxWidth()){
                    diccionarioExpedicionesProvincia[item]?.forEach {
                        RowExpediciones(it, type = RowExpedicionType.DESCARGAR)
                    }
                }
            }
        }

}