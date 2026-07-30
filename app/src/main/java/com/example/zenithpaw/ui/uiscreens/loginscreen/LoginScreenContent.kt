package com.example.zenithpaw.ui.uiscreens.loginscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.zenithpaw.ui.theme.ZenithPawTheme
import com.example.zenithpaw.ui.uievents.UserUiEvent
import com.example.zenithpaw.ui.user.UserUiState

@Composable
fun LoginScreenContent(
    uiState: UserUiState,
    onEvent: (UserUiEvent) -> Unit,
){
    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        LoginUserDialog(
            uiState,
            onEvent = onEvent, //Pass the ViewModel's onEvent directly
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true, name = "Login Screen")
@Composable
fun LoginScreenPreview(){
    ZenithPawTheme(dynamicColor = false) {
        LoginScreenContent(
            uiState = UserUiState(),
            onEvent = {}
        )
    }
}