package com.example.zenithpaw.ui.uiscreens.shopscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zenithpaw.R
import com.example.zenithpaw.ui.shopitem.ShopItemResourceMapper
import com.example.zenithpaw.ui.shopitem.ShopItemUiState
import com.example.zenithpaw.ui.theme.ZenithPawTheme
import com.example.zenithpaw.ui.uievents.ShopUiEvent
import com.example.zenithpaw.ui.uiscreens.pixelcomposables.AppBasicCard
import com.example.zenithpaw.ui.uiscreens.pixelcomposables.AppIconButton
import com.example.zenithpaw.ui.uiscreens.pixelcomposables.AppImageButton

/**
 * Composable for the Shop Item Card
 */
@Composable
fun ShopItemCard(
    state: ShopItemUiState,
    onEvent: (ShopUiEvent) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    shadowColor: Color = Color.Black,
    shadowOffset: Dp = 8.dp,
    borderWidth: Dp = 2.2.dp,
    shape: Shape = RectangleShape,
    stroke: Stroke = Stroke(width = 10f),
    textStyle: TextStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp),
    buttonTextStyle: TextStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 8.sp
    )
){
    AppBasicCard(
        modifier = modifier,
        backgroundColor = backgroundColor,
        shadowColor = shadowColor,
        shadowOffset = shadowOffset,
        borderWidth = borderWidth,
        shape = shape,
        content = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ){
                // Item Title
                Text(
                    text = state.name,
                    style = textStyle,
                )
                // Item Image
                Image(
                    bitmap = ImageBitmap.imageResource(ShopItemResourceMapper.getResourceId(state.shopItemId)),
                    contentDescription = state.description,
                    modifier = Modifier.size(width = 50.dp, height = 50.dp),
                    filterQuality = FilterQuality.None,
                )
                Spacer(modifier = Modifier.height(4.dp))

                AppImageButton(
                    onClick = onClick,
                    text = "${state.price}",
                    textStyle = buttonTextStyle,
                    imageId = R.drawable.coin_sprite, imageHeight = 25.dp, imageWidth = 25.dp,
                    stroke = stroke,
                    modifier = Modifier.size(width = 80.dp, height = 50.dp),
                    backgroundColor = MaterialTheme.colorScheme.primary,
                )
            }
        }
    )
}

