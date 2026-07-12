package com.example.zenithpaw.ui.uiscreens.pixelcomposables

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


/**
 * Custom NeoBrutal Toggle Button override to allow for
 * custom sizing of the button, icon and text independently
 *
 * @param modifier Modifier to be applied to the button
 * @param text Text to be displayed on the button
 * @param iconId The resource ID of the icon to display
 * @param activeIconTint The tint color of the active icon
 * @param inactiveIconTint The tint color of the inactive icon
 * @param iconHeight The height of the icon
 * @param iconWidth The width of the icon
 * @param stroke The stroke of the button
 * @param spacerWidth The width of the spacer between the icon and text
 * @param enabled Whether the button is enabled or not
 * @param isActive Whether the button is active or not
 * @param onToggle The callback to be invoked when the button is toggled
 * @param activeColor The color of the button when it is active
 * @param inactiveColor The color of the button when it is inactive
 * @param activeTextColor The color of the text when the button is active
 * @param inactiveTextColor The color of the text when the button is inactive
 * @param shadowOffset The offset of the shadow
 * @param shadowColor The color of the shadow
 * @param shape The shape of the button and the shadow
 * @param textStyle The style of the text
 */
@Composable
fun AppToggleButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    iconId: Int,
    activeIconTint: Color = MaterialTheme.colorScheme.onPrimary,
    inactiveIconTint: Color = MaterialTheme.colorScheme.onBackground,
    iconHeight: Dp = 24.dp,
    iconWidth: Dp = 24.dp,
    stroke: Stroke = Stroke(width = 5f),
    spacerWidth: Dp = 8.dp,
    enabled: Boolean = true,
    isActive: Boolean,
    onToggle: (Boolean) -> Unit,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.surface,
    activeTextColor: Color = MaterialTheme.colorScheme.onPrimary,
    inactiveTextColor: Color = MaterialTheme.colorScheme.onSurface,
    shadowOffset: Dp = 4.dp,
    shadowColor: Color = Color.Black,
    shape: Shape = RectangleShape,
    textStyle: TextStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp
    )
){

    Box(
        modifier = modifier
            // Removed the wrapContentSize()
            // so that root container can receive the size from the modifier
            .padding(bottom = shadowOffset, end = shadowOffset)
    ) {
        if (isActive) {
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
                        x = shadowOffset / 2,
                        y = shadowOffset / 2
                    )
                    .clickable(enabled = enabled) {
                        onToggle(!isActive)
                    }
                    .background(activeColor, shape)
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
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(iconId),
                        contentDescription = null,
                        tint = activeIconTint,
                        modifier = Modifier
                            .width(iconWidth)
                            .height(iconHeight)
                    )
                    if (!text.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.width(spacerWidth))
                        Text(
                            text = text,
                            color = activeTextColor,
                            style = textStyle,
                        )
                    }
                }
            }
        }
        else {
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
                        x =  shadowOffset / 2,
                        y = shadowOffset / 2
                    )
                    .clickable(enabled = enabled) {
                        onToggle(!isActive)
                    }
                    .background(inactiveColor, shape)
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
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(iconId),
                        contentDescription = null,
                        tint = inactiveIconTint,
                        modifier = Modifier
                            .width(iconWidth)
                            .height(iconHeight)
                    )
                    if (!text.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.width(spacerWidth))
                        Text(
                            text = text,
                            color = inactiveTextColor,
                            style = textStyle,
                        )
                    }
                }
            }
        }
    }
}