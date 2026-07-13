package com.example.zenithpaw.ui.uiscreens.pixelcomposables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppBasicBackgroundLayout(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    shadowColor: Color = MaterialTheme.colorScheme.onBackground,
    shadowOffset: Dp = 8.dp,
    borderWidth: Dp = 2.2.dp,
    paddingContentSize: Dp = 16.dp,
    shape: Shape = RectangleShape,
    content: @Composable () -> Unit
){
    Box(
        modifier = modifier
            .padding(bottom = shadowOffset, end = shadowOffset)
    ){
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffset, y = shadowOffset)
                .background(shadowColor, shape)
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 0.dp, y = 0.dp)
                .background(backgroundColor, shape)
                .border(width = borderWidth, color = shadowColor, shape = shape)
        ) {
            Column(
                modifier = Modifier
                    .matchParentSize()
                    .padding(paddingContentSize)
            ){
                // Content to display
              content()
            }
        }
    }
}