package com.example.zenithpaw.ui.uiscreens.pixelcomposables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    textStyle: TextStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelStyle: TextStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor: Color = MaterialTheme.colorScheme.primaryContainer,
    labelColor: Color = MaterialTheme.colorScheme.onBackground,
    placeholderColor: Color = MaterialTheme.colorScheme.onBackground,
    iconTint: Color = MaterialTheme.colorScheme.onBackground,
    shadowColor: Color = Color.Black,
    shadowOffset: Dp = 4.dp,
    borderWidth: Dp = 2.2.dp,
    shape: Shape = RectangleShape
){
    val isFocused by interactionSource.collectIsFocusedAsState()
    val borderColor = if (isFocused) focusedBorderColor else unfocusedBorderColor

    Column(
        //removed wrapContentSize() so that root container can receive the size from the modifier
        modifier = modifier
    ) {
        if (label != null) {
            Text(
                text = label,
                color = labelColor,
                fontWeight = labelStyle.fontWeight,
                fontSize = labelStyle.fontSize,
                fontFamily = labelStyle.fontFamily,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        // match the width of the column
        Box(modifier = Modifier.fillMaxWidth().weight(1f, fill = false)){
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = shadowOffset, y = shadowOffset)
                    .background(shadowColor, shape)
            )
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                readOnly = readOnly,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                visualTransformation = visualTransformation,
                interactionSource = interactionSource,
                textStyle = textStyle.copy(color = if (enabled) labelColor else placeholderColor),
                singleLine = true,
                cursorBrush = SolidColor(Color.Black),
                modifier = Modifier
                    .fillMaxSize() // removed wrapContentSize() so that button size scales independently of the text
                    .background(backgroundColor, shape)
                    .border(width = borderWidth, color = borderColor, shape = shape),
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp)
                    ){
                        if (leadingIcon != null) {
                            Icon(
                                imageVector = leadingIcon,
                                contentDescription = null,
                                tint = iconTint,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        Box (
                            modifier = Modifier.fillMaxHeight().weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ){
                            if (value.isEmpty() && placeholder != null) {
                                Text(
                                    text = placeholder,
                                    color = placeholderColor,
                                    fontWeight = labelStyle.fontWeight,
                                    fontSize = labelStyle.fontSize
                                )
                            }
                            innerTextField()
                        }
                        if (trailingIcon != null) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = trailingIcon,
                                contentDescription = null,
                                tint = iconTint,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            )
        }
    }
}