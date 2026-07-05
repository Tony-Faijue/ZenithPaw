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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zenithpaw.ui.theme.ZenithPawTheme
import com.example.zenithpaw.ui.uiscreens.MainScreenContent
import com.example.zenithpaw.ui.uiscreens.PixelLoadingScreen
import com.example.zenithpaw.ui.uiscreens.pixelcomposables.AppButton
import com.example.zenithpaw.ui.uiscreens.pixelcomposables.AppTextField
import com.example.zenithpaw.ui.user.UserUiState
import zahid.neobrutal.buttons.NeoButton
import zahid.neobrutal.inputs.NeoTextField

@Preview(showBackground = true, name = "Main Screen")
@Composable
fun MainScreenPreview() {
    ZenithPawTheme(dynamicColor = false) {
        MainScreenContent(
            uiState = UserUiState(isLoading = false, name = "John", email = "john@example.com"),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "AppButton Preview")
@Composable
fun AppButtonPreview() {
    // dynamic color false use custom theme instead of dynamic theme
    ZenithPawTheme (dynamicColor = false){
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
            AppButton(text = "App Button", onClick = {}, shadowColor = Color.Black, shadowOffset = 12.dp, modifier = Modifier.size(width = 300.dp, height = 175.dp))
            Spacer(modifier = Modifier.height(16.dp))
            NeoButton(text = "Neo Button", onClick = {}, shadowColor = Color.Black, shadowOffset = 12.dp)
        }
    }
}

@Preview(showBackground = true, name = "AppTextField Preview")
@Composable
fun AppTextFieldPreview() {
    // dynamic color false use custom theme instead of dynamic theme
    ZenithPawTheme (dynamicColor = false){
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
            AppTextField(
                value = "App TextField",
                onValueChange = {},
                label = "Label",
                placeholder = "Placeholder",
                modifier = Modifier.size(width = 300.dp, height = 80.dp),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 24.sp, fontFamily = FontFamily.Monospace)
            )
            Spacer(modifier = Modifier.height(16.dp))
            NeoTextField(
                value = "Neo TextField",
                onValueChange = {},
                label = "Label",
                placeholder = "Placeholder"
            )
        }
    }
}

@Preview(showBackground = true, name = "PixelLoadingScreen")
@Composable
fun PixelLoadingScreenPreview() {
    ZenithPawTheme {
        PixelLoadingScreen(isVisible = true, onAnimationFinished = {})
    }
}