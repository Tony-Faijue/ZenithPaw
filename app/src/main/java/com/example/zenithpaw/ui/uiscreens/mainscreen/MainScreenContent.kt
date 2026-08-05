package com.example.zenithpaw.ui.uiscreens.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.zenithpaw.R
import com.example.zenithpaw.ui.theme.ZenithPawTheme
import com.example.zenithpaw.ui.uievents.UserUiEvent
import com.example.zenithpaw.ui.uiscreens.pixelcomposables.AppBasicBackgroundLayout
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
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiary)
            .fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth(),
        )
        {
            AppBasicBackgroundLayout(
                content = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                        ){ Row(){
                            Image(
                                bitmap = ImageBitmap.imageResource(R.drawable.zenith_paw_title),
                                contentDescription = R.string.app_name.toString(),
                                modifier = Modifier.size(250.dp, 50.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Icon(
                            bitmap = ImageBitmap.imageResource(R.drawable.zenith_paw_print),
                            contentDescription = "Paw Print",
                            modifier = Modifier.size(50.dp, 50.dp),
                            tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .size(width = 300.dp, height = 150.dp),
                shadowOffset = 6.dp,
                borderWidth = 2.dp,
                backgroundColor = MaterialTheme.colorScheme.secondary,
                )

            AppButton(
                text = if (uiState.isLoading) "Loading..." else  "Press to Start",
                onClick = { if (uiState.isLoading) {return@AppButton} else onEvent(UserUiEvent.OnStartButtonClicked) },
                shadowColor = Color.Black,
                shadowOffset = 12.dp,
                modifier = Modifier.size(250.dp, 100.dp),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontFamily = FontFamily.Monospace
                )
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