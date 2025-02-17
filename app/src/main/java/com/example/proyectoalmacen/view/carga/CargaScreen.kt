package com.example.proyectoalmacen.view.carga

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.proyectoalmacen.view.commons.basicComponents.CustomIconButton
import com.example.proyectoalmacen.view.commons.basicComponents.CustomInputField
import com.example.proyectoalmacen.view.commons.basicComponents.CustomTopBar
import com.example.proyectoalmacen.view.commons.basicComponents.DialogoBulto
import com.example.proyectoalmacen.view.commons.basicComponents.DialogoBultoType
import com.example.proyectoalmacen.view.commons.basicComponents.EstadoDialogo
import com.example.proyectoalmacen.view.commons.basicComponents.InputFieldType
import com.example.proyectoalmacen.view.commons.basicComponents.RowExpedicionType
import com.example.proyectoalmacen.view.commons.basicComponents.RowExpediciones
import com.example.proyectoalmacen.viewmodel.BultoViewModel
import com.example.proyectoalmacen.viewmodel.ExpedicionViewModel
import com.example.proyectoalmacen.viewmodel.PlazasViewModel

@Composable
fun CargaScreen(navController: NavController, plazas:List<Int>){
    var showDialog by remember { mutableStateOf(false) }
    var estadoDialogo: EstadoDialogo = EstadoDialogo.ENCONTRADO
    val focusRequester = remember { FocusRequester() }
    var myTextFieldValue by remember { mutableStateOf("") }
    var bultoViewModel: BultoViewModel = hiltViewModel()
    var bulto: Bulto? = null
    var expedicion: Expedicion? = null
    var dialogTimer by remember { mutableStateOf(0L) }
    val dialogDelay = 500L
    var expedicionViewModel: ExpedicionViewModel = hiltViewModel()
    val expandedStates = remember { mutableStateListOf<Boolean>().apply {
        addAll(List(plazas.size) { false })
    } }
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
            CustomInputField(focusRequester = focusRequester,value = myTextFieldValue ,type = InputFieldType.READ_ONLY, onValueChange = { newText ->

                val modifiedText = newText.replace("\n","").trim()

                val limitedString = if(modifiedText.length > 20){
                    modifiedText.takeLast(20)
                }else{
                    modifiedText
                }
                myTextFieldValue = limitedString
                bulto = bultoViewModel.getBultosByIdBulto(myTextFieldValue).firstOrNull()

                if(bulto != null){
                    estadoDialogo = if(bulto!!.estadoBulto == EstadoBulto.CARGADO) EstadoDialogo.REPETIDO else EstadoDialogo.ENCONTRADO
                }else{
                    estadoDialogo = EstadoDialogo.NOENCONTRADO
                }
                showDialog = true
                expedicion = null
            })
            if(bulto != null){
                expedicion = expedicionViewModel.getExpedicionById(bulto!!.idExpedicion)
                val posicionExpandida = expandedStates.indexOf(true)

                if(posicionExpandida != -1 && expedicion != null && expedicion!!.codPlaza != plazas[posicionExpandida]) estadoDialogo = EstadoDialogo.EQUIVOCADO
            }
            val currentTime = System.currentTimeMillis()
            if(showDialog && currentTime - dialogTimer > dialogDelay){
                DialogoBulto(onDismissRequest = {showDialog = false}, estadoDialogo = estadoDialogo, expedicion = expedicion, dialogoBultoType = DialogoBultoType.CARGA)
            }
            if(bulto!= null && (bulto!!.estadoBulto == EstadoBulto.DESCARGADO || bulto!!.estadoBulto == EstadoBulto.REPASADO) && estadoDialogo != EstadoDialogo.EQUIVOCADO) bultoViewModel.updateBulto(bulto!!.copy(estadoBulto = EstadoBulto.CARGADO))
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
fun itemRowProvinciascarga(item: Int, index: Int, expandedStates: MutableList<Boolean>){
    var iconoDesplegable by remember { mutableStateOf(Icons.Filled.KeyboardArrowDown) }
    val expedicionViewModel: ExpedicionViewModel = hiltViewModel()
    val plazasViewModel: PlazasViewModel = hiltViewModel()
    val plazaName = plazasViewModel.getPlazaById(item).nombrePlaza
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
                expedicionViewModel.getExpedicionesByPlazas(item).forEach() {
                    RowExpediciones(it, type = RowExpedicionType.CARGAR)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

