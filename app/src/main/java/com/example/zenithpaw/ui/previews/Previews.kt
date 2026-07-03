package com.example.zenithpaw.ui.previews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zenithpaw.ui.theme.ZenithPawTheme
import com.example.zenithpaw.ui.uiscreens.LoadingScreenContent
import com.example.zenithpaw.ui.uiscreens.MainScreenContent
import com.example.zenithpaw.ui.uiscreens.pixelcomposables.AppButton
import com.example.zenithpaw.ui.user.UserUiState
import zahid.neobrutal.buttons.NeoButton

@Preview(showBackground = true, name = "Main Screen")
@Composable
fun MainScreenPreview() {
    ZenithPawTheme {
        MainScreenContent(
            uiState = UserUiState(isLoading = false, name = "John", email = "john@example.com"),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Loading Screen")
@Composable
fun LoadingScreenPreview() {
    ZenithPawTheme {
        LoadingScreenContent(modifier= Modifier)
    }
}

@Preview(showBackground = true, name = "AppButton Preview")
@Composable
fun AppButtonPreview() {
    // dynamic color false used custom theme instead of dynamic theme
    ZenithPawTheme (dynamicColor = false){
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
            AppButton(text = "App Button", onClick = {}, shadowColor = Color.Black, shadowOffset = 12.dp, modifier = Modifier.size(width = 300.dp, height = 175.dp), fontSize = 48.sp)
            Spacer(modifier = Modifier.height(16.dp))
            NeoButton(text = "Neo Button", onClick = {}, shadowColor = Color.Black, shadowOffset = 12.dp)
        }
    }
}