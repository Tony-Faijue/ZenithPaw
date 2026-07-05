package com.example.zenithpaw.ui.uiscreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.zenithpaw.ui.uievents.UserUiEvent
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
    var isDismissed by rememberSaveable { mutableStateOf(false) }
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

        // Overlay the loading scree on top of the content
        if (!isDismissed) {
            //Show Loading Screen
            PixelLoadingScreen(
                isVisible = uiState.isLoading,
                onAnimationFinished = {
                    isDismissed = true // Permanently dismiss the loading screen
                }
            )
        }
    }
}