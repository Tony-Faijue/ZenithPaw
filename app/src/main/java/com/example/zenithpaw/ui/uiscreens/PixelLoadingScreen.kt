package com.example.zenithpaw.ui.uiscreens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import coil3.gif.AnimatedImageDecoder
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.bitmapConfig
import coil3.request.crossfade
import com.example.zenithpaw.R

@Composable
fun PixelLoadingScreen(
    isVisible: Boolean,
    onAnimationFinished: () -> Unit
){
    val context = LocalContext.current

    /**
     * Configure explicitly an ImageRequest for Coil AsyncImage composable
     */
    val gifImageRequest = remember {ImageRequest.Builder(context)
        .data(R.drawable.loading_bar)
        .decoderFactory(AnimatedImageDecoder.Factory()) // Animated GIF support
        .crossfade(true)
        .allowHardware(false) // disallow hardware bitmaps
        .bitmapConfig(android.graphics.Bitmap.Config.ARGB_8888) //force standard pixel format
        .interceptorCoroutineContext(kotlinx.coroutines.Dispatchers.IO) // Use Dispatchers.IO for GIF decoding
        .listener(
            onStart = { Log.d("CoilDebug", "Image load started...") },
            onSuccess = { _, _ -> Log.d("CoilDebug", "Image loaded successfully!") },
            onError = { _, result -> Log.e("CoilDebug", "Image FAILED to load: ${result.throwable.localizedMessage}") }
        ) // Log image loading events
        .build() // builds the image request
    }

    // Use LaunchedEffect to handle visibility changes by moving the state side effect outside of main composable body
    LaunchedEffect(isVisible) {
        //Complete animation when screen is no longer visible
        if (!isVisible){
            onAnimationFinished()
        }
    }

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
            Box(
                modifier = Modifier.size(width = 240.dp, height = 70.dp)
            ){
                // Loads the gif image using Coil AsyncImage composable
                AsyncImage(
                    model = gifImageRequest,
                    contentDescription = "Loading ZenithPaw",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    filterQuality = FilterQuality.None // Prevents image from being blurred
                )
            }
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
}