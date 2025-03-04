package com.example.proyectoalmacen.view.carga

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.proyectoalmacen.R
import com.example.proyectoalmacen.model.DataClasses.Bulto
import com.example.proyectoalmacen.model.DataClasses.EstadoBulto
import com.example.proyectoalmacen.model.DataClasses.Expedicion
import com.example.proyectoalmacen.model.States.UiState
import com.example.proyectoalmacen.view.commons.basicComponents.CustomIconButton
import com.example.proyectoalmacen.view.commons.basicComponents.CustomInputField
import com.example.proyectoalmacen.view.commons.basicComponents.CustomLoader
import com.example.proyectoalmacen.view.commons.basicComponents.CustomTopBar
import com.example.proyectoalmacen.view.commons.basicComponents.DialogoBulto
import com.example.proyectoalmacen.view.commons.basicComponents.DialogoBultoType
import com.example.proyectoalmacen.view.commons.basicComponents.EstadoDialogo
import com.example.proyectoalmacen.view.commons.basicComponents.InputFieldType
import com.example.proyectoalmacen.view.commons.basicComponents.RowExpedicionType
import com.example.proyectoalmacen.view.commons.basicComponents.RowExpediciones
import com.example.proyectoalmacen.viewmodel.BultoViewModel
import com.example.proyectoalmacen.viewmodel.ExpedicionViewModel
import com.example.proyectoalmacen.viewmodel.ExpedicionesQueryType
import com.example.proyectoalmacen.viewmodel.PlazasViewModel

@Composable
fun CargaScreen(navController: NavController, plazas:List<Int>, bultoViewModel: BultoViewModel = hiltViewModel(), expedicionViewModel: ExpedicionViewModel = hiltViewModel()){
    var showDialog by remember { mutableStateOf(false) }
    var estadoDialogo by remember { mutableStateOf(EstadoDialogo.ENCONTRADO)}
    val focusRequester = remember { FocusRequester() }
    var myTextFieldValue by remember { mutableStateOf("") }
    var dialogTimer by remember { mutableStateOf(0L) }
    val dialogDelay = 500L

    val expandedStates = remember { mutableStateListOf<Boolean>().apply {
        addAll(List(plazas.size) { false })
    } }
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
    LaunchedEffect(Unit) {
        val plazasString = plazas.joinToString(",")
        expedicionViewModel.setSearchQuery(plazasString, ExpedicionesQueryType.MULTIPLECODPLAZA)
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = Modifier.imePadding(),
        topBar = { CustomTopBar(navController = navController, title = stringResource(R.string.carga_text), showConfigButton = false, showHomeButton = true) },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomInputField(focusRequester = focusRequester,value = myTextFieldValue ,type = InputFieldType.READ_ONLY, onValueChange = { newText ->

                val modifiedText = newText.replace("\n", "").trim()

                val limitedString = if (modifiedText.length > 20) {
                    modifiedText.takeLast(20)
                } else {
                    modifiedText
                }
                myTextFieldValue = limitedString



            })
            LaunchedEffect(key1 = myTextFieldValue) {
                if (!loadingBultos && myTextFieldValue.isNotEmpty()) {
                    bultoViewModel.setSearchQuery(myTextFieldValue)
                }
            }

            LaunchedEffect(key1 = bultos) {
                if(!loadingBultos && myTextFieldValue.isNotEmpty()){
                    if (bultos.firstOrNull() != null && bultos.first().estadoBulto != EstadoBulto.CREADO) {
                        estadoDialogo =
                            if (bultos.first().estadoBulto == EstadoBulto.CARGADO) EstadoDialogo.REPETIDO else EstadoDialogo.ENCONTRADO
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
                    val posicionExpandida = expandedStates.indexOf(true)
                    if (posicionExpandida != -1 && plazas[posicionExpandida] != expedicionEscaneada.first().codPlaza) estadoDialogo =
                        EstadoDialogo.EQUIVOCADO
                    if ( bultos.firstOrNull() != null && (bultos.first().estadoBulto == EstadoBulto.DESCARGADO || bultos.first().estadoBulto == EstadoBulto.REPASADO) && estadoDialogo != EstadoDialogo.EQUIVOCADO) bultoViewModel.updateBulto(
                        bultos.first().copy(estadoBulto = EstadoBulto.CARGADO)
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
                    dialogoBultoType = DialogoBultoType.CARGA,
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            // Request focus when the composable is launched
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            Row{
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                ) {
                    itemsIndexed(plazas) { index, item ->
                        itemRowProvinciascarga(item = item, index = index, expandedStates = expandedStates)
                    }
                }
            }

        }
    }
}

@Composable
fun itemRowProvinciascarga(plazasViewModel: PlazasViewModel = hiltViewModel(),expedicionViewModel: ExpedicionViewModel = hiltViewModel(),item: Int, index: Int, expandedStates: MutableList<Boolean>){
    var iconoDesplegable by remember { mutableStateOf(Icons.Filled.KeyboardArrowDown) }
    val plazaName = plazasViewModel.getPlazaById(item).nombrePlaza
    val uiStateExpedicion by expedicionViewModel.expedicionListFiltred.collectAsState()
    var loadingExpediciones by remember { mutableStateOf(false) }
    val expediciones by remember {
        derivedStateOf {
            (uiStateExpedicion as? UiState.Success<List<Expedicion>>)?.data ?: emptyList()
        }
    }
    when (uiStateExpedicion) {
        is UiState.Loading -> {
            loadingExpediciones = true
            CustomLoader(loadingExpediciones)
        }
        is UiState.Success -> {
            Log.i("Expedicion Success", "${(uiStateExpedicion as UiState.Success<*>).data}")
            loadingExpediciones = false
        }
        is UiState.Error -> Log.e("Expedicion,", (uiStateExpedicion as UiState.Error).message)
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        CustomIconButton(
            onClick = {
                iconoDesplegable =
                    if (expandedStates[index]) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp
                expandedStates[index] = !expandedStates[index]
                expandedStates.forEachIndexed { indexTwo, _ ->
                    if (indexTwo != index) {
                        expandedStates[indexTwo] = false
                    }
                }
            },
            text = plazaName,
            icon = iconoDesplegable,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            shape = RectangleShape,
            containerColor = colorScheme.primaryContainer,
        )

        if (expandedStates[index]) {
            // Expanded content
            Column {
                expediciones.filter { it.codPlaza == item }.forEach() {
                    RowExpediciones(it, type = RowExpedicionType.CARGAR)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

