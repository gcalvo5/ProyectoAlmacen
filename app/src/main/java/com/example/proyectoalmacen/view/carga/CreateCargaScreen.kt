package com.example.proyectoalmacen.view.carga

import CustomButton
import CustomTextView
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proyectoalmacen.R
import com.example.proyectoalmacen.view.commons.basicComponents.CustomInputField
import com.example.proyectoalmacen.view.commons.basicComponents.CustomMultiSelectDropdown
import com.example.proyectoalmacen.view.commons.basicComponents.InputFieldType
import com.example.proyectoalmacen.view.commons.basicComponents.SelectableItem
import com.example.proyectoalmacen.viewmodel.HojaCargaViewModel
import com.example.proyectoalmacen.viewmodel.PlazasViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateCargaScreen(onDismissRequest: () -> Unit, navigateToCarga: (plazas: List<Int>) -> Unit = { plazas: List<Int> -> }){
    var muelleText by remember { mutableStateOf("") }
    var plazasText by remember { mutableStateOf("") }
    var plazasList = remember { mutableStateListOf<Int>() }
    val plazasViewModel: PlazasViewModel = hiltViewModel()
    val hojaCargaViewModel: HojaCargaViewModel = hiltViewModel()
    Dialog(onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ){
        Card (colors = CardDefaults.cardColors(containerColor = colorScheme.primaryContainer, contentColor = colorScheme.primary), modifier = Modifier.fillMaxWidth()){
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    CustomTextView(
                        type = TextViewType.TITLE_AND_SUBTITLE,
                        stringResource(R.string.introducir_datos_text)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.height(60.dp)) {
                    Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                        CustomTextView(type = TextViewType.SINGLE, stringResource(R.string.plazas_text))
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    var selectableItemList: MutableList<SelectableItem> = mutableListOf()
                    plazasViewModel.plazasState.plazas.forEach(
                        { selectableItem ->
                            selectableItemList.add(SelectableItem(selectableItem.nombrePlaza))
                        }
                    )
                    CustomMultiSelectDropdown(selectableItemList) {
                        plazasList.addAll(it.map { selectableItem -> plazasViewModel.getPlazaByNombre(selectableItem.name).codPlazas })
                        plazasText = it.joinToString { selectableItem -> selectableItem.name }
                    }

                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(modifier = Modifier.height(60.dp)) {
                    Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                        CustomTextView(type = TextViewType.SINGLE, stringResource(R.string.plazas_text))
                    }
                    Spacer(modifier = Modifier.width(7.dp))
                    Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                        if (plazasText.isNotEmpty()) {
                            CustomTextView(type = TextViewType.SINGLE, plazasText)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Spacer(modifier = Modifier.height(5.dp))
                Row(modifier = Modifier.height(60.dp)) {
                    Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                        CustomTextView(type = TextViewType.SINGLE, stringResource(R.string.muelle_text))
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    CustomInputField( value = muelleText, type = InputFieldType.NUMBER, modifier = Modifier.padding(start = 30.dp), onValueChange = { newText -> muelleText = newText})
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    CustomButton(
                        stringResource(R.string.crear_text),
                        onClick = {
                            hojaCargaViewModel.crearHojaCarga(muelleText, listaPlazasId = plazasList)
                            navigateToCarga(plazasList)
                        },
                        backgroundColor = colorScheme.secondary,
                        modifier = Modifier.width(150.dp)
                    )
                }
            }
        }


    }
}