package com.example.proyectoalmacen.view.commons.basicComponents

import CustomTextView
import TextViewConfig
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomComparationText(
    firstText:String,
    secondText:String,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier.size(25.dp),
    textConfig: TextViewConfig = TextViewConfig(mainTextFontSize = 18.sp, mainTextColor = colorScheme.primary)
) {
    Row (modifier = modifier) {
        CustomTextView(type = TextViewType.SINGLE, mainText = firstText, config = textConfig)
        Canvas(modifier = Modifier
            .padding(horizontal = 2.dp)
            .size(10.dp, 20.dp)) {
            textConfig.mainTextColor?.let {
                drawLine(
                    color = it,
                    start = Offset(3f, size.height),
                    end = Offset(size.width, 3f),
                    strokeWidth = 2f
                )
            }
        }
        CustomTextView(type = TextViewType.SINGLE, mainText = secondText, config = textConfig)
        Spacer(modifier = Modifier.width(5.dp))
        if(icon != null) textConfig.mainTextColor?.let { Icon(imageVector = icon, contentDescription = "None", tint = it) }
    }

}