package com.khalekuzzaman.just.cse.task.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NestedHorizontalScroller(
    modifier: Modifier=Modifier,
    header: @Composable (() -> Unit)? = null,
    children: List<@Composable () -> Unit>,
    height: Dp? = null,
    gap: Dp = 8.dp,
    showIndicator: Boolean = false
) {
    Column(
        modifier = if (height != null) modifier.height(height).fillMaxWidth() else modifier.wrapContentHeight().fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(gap)
    ) {
        header?.invoke()

        header?.let {
            Spacer(modifier = Modifier.height(gap))
        }

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            children.forEach { child -> child() }
        }
        if (showIndicator){
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(children.size) {
                    Box(modifier = Modifier.size(6.dp).background(color = Color.Black, shape = CircleShape))
                    Spacer(Modifier.width(4.dp))
                }
            }
        }


    }
}


fun Modifier.customShadow(
    maxWidth: Dp = Dp.Infinity,
    maxHeight: Dp = Dp.Infinity,
    minWidth: Dp = 0.dp,
    minHeight: Dp = 0.dp,
    backgroundColor: Color = Color.White,
    shadowColor: Color = Color.Black,
    blurRadius: Dp = 6.dp,
    offset: Offset = Offset(0f, 3f),
    borderRadius: Dp = 8.dp,
    padding: Dp = 8.dp
): Modifier {
    return this
        .shadow(
            elevation = blurRadius,
            shape = RoundedCornerShape(borderRadius),
            ambientColor = shadowColor.copy(alpha = 0.1f),
            spotColor = shadowColor.copy(alpha = 0.1f)
        )
        .clip(RoundedCornerShape(borderRadius))
        .background(backgroundColor)
        .padding(padding)
}


@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit,
    trailingIcon: (@Composable (Modifier) -> Unit)? = null
) {
    _BasicAuthTextField(
        modifier = modifier,
        enabled = enabled,
        label = label,
        value = value,
        visualTransformation = visualTransformation,
        leadingIcon = leadingIcon,
        keyboardType = keyboardType,
        onValueChanged = onValueChange,
        readOnly = readOnly,
        trailingIcon = trailingIcon
    )
}

@Composable
private fun _BasicAuthTextField(
    modifier: Modifier,
    label: String,
    value: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: ImageVector?,
    keyboardType: KeyboardType,
    onValueChanged: (String) -> Unit,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    trailingIcon: (@Composable (Modifier) -> Unit)? = null
) {

    val borderColor = if (enabled) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)

    val placeholderColor = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)

    val textColor = if (enabled) MaterialTheme.colorScheme.onSurface
    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)

    val iconTint = if (enabled) MaterialTheme.colorScheme.tertiary
    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

    val fontSize = 15.sp

    BasicTextField(
        enabled = enabled,
        value = value,
        onValueChange = onValueChanged,
        textStyle = TextStyle(fontSize = fontSize, color = textColor),
        singleLine = true,
        readOnly = readOnly,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        cursorBrush = if (enabled) SolidColor(MaterialTheme.colorScheme.primary) else SolidColor(Color.Transparent), // Hide cursor when disabled
        decorationBox = { innerText ->
            Row(
                modifier
                    .border(width = 0.dp, color = Color.Gray, shape = CircleShape)
                    .padding(vertical = 8.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        tint = iconTint,
                        contentDescription = "leading icon",
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(22.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                }

                Box(Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        _Placeholder(label, fontSize, placeholderColor)
                    }
                    // Call innerText in both cases to ensure the cursor is shown (if enabled)
                    innerText()
                }

                if (trailingIcon != null) {
                    Spacer(Modifier.width(8.dp))
                    trailingIcon(Modifier.padding(end = 8.dp))
                }
            }
        }
    )
}

@Composable
fun _Placeholder(text: String, fontSize: TextUnit, placeholderColor: Color) {
    Text(
        text = text,
        fontSize = fontSize,
        color = placeholderColor
    )
}


@Composable
private fun _Placeholder(
    text: String,
    fontSize: TextUnit,
    color: Color,
    fontWeight: FontWeight = FontWeight.W600,
) {
    Text(
        text = text,
        fontSize = fontSize,
        color = color,
        fontWeight = fontWeight
    )

}