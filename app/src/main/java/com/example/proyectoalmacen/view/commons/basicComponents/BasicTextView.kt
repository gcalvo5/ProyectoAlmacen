import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

enum class TextViewType {
    SINGLE, TITLE_AND_SUBTITLE
}

// 2. Use data class for better parameter management and default values
data class TextViewConfig(
    val mainTextFontSize: TextUnit = 20.sp,
    val subtitleFontSize: TextUnit = 16.sp,
    val mainTextColor: Color? = null,
    val subtitleColor: Color? = null,
    val textAlign: TextAlign = TextAlign.Start,
    val mainTextFontWeight: FontWeight = FontWeight.Normal,
    val subtitleFontWeight: FontWeight = FontWeight.Normal,
    val mainTextBoldForTitle: Boolean = true,
    val maintextStyle: TextStyle = TextStyle.Default
)

// 3. Refactor Composable function for better readability and maintainability
@Composable
fun CustomTextView(
    type: TextViewType = TextViewType.SINGLE,
    mainText: String,
    subtitle: String = "",
    config: TextViewConfig = TextViewConfig(), // Use data class for configuration
    modifier: Modifier = Modifier
) {
    when (type) {
        TextViewType.SINGLE -> {
            Text(
                text = mainText,
                fontSize = config.mainTextFontSize,
                color = config.mainTextColor?: MaterialTheme.colorScheme.primary ,
                textAlign = config.textAlign,
                fontWeight = config.mainTextFontWeight,
                modifier = modifier,
                style = config.maintextStyle
            )
        }

        TextViewType.TITLE_AND_SUBTITLE -> {
            Column(modifier = modifier) {
                Text(
                    text = mainText,
                    fontSize = config.mainTextFontSize,
                    color = config.mainTextColor?: MaterialTheme.colorScheme.primary,
                    textAlign = config.textAlign,
                    fontWeight = if (config.mainTextBoldForTitle) FontWeight.Bold else config.mainTextFontWeight,
                    style = config.maintextStyle,

                )
                Text(
                    text = subtitle,
                    fontSize = config.subtitleFontSize,
                    color = config.subtitleColor?: MaterialTheme.colorScheme.tertiary,
                    textAlign = config.textAlign,
                    fontWeight = config.subtitleFontWeight
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BasicTextViewPreview() {
    Column {
        CustomTextView(
            type = TextViewType.TITLE_AND_SUBTITLE,
            mainText = "Title",
            subtitle = "Subtitle"
        )
        CustomTextView(
            type = TextViewType.SINGLE,
            mainText = "Single Text"
        )
        CustomTextView(
            type = TextViewType.TITLE_AND_SUBTITLE,
            mainText = "Custom Title",
            subtitle = "Custom Subtitle",
            config = TextViewConfig(
                mainTextFontSize = 30.sp,
                subtitleFontSize = 14.sp,
                mainTextColor = Color.Blue,
                subtitleColor = Color.Red,
                mainTextFontWeight = FontWeight.Light,
                mainTextBoldForTitle = false
            )
        )
    }
}