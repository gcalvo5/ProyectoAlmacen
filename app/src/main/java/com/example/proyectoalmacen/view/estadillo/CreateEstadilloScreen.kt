package com.example.proyectoalmacen.view.estadillo

import CustomButton
import CustomTextView
import TextViewType
import android.os.Build
import android.util.Log
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import com.example.proyectoalmacen.model.DataClasses.TipoUsuario
import com.example.proyectoalmacen.model.DataClasses.Usuario
import com.example.proyectoalmacen.model.States.UiState
import com.example.proyectoalmacen.view.commons.basicComponents.CustomInputField
import com.example.proyectoalmacen.view.commons.basicComponents.CustomLoader
import com.example.proyectoalmacen.view.commons.basicComponents.CustomMultiSelectDropdown
import com.example.proyectoalmacen.view.commons.basicComponents.InputFieldType
import com.example.proyectoalmacen.view.commons.basicComponents.SelectableItem
import com.example.proyectoalmacen.viewmodel.EstadilloViewModel
import com.example.proyectoalmacen.viewmodel.UsuarioViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateEstadilloScreen(usuarioViewModel: UsuarioViewModel = hiltViewModel(),estadilloViewModel: EstadilloViewModel = hiltViewModel(),onDismissRequest: () -> Unit, navigateToEstadillo: (numEstadillo: Int, nombreChofer: String) -> Unit) {
    var muelleText by remember { mutableStateOf("") }
    var conductorText by remember { mutableStateOf("") }
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
                        CustomTextView(type = TextViewType.SINGLE, stringResource(R.string.conductor_text))
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    var selectableItemList: MutableList<SelectableItem> = mutableListOf()
                    usuarios.forEach(
                        { selectableItem ->
                            if (selectableItem.tipoUsuario == TipoUsuario.CHOFER){
                                selectableItemList.add(SelectableItem(selectableItem.nombre))
                            }
                        }
                    )
                    CustomMultiSelectDropdown(items = selectableItemList, onSelectionChanged = {
                        conductorText = it.firstOrNull()?.name ?: ""
                    }, searchTextIntro = conductorText)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.height(60.dp)) {
                    Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                        CustomTextView(type = TextViewType.SINGLE, stringResource(R.string.muelle_text))
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    CustomInputField( value = muelleText, type = InputFieldType.NUMBER, modifier = Modifier.padding(start = 30.dp), onValueChange = {newText -> muelleText = newText})
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    val conductor = usuarios.find { it.nombre == conductorText }
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