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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController
import com.example.proyectoalmacen.R
import com.example.proyectoalmacen.model.DataClasses.Bulto
import com.example.proyectoalmacen.model.DataClasses.EstadoBulto
import com.example.proyectoalmacen.model.DataClasses.Expedicion
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

data class DialogData(
    val estadoDialogo: EstadoDialogo,
    val expedicion: Expedicion?,
    val dialogoBultoType: DialogoBultoType
)

@Composable
fun RepasoScreen(codPlaza:Int, navController: NavController){
    var expedicionViewModel: ExpedicionViewModel = hiltViewModel()
    val plazasViewModel: PlazasViewModel = hiltViewModel()
    val plaza = plazasViewModel.getPlazaById(codPlaza).nombrePlaza
    var myTextFieldValue by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    var bultoViewModel: BultoViewModel = hiltViewModel()

    var showDialog by remember { mutableStateOf(false) }
    Log.i("Pruebas Dialogo", "variables inicializadas $showDialog")
    var estadoDialogo: EstadoDialogo = EstadoDialogo.ENCONTRADO
    var bulto by remember { mutableStateOf<Bulto?>(null) }
    var expedicion by remember { mutableStateOf<Expedicion?>(null) }
    var dialogTimer = 0L
    val dialogDelay = 500L
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
            val butlosTotales = expedicionViewModel.getExpedicionesBultosConfirmadosByPlazas(codPlaza) + expedicionViewModel.getExpedicionesBultosRepasadosByPlazas(codPlaza)
            CustomTextView(type = TextViewType.TITLE_AND_SUBTITLE, mainText = stringResource(R.string.total_repasados_Text, expedicionViewModel.getExpedicionesBultosRepasadosByPlazas(codPlaza), butlosTotales))
            Spacer(modifier = Modifier.height(10.dp))
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
                    estadoDialogo = if(bulto!!.estadoBulto == EstadoBulto.REPASADO || bulto!!.estadoBulto == EstadoBulto.CARGADO) EstadoDialogo.REPETIDO else EstadoDialogo.ENCONTRADO
                }else{
                    estadoDialogo = EstadoDialogo.NOENCONTRADO
                }
                Log.i("Pruebas Dialogo", "flags puestos a true" )
                showDialog = true
                expedicion = null
            })
            if(bulto != null){
                expedicion = expedicionViewModel.getExpedicionById(bulto!!.idExpedicion)
                if(expedicion != null && expedicion!!.codPlaza != codPlaza) estadoDialogo = EstadoDialogo.EQUIVOCADO
            }
            if(showDialog){
                Log.i("Pruebas Dialogo", "dialogo bulto inicializado${expedicion.let { "null" }}${bulto.let { "null" }}" )
                DialogoBulto(
                    onDismissRequest = { showDialog = false
                                       },
                    estadoDialogo = estadoDialogo,
                    expedicion = expedicion,
                    dialogoBultoType = DialogoBultoType.REPASO,
                )
            }
            if(bulto!= null && bulto!!.estadoBulto == EstadoBulto.DESCARGADO && estadoDialogo != EstadoDialogo.EQUIVOCADO) bultoViewModel.updateBulto(bulto!!.copy(estadoBulto = EstadoBulto.REPASADO))
            Spacer(modifier = Modifier.height(10.dp))
            // Request focus when the composable is launched
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
            ) {
                items(expedicionViewModel.getExpedicionesByPlazas(codPlaza)) { item ->
                    RowExpediciones(item = item, type = RowExpedicionType.REPASAR)
                }
            }
        }
    }
}



