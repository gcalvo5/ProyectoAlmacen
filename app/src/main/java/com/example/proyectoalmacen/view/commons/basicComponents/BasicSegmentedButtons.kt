package com.example.proyectoalmacen.view.commons.basicComponents
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class SegmentedButtonOption(
    val label: String = "",
    val icon: ImageVector? = null
)

/**
 * Data class to represent a single option in the segmented button row.
 *
 * @property label The text label to display for this option.
 * @property icon An optional icon to display alongside the label.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSegmentedButtons(
    options: List<SegmentedButtonOption>,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = {
                    selectedIndex = index
                    onOptionSelected(index)
                },
                selected = index == selectedIndex,
                icon = {
                    if (option.icon != null) {
                        Icon(
                            imageVector = option.icon,
                            contentDescription = option.label
                        )
                    }
                },
                label = {
                    Text(text = option.label)
                },
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = colorScheme.secondaryContainer,
                    inactiveContainerColor = colorScheme.tertiaryContainer,
                    activeContentColor = colorScheme.secondary,
                    inactiveContentColor = colorScheme.primary
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomSegmentedButtonsPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomSegmentedButtons(
            options = listOf(
                SegmentedButtonOption("", Icons.Filled.Home),
                SegmentedButtonOption("", Icons.Filled.Settings),
                SegmentedButtonOption("Profile"),
                SegmentedButtonOption("Profile"),
                SegmentedButtonOption("Profile")
            ),
            onOptionSelected = { index ->
                println("Selected option: $index")
            }
        )
        CustomSegmentedButtons(
            options = listOf(
                SegmentedButtonOption("Option 1"),
                SegmentedButtonOption("Option 2"),
                SegmentedButtonOption("Option 3")
            ),
            onOptionSelected = { index ->
                println("Selected option: $index")
            }
        )
    }
}