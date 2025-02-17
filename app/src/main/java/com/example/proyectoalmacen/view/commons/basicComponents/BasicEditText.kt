package com.example.proyectoalmacen.view.commons.basicComponents

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyectoalmacen.ui.theme.Green80
import com.example.proyectoalmacen.ui.theme.Grey40
import com.example.proyectoalmacen.ui.theme.Grey80
import com.example.proyectoalmacen.ui.theme.Grey95
import com.example.proyectoalmacen.ui.theme.ProyectoAlmacenTheme
import com.example.proyectoalmacen.ui.theme.Red30
import com.example.proyectoalmacen.ui.theme.Transparent
import com.example.proyectoalmacen.ui.theme.White40

/**
 * Represents the different types of text input fields.
 */
enum class InputFieldType {
    /**
     * Standard text input field.
     */
    TEXT,

    /**
     * Numeric input field.
     */
    NUMBER,

    /**
     * Non-editable text field.
     */
    READ_ONLY
}

/**
 * A customizable text input field with support for various input types and behaviors.
 *
 * This composable provides a flexible way to create text input fields with different
 * functionalities, including numeric input, read-only mode, and custom placeholder text.
 * It leverages Material 3's `TextField` and provides styling options for different
 * input types.
 *
 * @param type The type of the input field. This determines the keyboard type,
 *   text alignment, and whether the field is editable.
 *   Possible values are:
 *   - [InputFieldType.TEXT]: Standard text input. (Default)
 *   - [InputFieldType.NUMBER]: Numeric input, displays the number keyboard.
 *   - [InputFieldType.READ_ONLY]: Read-only text field, the user cannot edit the value.
 * @param value The current value of the input field, represented as a String.
 *   Defaults to an empty string.
 * @param onValueChange Callback function invoked whenever the input value changes.
 *   It receives the new value as a String.
 *   Defaults to an empty lambda (no action on change).
 * @param placeholder The placeholder text displayed when the input field is empty.
 *   Defaults to an empty string.
 * @param modifier Modifier to be applied to the input field for layout and styling.
 *   Defaults to an empty Modifier.
 *
 * Example Usage:
 * ```
 * // Standard text input
 * CustomInputField(
 *     value = textValue,
 *     onValueChange = { textValue = it },
 *     placeholder = "Enter text here"
 * )
 *
 * // Numeric input
 * CustomInputField(
 *     type = InputFieldType.NUMBER,
 *     value = numberValue,
 *     onValueChange = { numberValue = it },
 *     placeholder = "Enter a number"
 * )
 *
 * // Read-only input
 * CustomInputField(
 *     type = InputFieldType.READ_ONLY,
 *     value = "This is read-only",
 * )
 * ```
 *
 * @see InputFieldType
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomInputField(
    type: InputFieldType = InputFieldType.TEXT,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    placeholder: String = "",
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    hideKeyboardOnFocus: Boolean = true
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }
    val keyboardOptions = when (type) {
        InputFieldType.NUMBER -> KeyboardOptions(keyboardType = KeyboardType.Number)
        else -> KeyboardOptions.Default
    }
    val textStyle = when (type) {
        InputFieldType.NUMBER -> TextStyle(textAlign = TextAlign.Center)
        else -> TextStyle.Default
    }

    val placeholderComposable: (@Composable () -> Unit)? = if (placeholder.isNotEmpty()) {
        {
            Text(
                text = placeholder,
                textAlign = if (type == InputFieldType.NUMBER) TextAlign.Center else TextAlign.Start,
                modifier = if (type == InputFieldType.NUMBER) Modifier.fillMaxWidth() else Modifier
            )
        }
    } else {
        null
    }


    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder =  placeholderComposable,
        keyboardOptions = keyboardOptions,
        textStyle = textStyle,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = colorScheme.secondaryContainer,
            focusedIndicatorColor = colorScheme.primary,
            unfocusedIndicatorColor = colorScheme.secondaryContainer,
            disabledIndicatorColor = colorScheme.secondaryContainer,
        ),
        modifier = when (type) {
            InputFieldType.NUMBER -> modifier
                .width(80.dp)
                .padding(6.dp)
            InputFieldType.READ_ONLY -> Modifier
                .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier)
                .onFocusEvent { focusState ->
                    if (hideKeyboardOnFocus && focusState.isFocused) {
                        keyboardController?.hide()
                    }
                }
            else -> modifier
                .fillMaxWidth()
                .padding(6.dp)
        },
        interactionSource = interactionSource
    )
}

@Preview(showBackground = true)
@Composable
fun BasicInputFieldPreview() {
    ProyectoAlmacenTheme {
        CustomInputField(
            type = InputFieldType.READ_ONLY,
            value = "Read Only Text",
            placeholder = "This is a read only field"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BasicInputFieldPreview2() {
    CustomInputField(
        type = InputFieldType.NUMBER,
        placeholder = "Enter a number"
    )
}

@Preview(showBackground = true)
@Composable
fun BasicInputFieldPreview3() {
    CustomInputField(
        type = InputFieldType.TEXT,
        placeholder = "Enter some text"
    )
}
/*    focusRequester: FocusRequester? = null,
    hideKeyboardOnFocus: Boolean = true
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = modifier.padding(5.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            modifier = Modifier
                .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier)
                .onFocusEvent { focusState ->
                    if (hideKeyboardOnFocus && focusState.isFocused) {
                        keyboardController?.hide()
                    }
                },
            enabled = enabled,
            textStyle = TextStyle(fontSize = 18.sp),
            keyboardOptions = when (type) {
                InputFieldType.NUMBER -> KeyboardOptions(keyboardType = KeyboardType.Number)
                else -> KeyboardOptions.Default
            },
            interactionSource = interactionSource
        )
    }
}*/
