package com.example.zenithpaw.ui.uiscreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        when {
            //Show Loading Screen
            uiState.isLoading -> {
                LoadingScreenContent(modifier = Modifier)
            }
            //Show Registration Screen
            !uiState.isRegistered -> {
                RegisterUserDialog(
                    uiState,
                    onEvent = onEvent, //Pass the ViewModel's onEvent directly
                    modifier = Modifier
                )
            }
            //Show Main Screen
            else -> {
                Text(
                    modifier = Modifier,
                    text = "Sample Main Screen, Hello ${uiState.name}!"
                )
            }
        }
    }
}