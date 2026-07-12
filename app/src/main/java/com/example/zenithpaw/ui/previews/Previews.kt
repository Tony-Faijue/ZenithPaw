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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zenithpaw.R
import com.example.zenithpaw.ui.theme.ZenithPawTheme
import com.example.zenithpaw.ui.uiscreens.MainScreenContent
import com.example.zenithpaw.ui.uiscreens.PixelLoadingScreen
import com.example.zenithpaw.ui.uiscreens.pixelcomposables.AppButton
import com.example.zenithpaw.ui.uiscreens.pixelcomposables.AppIconButton
import com.example.zenithpaw.ui.uiscreens.pixelcomposables.AppTextField
import com.example.zenithpaw.ui.uiscreens.pixelcomposables.AppToggleButton
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
@Preview(showBackground = true, name = "AppIconButton Preview")
@Composable
fun AppIconButtonPreview() {
    val myTextStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
    val myStroke = Stroke(width = 10f)
    // dynamic color false use custom theme instead of dynamic theme
    ZenithPawTheme (dynamicColor = false){
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
            AppIconButton(
                text = "App Icon Button", onClick = {}, shadowColor = Color.Black, shadowOffset = 12.dp,
                iconId = R.drawable.gift_solid_full, iconHeight = 35.dp,iconWidth = 35.dp, iconTint = Color.White,
                spacerWidth = 4.dp, stroke = myStroke, textStyle = myTextStyle,
                modifier = Modifier.size(width = 250.dp, height = 100.dp))
        }
    }
}
@Preview(showBackground = true, name = "AppToggleButton Preview")
@Composable
fun AppToggleButtonPreview() {
    val myTextStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
    val myStroke = Stroke(width = 3f)
    // dynamic color false use custom theme instead of dynamic theme
    ZenithPawTheme (dynamicColor = false){
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
            AppToggleButton(
                text = "App Toggle Button", onToggle = {}, isActive = false, shadowColor = Color.Black, shadowOffset = 16.dp,
                iconId = R.drawable.check_solid_full, iconHeight = 35.dp,iconWidth = 35.dp,
                spacerWidth = 4.dp, stroke = myStroke, textStyle = myTextStyle,
                modifier = Modifier.size(width = 250.dp, height = 100.dp))
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