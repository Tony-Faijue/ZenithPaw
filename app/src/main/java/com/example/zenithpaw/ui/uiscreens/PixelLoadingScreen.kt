package com.example.zenithpaw.ui.uiscreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.zenithpaw.R

@Composable
fun PixelLoadingScreen(
    isVisible: Boolean,
    onAnimationFinished: () -> Unit
){
    val context = LocalContext.current

    // Configure explicitly an ImageRequest for Coil AsyncImage
    val gifImageRequest = ImageRequest.Builder(context)
        .data(R.drawable.loading_bar)
        .build()

    // Animates the composable visibility to fade out
    AnimatedVisibility(
        visible = isVisible,
        exit = fadeOut()
    ){
        Column(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.tertiary),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            // Loads the gif image using Coil AsyncImage composable
            AsyncImage(
                model = gifImageRequest,
                contentDescription = "Loading ZenithPaw",
                modifier = Modifier.size(width = 240.dp, height = 70.dp),
                contentScale = ContentScale.Fit,
                filterQuality = FilterQuality.None // Prevents image from being blurred
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondaryContainer,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }

    //Complete animation when screen is no longer visible
    if (!isVisible){
        onAnimationFinished()
    }
}