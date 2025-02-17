package com.example.proyectoalmacen.view.estadillo

import CustomTextView
import TextViewConfig
import TextViewType
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
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
import com.example.proyectoalmacen.model.DataClasses.Expedicion
import com.example.proyectoalmacen.model.DataClasses.Recogida
import com.example.proyectoalmacen.model.DataClasses.Usuario
import com.example.proyectoalmacen.view.commons.basicComponents.CustomComparationText
import com.example.proyectoalmacen.view.commons.basicComponents.CustomIconButton
import com.example.proyectoalmacen.view.commons.basicComponents.CustomInputField
import com.example.proyectoalmacen.view.commons.basicComponents.CustomSegmentedButtons
import com.example.proyectoalmacen.view.commons.basicComponents.CustomTopBar
import com.example.proyectoalmacen.view.commons.basicComponents.InputFieldType
import com.example.proyectoalmacen.view.commons.basicComponents.RowExpedicionType
import com.example.proyectoalmacen.view.commons.basicComponents.RowExpediciones
import com.example.proyectoalmacen.view.commons.basicComponents.SegmentedButtonOption
import com.example.proyectoalmacen.viewmodel.EstadilloViewModel
import com.example.proyectoalmacen.viewmodel.ExpedicionViewModel
import com.example.proyectoalmacen.viewmodel.RecogidaViewModel
import com.example.proyectoalmacen.viewmodel.UsuarioViewModel

@Composable
fun EstadilloScreen(navController: NavController, idEstadillo: Int, nombreChofer: String){
    var recogidaViewModel: RecogidaViewModel = hiltViewModel()
    var estadilloViewModel: EstadilloViewModel = hiltViewModel()
    var usuarioViewModel: UsuarioViewModel = hiltViewModel()

    var myTextFieldValue by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    val estadillo = estadilloViewModel.getEstadilloByID(idEstadillo)
    val conductor:Usuario? = usuarioViewModel.getUsuarioByName(nombreChofer).firstOrNull()
    val recogidas = conductor?.let { recogidaViewModel.getRecogidasByIdConductor(it.numUsuario) }

    val expandedStates = remember { mutableStateListOf<Boolean>().apply {
        if (recogidas != null) {
            addAll(List(recogidas.size) { false })
        }
    } }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = Modifier.imePadding(),
        topBar = { CustomTopBar(navController = navController, title = stringResource(R.string.estadillo_text), showConfigButton = false, showHomeButton = true) },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row {
                CustomInputField(focusRequester = focusRequester,value = myTextFieldValue ,type = InputFieldType.READ_ONLY, onValueChange = { newText ->

                    val modifiedText = newText.replace("\n", "").trim()

                    val limitedString = if (modifiedText.length > 20) {
                        modifiedText.takeLast(20)
                    } else {
                        modifiedText
                    }
                    myTextFieldValue = limitedString
                }
                )
            }
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                CustomSegmentedButtons(options = listOf(
                    SegmentedButtonOption("", Icons.Filled.Home),
                    SegmentedButtonOption("", Icons.Filled.Settings),
                    SegmentedButtonOption("", Icons.Filled.Settings),
                    SegmentedButtonOption("", Icons.Filled.Settings),
                            SegmentedButtonOption("", Icons.Filled.Settings))
                    , onOptionSelected = {})
            }
            Row{
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                ) {
                    itemsIndexed(recogidas!!) { index, item ->
                        ItemRowEstadillo(item = item, index = index, expandedStates = expandedStates)
                    }
                }
            }
        }


    }
}

@Composable
fun ItemRowEstadillo(index: Int, item: Recogida, expandedStates: MutableList<Boolean>) {
    var iconoDesplegable by remember { mutableStateOf(Icons.Filled.KeyboardArrowDown) }
    var expedicionViewModel: ExpedicionViewModel = hiltViewModel()
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
                expedicionViewModel.getExpedicionesByIdCliente(item.clienteRecogida.idCliente).forEach() {
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