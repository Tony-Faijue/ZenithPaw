package com.example.zenithpaw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zenithpaw.ui.theme.ZenithPawTheme
import com.example.zenithpaw.ui.uiscreens.MainScreenContent
import com.example.zenithpaw.ui.uiscreens.PixelLoadingScreen
import com.example.zenithpaw.ui.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZenithPawTheme {
                MainScreen()
//              PixelLoadingScreen(true, {})
            }
        }
    }
}

/**
 * The main screen of the app.
 * @param viewModel a UserViewModel for the screen which is a HiltViewModel dependency
 */
@Composable
fun MainScreen(viewModel: UserViewModel = hiltViewModel()) {
    // Collect the UI state from the ViewModel
    val uiState by viewModel.uiState.collectAsState()
    MainScreenContent(uiState = uiState, onEvent = viewModel::onEvent)
}