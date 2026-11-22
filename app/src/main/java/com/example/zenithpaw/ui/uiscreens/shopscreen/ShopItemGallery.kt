package com.example.zenithpaw.ui.uiscreens.shopscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.zenithpaw.ui.shop.ShopUiState
import com.example.zenithpaw.ui.uievents.ShopUiEvent



@Composable
fun ShopItemGallery(
    uiState: ShopUiState,
    modifier: Modifier,
    rowWidth: Dp = 400.dp,
    rowHeight: Dp = 200.dp,
    itemWidth: Dp = 250.dp,
    itemHeight: Dp = 150.dp
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(8.dp)
            .size(rowWidth, rowHeight)
    ){
        Column(
            modifier = Modifier
                .weight(1f)
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .border(1.dp, MaterialTheme.colorScheme.outline)
        ){
            LazyHorizontalGrid(
                rows = GridCells.Fixed(1), // Define how many rows wanted
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                // Get shop items from uiState
                items(uiState.availableItems){ item ->
                    ShopItemCard(
                        state = item,
                        isPreviewVisible = uiState.isPreviewVisible,
                        onClick = {
                            // To preview confirmation dialog
                            ShopUiEvent.OnShowConfirmBuyDialogClicked(item.shopItemId)
                        },
                        modifier = Modifier.size(width = itemWidth, height = itemHeight),
                        )
                }
            }
        }
    }
}