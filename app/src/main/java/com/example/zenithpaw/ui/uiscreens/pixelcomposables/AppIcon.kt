package com.example.zenithpaw.ui.uiscreens.pixelcomposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.zenithpaw.R
import com.example.zenithpaw.ui.theme.ZenithPawTheme

@Composable
fun AppIcon(
    icon: ImageBitmap,
    iconSize: Dp = 80.dp,
    iconDescription: String,
    iconPadding: Dp = 4.dp,
    iconContentScale: ContentScale = ContentScale.Crop,
    borderWidth: Dp = 2.2.dp,
    borderColor: Color = Color.Black,
    iconShape: Shape = CircleShape,
    backgroundColor: Color = MaterialTheme.colorScheme.surface
    ){
        Box(
            modifier = Modifier
                .size(iconSize)
                .clip(iconShape)
                .border(width = borderWidth, color = borderColor, shape = iconShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ){
            Image(
                bitmap = icon,
                contentDescription = iconDescription,
                contentScale = iconContentScale,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(iconPadding)
            )
        }
}

@Composable
@Preview(showBackground = true, name = "AppIcon")
fun AppIconPreview(){
    ZenithPawTheme (dynamicColor = false) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val myAvatarImage: ImageBitmap = ImageBitmap.imageResource(R.drawable.cat_run_jump)
            AppIcon(
                icon = myAvatarImage,
                iconDescription = "Cat",
                iconShape = RoundedCornerShape(16.dp),
                backgroundColor = Color.Magenta
            )
        }
    }
}