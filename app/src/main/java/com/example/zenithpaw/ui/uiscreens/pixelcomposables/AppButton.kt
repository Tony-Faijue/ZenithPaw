package com.example.zenithpaw.ui.uiscreens.pixelcomposables


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Custom NeoBrutal Button override to allow for
 * custom sizing of the button and text independently
 *
 */
@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    shadowOffset: Dp = 4.dp,
    shadowColor: Color = Color.Black,
    shape: Shape = RectangleShape,
    fontSize: TextUnit = 16.sp
){
    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            onClick()
            delay(40)
            isPressed = false
        }
    }

    Box(
        modifier = modifier
            // Removed the wrapContentSize()
            // so that root container can receive the size from the modifier
            .padding(bottom = shadowOffset, end = shadowOffset)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffset, y = shadowOffset)
                .background(shadowColor, shape)
        )
        Box(
            modifier = Modifier
                // Replaced wrapContentSize() with matchParentSize()
                // so that button size scales independently of the text
                .matchParentSize()
                .offset(
                    x = if (isPressed) shadowOffset / 2 else 0.dp,
                    y = if (isPressed) shadowOffset / 2 else 0.dp
                )
                .clickable(enabled = enabled) {
                    isPressed = true
                }
                .background(backgroundColor, shape)
                .drawBehind {
                    drawRect(
                        color = shadowColor,
                        size = size,
                        style = Stroke(width = 5f)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = textColor,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    // Custom font size
                    fontSize = fontSize,
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}
