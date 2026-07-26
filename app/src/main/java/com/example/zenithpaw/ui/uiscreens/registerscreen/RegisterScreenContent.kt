package com.example.zenithpaw.ui.uiscreens.registerscreen

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

/**
 * The register screen content to show in the UI
 * @param uiState the current UserUiState of the UI
 * @param onEvent a UserUiEvent to handle UI events
 */
@Composable
fun RegisterScreenContent(
    uiState: UserUiState,
    onEvent: (UserUiEvent) -> Unit,
){
    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        RegisterUserDialog(
            uiState,
            onEvent = onEvent, //Pass the ViewModel's onEvent directly
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true, name = "Register Screen")
@Composable
fun RegisterScreenPreview(){
    ZenithPawTheme(dynamicColor = false) {
        RegisterScreenContent(
            uiState = UserUiState(isLoading = false, name = "John", email = "john@example.com"),
            onEvent = {}
        )
    }
}