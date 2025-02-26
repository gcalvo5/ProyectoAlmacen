package com.example.proyectoalmacen.view.commons.basicComponents

import CustomTextView
import TextViewConfig
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectoalmacen.R
import com.example.proyectoalmacen.model.DataClasses.Expedicion
import kotlinx.coroutines.delay

enum class DialogoBultoType{
    DESCARGA,
    REPASO,
    CARGA
}

enum class EstadoDialogo{
    ENCONTRADO,
    NOENCONTRADO,
    REPETIDO,
    EQUIVOCADO
}

data class DialogData(
    val estadoDialogo: EstadoDialogo,
    val expedicion: Expedicion?,
    val dialogoBultoType: DialogoBultoType
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogoBulto(
    onDismissRequest: () -> Unit,
    estadoDialogo: EstadoDialogo,
    expedicion: Expedicion?,
    dialogoBultoType: DialogoBultoType
){
    Log.i("Pruebas Dialogo", "Dialogo bulto creado" )
    val colorFondo = when(estadoDialogo){
        EstadoDialogo.ENCONTRADO -> colorScheme.surfaceContainerLow
        EstadoDialogo.NOENCONTRADO, EstadoDialogo.EQUIVOCADO -> colorScheme.errorContainer
        EstadoDialogo.REPETIDO -> colorScheme.surfaceContainerHigh
    }
    val icono = when(estadoDialogo){
        EstadoDialogo.ENCONTRADO -> Icons.Filled.CheckCircle
        EstadoDialogo.NOENCONTRADO, EstadoDialogo.EQUIVOCADO -> Icons.Filled.Clear
        EstadoDialogo.REPETIDO -> Icons.Filled.Info
    }
    var texto = ""
    var textoSecundario = ""
    when(dialogoBultoType){
        DialogoBultoType.DESCARGA -> {
            texto = when(estadoDialogo){
                EstadoDialogo.ENCONTRADO -> stringResource(R.string.encontrado_titulo_text)
                EstadoDialogo.NOENCONTRADO -> stringResource(R.string.no_encontrado_titulo_text)
                EstadoDialogo.REPETIDO -> stringResource(R.string.repetido_titulo_text)
                EstadoDialogo.EQUIVOCADO -> stringResource(R.string.equivocado_titulo_text)
            }
            textoSecundario = when(estadoDialogo){
                EstadoDialogo.ENCONTRADO -> stringResource(R.string.encontrado_text_descarga)
                EstadoDialogo.NOENCONTRADO -> stringResource(R.string.no_encontrado_text)
                EstadoDialogo.REPETIDO -> stringResource(R.string.repetido_text_descarga)
                EstadoDialogo.EQUIVOCADO -> stringResource(R.string.equivocado_text, expedicion!!.provincia)
            }
        }
        DialogoBultoType.REPASO -> {
            texto = when(estadoDialogo){
                EstadoDialogo.ENCONTRADO -> stringResource(R.string.encontrado_titulo_text)
                EstadoDialogo.NOENCONTRADO -> stringResource(R.string.no_encontrado_titulo_text)
                EstadoDialogo.REPETIDO -> stringResource(R.string.repetido_titulo_text)
                EstadoDialogo.EQUIVOCADO -> stringResource(R.string.equivocado_titulo_text)
            }
            textoSecundario = when(estadoDialogo){
                EstadoDialogo.ENCONTRADO -> stringResource(R.string.encontrado_text)
                EstadoDialogo.NOENCONTRADO -> stringResource(R.string.no_encontrado_text)
                EstadoDialogo.REPETIDO -> stringResource(R.string.repetido_text)
                EstadoDialogo.EQUIVOCADO -> stringResource(R.string.equivocado_text, expedicion!!.provincia)
            }
        }
        DialogoBultoType.CARGA -> {
            texto = when(estadoDialogo){
                EstadoDialogo.ENCONTRADO -> stringResource(R.string.encontrado_titulo_text)
                EstadoDialogo.NOENCONTRADO -> stringResource(R.string.no_encontrado_titulo_text)
                EstadoDialogo.REPETIDO -> stringResource(R.string.repetido_titulo_text)
                EstadoDialogo.EQUIVOCADO -> stringResource(R.string.equivocado_titulo_text)
            }
            textoSecundario = when(estadoDialogo){
                EstadoDialogo.ENCONTRADO -> stringResource(R.string.encontrado_text_carga)
                EstadoDialogo.NOENCONTRADO -> stringResource(R.string.no_encontrado_text)
                EstadoDialogo.REPETIDO -> stringResource(R.string.repetido_text_carga)
                EstadoDialogo.EQUIVOCADO -> stringResource(R.string.equivocado_text, expedicion!!.provincia)
            }
        }
    }

    BasicAlertDialog (
        onDismissRequest = onDismissRequest,
        content = {
            var cerrarAutomaticamente by remember { mutableStateOf(true) }
            Card(colors = CardDefaults.cardColors(containerColor = colorFondo, contentColor = colorScheme.primary), onClick = { cerrarAutomaticamente = false }) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(icono, "Icono de dialogo", tint = colorScheme.primary)
                    Spacer(modifier = Modifier.height(20.dp))
                    CustomTextView(type = TextViewType.TITLE_AND_SUBTITLE, mainText = texto, subtitle = textoSecundario)
                    if(expedicion != null && estadoDialogo != EstadoDialogo.NOENCONTRADO){
                        Spacer(modifier = Modifier.height(20.dp))
                        Card (colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceContainer, contentColor = colorScheme.primary), modifier = Modifier.fillMaxWidth().height(120.dp).padding(5.dp)){
                            Column (verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally){
                                CustomTextView(type = TextViewType.TITLE_AND_SUBTITLE, mainText = stringResource(
                                    R.string.info_expedicion_text)
                                )
                                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                                    CustomTextView(
                                        type = TextViewType.SINGLE,
                                        mainText = stringResource(R.string.cli_text),
                                        modifier = Modifier.width(40.dp),
                                        config = TextViewConfig(mainTextFontSize = 18.sp)
                                    )
                                    CustomTextView(
                                        type = TextViewType.SINGLE,
                                        mainText = expedicion.cliente.nombreCliente,
                                        modifier = Modifier.width(110.dp),
                                        config = TextViewConfig(mainTextFontSize = 18.sp)
                                    )
                                    Spacer(Modifier.width(7.dp))
                                    CustomTextView(
                                        type = TextViewType.SINGLE,
                                        mainText = stringResource(R.string.cp_text),
                                        modifier = Modifier.width(60.dp),
                                        config = TextViewConfig(mainTextFontSize = 18.sp)
                                    )
                                    CustomTextView(
                                        type = TextViewType.SINGLE,
                                        mainText = expedicion.poblacion,
                                        modifier = Modifier.width(70.dp),
                                        config = TextViewConfig(mainTextFontSize = 18.sp)
                                    )
                                }
                                Row (horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)){
                                    CustomTextView(
                                        type = TextViewType.SINGLE,
                                        mainText = stringResource(R.string.ref_text),
                                        modifier = Modifier.width(40.dp),
                                        config = TextViewConfig(mainTextFontSize = 18.sp)
                                    )
                                    CustomTextView(
                                        type = TextViewType.SINGLE,
                                        mainText = expedicion.referenciaCliente,
                                        modifier = Modifier.width(110.dp),
                                        config = TextViewConfig(mainTextFontSize = 18.sp)
                                    )
                                    Spacer(Modifier.width(7.dp))
                                    CustomTextView(
                                        type = TextViewType.SINGLE,
                                        mainText = stringResource(R.string.peso_text),
                                        modifier = Modifier.width(60.dp),
                                        config = TextViewConfig(mainTextFontSize = 18.sp)
                                    )
                                    CustomTextView(
                                        type = TextViewType.SINGLE,
                                        mainText = expedicion.pesoTotal.toString(),
                                        modifier = Modifier.width(70.dp),
                                        config = TextViewConfig(mainTextFontSize = 18.sp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            // LaunchedEffect with Unit as the key
            LaunchedEffect(Unit) {
                delay(2000)
                if(cerrarAutomaticamente) onDismissRequest()
            }
            Log.i("Pruebas Dialogo", "dialogobulto finalizado" )
        }
    )
}