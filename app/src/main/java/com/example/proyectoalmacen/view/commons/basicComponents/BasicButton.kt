import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A customizable button composable.
 *
 * This function creates a button with configurable text, click action, colors,
 * enabled/disabled state, and padding. It utilizes Material Design 3's Button composable
 * under the hood, providing a consistent look and feel.
 *
 * @param text The text to be displayed inside the button.
 * @param onClick The action to be performed when the button is clicked.
 * @param modifier Modifier to be applied to the button. Defaults to Modifier.
 * @param backgroundColor The background color of the button. Defaults to the `primary` color
 *     defined in `LocalButtonColors`.
 * @param textColor The text color of the button. Defaults to the `onPrimary` color defined
 *     in `LocalButtonColors`.
 * @param enabled Whether the button is enabled or disabled. Defaults to `true`.
 * @param padding The padding around the button content. Defaults to 8.dp.
 *
 * Example Usage:
 * ```
 * CustomButton(
 *     text = "Click Me",
 *     onClick = { println("Button Clicked!") },
 *     backgroundColor = Color.Blue,
 *     textColor = Color.White,
 *     padding = 16.dp
 * )
 * ```
 */
@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = colorScheme.secondaryContainer,
    textColor: Color = colorScheme.primary,
    enabled: Boolean = true,
    padding: Dp = 8.dp,
) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(padding),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        enabled = enabled
    ) {
        Text(text = text)
    }
}

@Composable
fun DoubleButton(
    firstButtonText: String,
    onFirstButtonClick: () -> Unit,
    secondButtonText: String,
    onSecondButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    firstButtonBackgroundColor: Color = colorScheme.primary,
    firstButtonTextColor: Color = colorScheme.primary,
    secondButtonBackgroundColor: Color = colorScheme.secondaryContainer,
    secondButtonTextColor: Color = colorScheme.tertiaryContainer,
    enabled: Boolean = true,
    padding: Dp = 8.dp,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CustomButton(
            text = firstButtonText,
            onClick = onFirstButtonClick,
            backgroundColor = firstButtonBackgroundColor,
            textColor = firstButtonTextColor,
            enabled = enabled,
            padding = padding,
        )
        CustomButton(
            text = secondButtonText,
            onClick = onSecondButtonClick,
            backgroundColor = secondButtonBackgroundColor,
            textColor = secondButtonTextColor,
            enabled = enabled,
            padding = padding,
        )
    }
}

@Preview
@Composable
fun CustomButtonPreview() {
    CustomButton(text = "Single Button", onClick = {})
}

@Preview
@Composable
fun DoubleButtonPreview() {
    DoubleButton(
        firstButtonText = "Button 1",
        onFirstButtonClick = {},
        secondButtonText = "Button 2",
        onSecondButtonClick = {}
    )
}