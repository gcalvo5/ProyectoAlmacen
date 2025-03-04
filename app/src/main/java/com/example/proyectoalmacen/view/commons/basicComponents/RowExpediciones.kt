package com.example.proyectoalmacen.view.commons.basicComponents

import CustomTextView
import TextViewConfig
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectoalmacen.R
import com.example.proyectoalmacen.model.DataClasses.Expedicion

enum class RowExpedicionType{
    DESCARGAR,
    REPASAR,
    CARGAR
}

@Composable
fun RowExpediciones(item: Expedicion, type: RowExpedicionType) {

    var colorFondo = colorScheme.tertiaryContainer
    var fixedText1 = ""
    var text1 = ""
    var fixedText2 = ""
    var text2 = ""
    var fixedText3 = ""
    var text3 = ""
    var fixedText4 = ""
    var text4 = ""
    var comparisonText1 = ""
    var comparisonText2 = ""
    when(type){
        RowExpedicionType.DESCARGAR ->
            when{
                item.numBultosConfirmados == item.numBultos -> colorFondo = colorScheme.surfaceContainerLow
                item.numBultosConfirmados == 0 -> colorFondo = colorScheme.tertiaryContainer
                item.numBultosConfirmados < item.numBultos -> colorFondo = colorScheme.surfaceContainerHigh
            }

        RowExpedicionType.REPASAR ->
            when{
                item.numBultosRepasados == item.numBultosConfirmados + item.numBultosRepasados && item.numBultosRepasados > 0 -> colorFondo = colorScheme.surfaceContainerLow
                item.numBultosRepasados == 0 && item.numBultosConfirmados > 0 -> colorFondo = colorScheme.tertiaryContainer
                item.numBultosRepasados < item.numBultosConfirmados + item.numBultosRepasados -> colorFondo = colorScheme.surfaceContainerHigh
                item.numBultosConfirmados + item.numBultosRepasados == 0 -> colorFondo = colorScheme.surfaceContainerLowest
            }
        RowExpedicionType.CARGAR ->
            when{
                item.numbultosCargados == item.numBultosConfirmados + item.numBultosRepasados + item.numbultosCargados && item.numbultosCargados > 0 -> colorFondo = colorScheme.surfaceContainerLow
                item.numbultosCargados == 0 && item.numBultosConfirmados + item.numBultosRepasados > 0 -> colorFondo = colorScheme.tertiaryContainer
                item.numbultosCargados < item.numBultosConfirmados + item.numBultosRepasados + item.numbultosCargados -> colorFondo = colorScheme.surfaceContainerHigh
                item.numBultosConfirmados + item.numBultosRepasados + item.numbultosCargados == 0 -> colorFondo = colorScheme.surfaceContainerLowest
            }
    }
    if(type == RowExpedicionType.DESCARGAR){
        fixedText1 = stringResource(R.string.cp_text)
        text1 = item.codigoPostal
        fixedText2 = stringResource(R.string.pobacion_text)
        text2 = item.poblacion
        fixedText3 = stringResource(R.string.ref_text)
        text3 = item.referenciaCliente
        fixedText4 = stringResource(R.string.peso_text)
        text4 = item.pesoTotal.toString()
        comparisonText1 = (item.numBultosConfirmados + item.numBultosRepasados + item.numbultosCargados).toString()
        comparisonText2 = item.numBultos.toString()
    }else if(type == RowExpedicionType.REPASAR){
        fixedText1 = stringResource(R.string.cli_text)
        text1 = item.cliente.nombreCliente
        fixedText2 = stringResource(R.string.cp_text)
        text2 = item.poblacion
        fixedText3 = stringResource(R.string.ref_text)
        text3 = item.referenciaCliente
        fixedText4 = stringResource(R.string.peso_text)
        text4 = item.pesoTotal.toString()
        comparisonText1 = item.numBultosRepasados.toString()
        comparisonText2 = (item.numBultosConfirmados + item.numBultosRepasados).toString()
    }else if(type == RowExpedicionType.CARGAR){
        fixedText1 = stringResource(R.string.cli_text)
        text1 = item.cliente.nombreCliente
        fixedText2 = stringResource(R.string.cp_text)
        text2 = item.poblacion
        fixedText3 = stringResource(R.string.ref_text)
        text3 = item.referenciaCliente
        fixedText4 = stringResource(R.string.peso_text)
        text4 = item.pesoTotal.toString()
        comparisonText1 = item.numbultosCargados.toString()
        comparisonText2 = (item.numBultosConfirmados + item.numBultosRepasados + item.numbultosCargados).toString()
    }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .background(colorFondo)
    ) {
        Column(
            Modifier
                .weight(1f) // Take up most of the space
        ) {
            Row {
                CustomTextView(
                    type = TextViewType.SINGLE,
                    mainText = fixedText1,
                    modifier = Modifier.width(40.dp),
                    config = TextViewConfig(mainTextFontSize = 18.sp, mainTextColor = colorScheme.inversePrimary)
                )
                CustomTextView(
                    type = TextViewType.SINGLE,
                    mainText = text1,
                    modifier = Modifier.width(90.dp),
                    config = TextViewConfig(mainTextFontSize = 18.sp, mainTextColor = colorScheme.inversePrimary)
                )
                Spacer(Modifier.width(7.dp))
                CustomTextView(
                    type = TextViewType.SINGLE,
                    mainText = fixedText2,
                    modifier = Modifier.width(60.dp),
                    config = TextViewConfig(mainTextFontSize = 18.sp, mainTextColor = colorScheme.inversePrimary)
                )
                CustomTextView(
                    type = TextViewType.SINGLE,
                    mainText = text2,
                    modifier = Modifier.width(70.dp),
                    config = TextViewConfig(mainTextFontSize = 18.sp, mainTextColor = colorScheme.inversePrimary)
                )
            }
            Row {
                CustomTextView(
                    type = TextViewType.SINGLE,
                    mainText = fixedText3,
                    modifier = Modifier.width(40.dp),
                    config = TextViewConfig(mainTextFontSize = 18.sp, mainTextColor = colorScheme.inversePrimary)
                )
                CustomTextView(
                    type = TextViewType.SINGLE,
                    mainText = text3,
                    modifier = Modifier.width(90.dp),
                    config = TextViewConfig(mainTextFontSize = 18.sp, mainTextColor = colorScheme.inversePrimary)
                )
                Spacer(Modifier.width(7.dp))
                CustomTextView(
                    type = TextViewType.SINGLE,
                    mainText = fixedText4,
                    modifier = Modifier.width(60.dp),
                    config = TextViewConfig(mainTextFontSize = 18.sp, mainTextColor = colorScheme.inversePrimary)
                )
                CustomTextView(
                    type = TextViewType.SINGLE,
                    mainText = text4,
                    modifier = Modifier.width(70.dp),
                    config = TextViewConfig(mainTextFontSize = 18.sp, mainTextColor = colorScheme.inversePrimary)
                )
            }
        }
        CustomComparationText(
            firstText = comparisonText1,
            secondText = comparisonText2,
            icon = Icons.Filled.Done,
            modifier = Modifier.padding(end = 8.dp, top = 12.dp),
            textConfig = TextViewConfig(mainTextFontSize = 18.sp, mainTextColor = colorScheme.inversePrimary)
        )
    }
}