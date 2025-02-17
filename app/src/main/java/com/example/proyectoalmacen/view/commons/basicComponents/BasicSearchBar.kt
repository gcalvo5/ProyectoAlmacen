package com.example.proyectoalmacen.view.commons.basicComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Search",
    leadingIcon: ImageVector = Icons.Filled.Search,
    trailingIcon: ImageVector? = Icons.Filled.Clear,
    onTrailingIconClick: (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Search,
    onSearch: (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    cursorColor: Color = MaterialTheme.colorScheme.primary,
    placeholderColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    leadingIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    trailingIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                color = placeholderColor
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = "Search",
                tint = leadingIconColor
            )
        },
        trailingIcon = {
            if (trailingIcon != null && value.isNotEmpty()) {
                IconButton(onClick = {
                    if (onTrailingIconClick != null) {
                        onTrailingIconClick()
                    } else {
                        onValueChange("")
                    }
                }) {
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = "Clear",
                        tint = trailingIconColor
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (onSearch != null) {
                    onSearch()
                }
            }
        ),
        textStyle = TextStyle(color = textColor),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = containerColor,
            cursorColor = cursorColor,
            focusedBorderColor = cursorColor,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )
    )
}

@Preview(showBackground = true)
@Composable
fun CustomizableSearchBarPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        var text1 by remember { mutableStateOf("") }
        CustomSearchBar(
            value = text1,
            onValueChange = { text1 = it },
            placeholder = "Search Products",
            onSearch = { println("Searching for: $text1") }
        )
        var text2 by remember { mutableStateOf("") }
        CustomSearchBar(
            value = text2,
            onValueChange = { text2 = it },
            placeholder = "Search Users",
            trailingIcon = null,
            onSearch = { println("Searching for: $text2") }
        )
        var text3 by remember { mutableStateOf("") }
        CustomSearchBar(
            value = text3,
            onValueChange = { text3 = it },
            placeholder = "Search by id",
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
            onSearch = { println("Searching for: $text3") }
        )
        var text4 by remember { mutableStateOf("example") }
        CustomSearchBar(
            value = text4,
            onValueChange = { text4 = it },
            placeholder = "Search by id",
            onTrailingIconClick = { text4 = "" },
            containerColor = Color.Yellow,
            textColor = Color.Black,
            cursorColor = Color.Red,
            placeholderColor = Color.DarkGray,
            leadingIconColor = Color.Blue,
            trailingIconColor = Color.Magenta
        )
    }
}