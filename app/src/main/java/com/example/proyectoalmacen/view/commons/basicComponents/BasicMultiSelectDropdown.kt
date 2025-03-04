package com.example.proyectoalmacen.view.commons.basicComponents

import androidx.compose.animation.core.copy
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyectoalmacen.R
import kotlin.text.contains
import kotlin.text.filter
import kotlin.text.map

data class SelectableItem(
    val name: String,
    var selected: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomMultiSelectDropdown(
    items: List<SelectableItem>,
    onSelectionChanged: (List<SelectableItem>) -> Unit,
    searchTextIntro: String = ""
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(searchTextIntro) }
    var selectableItems by remember { mutableStateOf(items) }

    val filteredItems = selectableItems.filter {
        it.name.contains(searchText, ignoreCase = true)
    }
    if (searchTextIntro.isNotEmpty() && !searchText.startsWith(searchTextIntro)) {
        searchText = searchTextIntro
    }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.wrapContentSize()
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text(stringResource(R.string.buscar_text)) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowUp,
                        contentDescription = "Dropdown"
                    )
                }
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp)
        ) {
            filteredItems.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val updatedItems = selectableItems.map {
                                if (it == item) {
                                    it.copy(selected = !it.selected)
                                } else {
                                    it
                                }
                            }
                            selectableItems = updatedItems
                            onSelectionChanged(updatedItems.filter { it.selected })
                            expanded = false
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = item.selected,
                        onCheckedChange = { isChecked ->
                            val updatedItems = selectableItems.map {
                                if (it == item) {
                                    it.copy(selected = isChecked)
                                } else {
                                    it
                                }
                            }
                            selectableItems = updatedItems
                            onSelectionChanged(updatedItems.filter { it.selected })
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = item.name)
                }
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun MultiSelectDropdownExample() {
    val items = listOf(
        SelectableItem("Apple"),
        SelectableItem("Banana"),
        SelectableItem("Orange"),
        SelectableItem("Grape"),
        SelectableItem("Mango"),
        SelectableItem("Pineapple"),
        SelectableItem("Strawberry"),
        SelectableItem("Watermelon")
    )
    var selectedItems by remember { mutableStateOf<List<SelectableItem>>(emptyList()) }

    Column(modifier = Modifier.padding(16.dp)) {
        CustomMultiSelectDropdown(
            items = items,
            onSelectionChanged = { selected ->
                selectedItems = selected
            }
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Text(text = "Selected items: ${selectedItems.joinToString { it.name }}")
    }
}