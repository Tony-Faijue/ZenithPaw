package com.example.zenithpaw.ui.uiscreens.mainscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.zenithpaw.ui.theme.ZenithPawTheme
import com.example.zenithpaw.ui.uievents.UserUiEvent
import com.example.zenithpaw.ui.uiscreens.registerscreen.RegisterUserDialog
import com.example.zenithpaw.ui.user.UserUiState

/**
 * The main screen content to show in the UI
 * @param uiState the current UserUiState of the UI
 * @param onEvent a UserUiEvent to handle UI events
 */
@Composable
fun MainScreenContent(
    uiState: UserUiState,
    onEvent: (UserUiEvent) -> Unit,
    modifier: Modifier = Modifier
){
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        // Render the app content
        // Show RegistrationDialog if not registered
        if (!uiState.isRegistered){
            RegisterUserDialog(
                uiState,
                onEvent = onEvent, //Pass the ViewModel's onEvent directly
                modifier = Modifier
            )
        } else {
            //Show Main Screen
            Text(
                modifier = Modifier,
                text = "Sample Main Screen, Hello ${uiState.name}!"
            )
        }
    }
}

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