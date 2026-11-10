package com.example.zenithpaw.ui.uiscreens.pixelcomposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@Composable
fun AppImageButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    imageId: Int,
    imageWidth: Dp,
    imageHeight: Dp,
    stroke: Stroke = Stroke(width = 5f),
    spacerWidth: Dp = 8.dp,
    enabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    shadowOffset: Dp = 4.dp,
    shadowColor: Color = Color.Black,
    shape: Shape = RectangleShape,
    buttonPadding: Dp = 8.dp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    textStyle: TextStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp
    )
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
                        style = stroke
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.padding(buttonPadding).fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = horizontalArrangement
            ){
                Image(
                    bitmap = ImageBitmap.imageResource(imageId),
                    contentDescription = null,
                    filterQuality = FilterQuality.None,
                    modifier = Modifier
                        .size(width = imageWidth, height = imageHeight)
                )
                if (!text.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.width(spacerWidth))
                    Text(
                        text = text,
                        color = textColor,
                        style = textStyle,
                    )
                }
            }
        }
    }
}