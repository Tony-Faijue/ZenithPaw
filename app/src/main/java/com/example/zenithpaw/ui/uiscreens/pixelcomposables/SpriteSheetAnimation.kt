package com.example.zenithpaw.ui.uiscreens.pixelcomposables

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.delay

/**
 * Data class to store the dimensions of a sprite sheet
 */
data class SpriteDimensions(
    val bitmap: ImageBitmap,
    val height: Int,
    val width: Int,
)

/**
 * Composable to display a sprite sheet animation
 * @param drawableRes the resource ID of the sprite sheet drawable
 * @param frameCount the number of frames in the animation
 * @param frameDurationMs the duration of each frame in milliseconds
 * @param modifier the modifier to apply to the composable
 */
@Composable
fun SpriteSheetAnimation(
    drawableRes: Int,
    frameCount: Int,
    frameDurationMs: Long = 100L,
    modifier: Modifier
){
    /**
     * The current frame index of the animation to store its state reference
     */
    var currentFrame by remember { mutableIntStateOf(0) }

    /**
     * Coroutine to run the animation loop based on the frame duration in milliseconds
     * to update the currentFrame state only if the key changes
     */
    LaunchedEffect(Unit) {
        while(true){
            delay(frameDurationMs)
            currentFrame = (currentFrame + 1) % frameCount
        }
    }

    // Load the image bitmap from the drawable resource for pixel by pixel manipulation
    // which allows to determine the frame dimensions based on the number of frames & bitmap size
    val bitmap = ImageBitmap.imageResource(id = drawableRes)
    val spriteDimensions = remember(drawableRes, frameCount) {
        val srcFrameWidth =  bitmap.width / frameCount
        val srcFrameHeight = bitmap.height
        SpriteDimensions(bitmap, srcFrameHeight, srcFrameWidth)
    }

    // Draw the current frame of the animation including the
    // offset and scaling to fill the container size
    Canvas(modifier = modifier){
        val srcOffset = currentFrame * spriteDimensions.width
        drawImage(
            image = bitmap,
            //Determine the area to slice in the bitmap
            srcOffset = IntOffset(srcOffset, 0),
            srcSize = IntSize(spriteDimensions.width, spriteDimensions.height),
            // Determine the area to draw in the canvas
            dstOffset = IntOffset(0, 0),
            dstSize = IntSize(size.width.toInt(), size.height.toInt()),
            filterQuality = FilterQuality.None
        )
    }
}