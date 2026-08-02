package com.example.zenithpaw.ui.uiscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Data class representing a navigation item in the bottom navigation bar.
 */
data class NavigationItem(
    val label: String,
    val iconRes: Int,
    val contentDesc: String,
    val iconSize: Dp,
    val route: String,
)

/**
 * Custom bottom navigation bar slot for navigation items.
 * @param containerColor the color of the bottom navigation bar container
 * @param content the content of the bottom navigation bar
 */
@Composable
fun CustomBottomNavigationBarSlot(
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable RowScope.() -> Unit
    ){
    Surface(
        color = containerColor,
        modifier = Modifier.fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

/**
 * Composable representing a single item in the bottom navigation bar.
 */
@Composable
fun RowScope.AppBottomNavigationItem(
    item: NavigationItem,
    isSelected: Boolean,
    onClick: () -> Unit
){
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .weight(1f)
            .height(64.dp)
            .clickable(onClick = onClick)
            .background(backgroundColor)
            .drawBehind {
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 4.dp.toPx()
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Icon(
                painter = painterResource(id = item.iconRes),
                contentDescription = item.contentDesc,
                tint = contentColor,
                modifier = Modifier.size(item.iconSize)
            )
            Text(
                text = item.label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                ),
                color = contentColor
            )
        }
    }
}
