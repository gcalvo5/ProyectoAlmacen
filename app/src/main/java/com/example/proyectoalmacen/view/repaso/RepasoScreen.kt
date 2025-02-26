package com.example.proyectoalmacen.view.repaso

import CustomTextView
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectoalmacen.R
import com.example.proyectoalmacen.model.DataClasses.Bulto
import com.example.proyectoalmacen.model.DataClasses.EstadoBulto
import com.example.proyectoalmacen.model.DataClasses.Expedicion
import com.example.proyectoalmacen.model.States.UiState
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
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport

data class DialogData(
    val estadoDialogo: EstadoDialogo,
    val expedicion: Expedicion?,
    val dialogoBultoType: DialogoBultoType
)

@Composable
fun RepasoScreen(plazasViewModel: PlazasViewModel = hiltViewModel(),expedicionViewModel: ExpedicionViewModel = hiltViewModel(),codPlaza:Int, navController: NavController, bultoViewModel: BultoViewModel = hiltViewModel()){
    val plaza = plazasViewModel.getPlazaById(codPlaza).nombrePlaza
    var myTextFieldValue by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    var showDialog by remember { mutableStateOf(false) }
    Log.i("Pruebas Dialogo", "variables inicializadas $showDialog")
    var estadoDialogo by remember { mutableStateOf(EstadoDialogo.ENCONTRADO) }
    val uiStateBultos by bultoViewModel.bultosListFiltred.collectAsState()
    val uiStateExpedicionEscaneada by expedicionViewModel.expedicionNoUpdate.collectAsState()
    var loadingBultos by remember { mutableStateOf(false) }
    var loadingExpediciones by remember { mutableStateOf(false) }
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
        is UiState.Error -> Log.e("Expedicion Escaneada Error,", (uiStateBultos as UiState.Error).message)
    }
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
    val uiStateExpedicion by expedicionViewModel.expedicionListFiltred.collectAsState()
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
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = Modifier.imePadding(),
        topBar = { CustomTopBar(navController = navController, title = plaza, showConfigButton = false, showHomeButton = true) },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
                var butlosTotales = 0
                var bultosRepasados = 0
                if (expedicionViewModel.getListNumBultosTotales().isNotEmpty()) {
                    butlosTotales = expedicionViewModel.getListNumBultosTotales(codPlaza)
                        .get(1) + expedicionViewModel.getListNumBultosTotales(codPlaza).get(2)
                    bultosRepasados = expedicionViewModel.getListNumBultosTotales(codPlaza).get(2)
                }
                CustomTextView(
                    type = TextViewType.TITLE_AND_SUBTITLE,
                    mainText = stringResource(
                        R.string.total_repasados_Text,
                        bultosRepasados,
                        butlosTotales
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                CustomInputField(
                    focusRequester = focusRequester,
                    value = myTextFieldValue,
                    type = InputFieldType.READ_ONLY,
                    onValueChange = { newText ->

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
                    if (bultos.firstOrNull() != null) {
                        estadoDialogo =
                            if (bultos.first().estadoBulto == EstadoBulto.REPASADO || bultos.first().estadoBulto == EstadoBulto.CARGADO) EstadoDialogo.REPETIDO else EstadoDialogo.ENCONTRADO
                    } else {
                        estadoDialogo = EstadoDialogo.NOENCONTRADO
                    }
                    Log.i("Pruebas Dialogo", "flags puestos a true")
                    if (bultos.firstOrNull() != null) {
                        if (!loadingExpediciones) {
                            expedicionViewModel.setSearchQuery(
                                bultos.first().idExpedicion,
                                ExpedicionesQueryType.ID,
                                true
                            )
                        }
                    }
                }
            }
            LaunchedEffect(key1 = expedicionEscaneada) {
                if(expedicionEscaneada.isNotEmpty() && !loadingExpedicionEscaneada){
                    if (expedicionEscaneada.first().codPlaza != codPlaza) estadoDialogo =
                        EstadoDialogo.EQUIVOCADO
                    if ( bultos.firstOrNull() != null && bultos.first().estadoBulto == EstadoBulto.DESCARGADO && estadoDialogo != EstadoDialogo.EQUIVOCADO) bultoViewModel.updateBulto(
                        bultos.first().copy(estadoBulto = EstadoBulto.REPASADO)
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
                        dialogoBultoType = DialogoBultoType.REPASO,
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                // Request focus when the composable is launched
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            LaunchedEffect(key1 = codPlaza) {
                expedicionViewModel.setSearchQuery(
                    codPlaza.toString(),
                    ExpedicionesQueryType.CODPLAZA
                )

            }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {

                    items(expediciones) { item ->
                        RowExpediciones(item = item, type = RowExpedicionType.REPASAR)
                    }
                }
            }

    }
}



