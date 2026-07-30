package com.example.zenithpaw.ui.uiscreens.mainscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.zenithpaw.ui.theme.ZenithPawTheme
import com.example.zenithpaw.ui.uievents.UserUiEvent
import com.example.zenithpaw.ui.uiscreens.pixelcomposables.AppButton
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
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
            AppButton(
                text = if (uiState.isLoading) "Loading..." else  "Press to Start",
                onClick = { if (uiState.isLoading) {return@AppButton} else onEvent(UserUiEvent.OnStartButtonClicked) },
                shadowColor = Color.Black,
                shadowOffset = 12.dp,
                modifier = Modifier.size(250.dp, 100.dp),
                textStyle = MaterialTheme.typography.titleLarge,
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