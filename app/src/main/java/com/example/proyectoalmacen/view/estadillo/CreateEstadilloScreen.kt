package com.example.proyectoalmacen.view.estadillo

import CustomButton
import CustomTextView
import TextViewType
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proyectoalmacen.R
import com.example.proyectoalmacen.model.DataClasses.Estadillo
import com.example.proyectoalmacen.view.commons.basicComponents.CustomInputField
import com.example.proyectoalmacen.view.commons.basicComponents.InputFieldType
import com.example.proyectoalmacen.viewmodel.EstadilloViewModel
import com.example.proyectoalmacen.viewmodel.UsuarioViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateEstadilloScreen(usuarioViewModel: UsuarioViewModel = hiltViewModel(),estadilloViewModel: EstadilloViewModel = hiltViewModel(),onDismissRequest: () -> Unit, navigateToEstadillo: (numEstadillo: Int, nombreChofer: String) -> Unit) {
    var muelleText by remember { mutableStateOf("") }
    var conductorText by remember { mutableStateOf("") }
    Dialog(onDismissRequest = onDismissRequest){
        Card (colors = CardDefaults.cardColors(containerColor = colorScheme.primaryContainer, contentColor = colorScheme.primary)){
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    CustomTextView(
                        type = TextViewType.TITLE_AND_SUBTITLE,
                        stringResource(R.string.introducir_datos_text)
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(modifier = Modifier.height(60.dp)) {
                    Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                        CustomTextView(type = TextViewType.SINGLE, stringResource(R.string.muelle_text))
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    CustomInputField( value = muelleText, type = InputFieldType.NUMBER, modifier = Modifier.padding(start = 30.dp), onValueChange = {newText -> muelleText = newText})
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.height(60.dp)) {
                    Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                        CustomTextView(type = TextViewType.SINGLE, stringResource(R.string.conductor_text))
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    CustomInputField(value = conductorText, type = InputFieldType.TEXT, onValueChange = {newText -> conductorText = newText})
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    val conductor = usuarioViewModel.getUsuarioByName(conductorText).firstOrNull()
                    CustomButton(
                        stringResource(R.string.crear_text),
                        onClick = {
                            if (conductor != null) {
                                estadilloViewModel.createEstadillo(muelleText.toInt(), conductor)
                                navigateToEstadillo(3, conductorText)
                            }
                        },
                        backgroundColor = colorScheme.secondary,
                        modifier = Modifier.width(150.dp)
                    )
                }
            }
        }


    }

}