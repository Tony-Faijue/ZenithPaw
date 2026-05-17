package com.example.zenithpaw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zenithpaw.ui.theme.ZenithPawTheme
import com.example.zenithpaw.ui.uievents.UserUiEvent
import com.example.zenithpaw.ui.uiscreens.RegisterUserDialog
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
            }
        }
    }
}


@Composable
fun MainScreen(viewModel: UserViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
    ) { when {
            //Show Loading Screen
            uiState.isLoading -> {}
            //Show Registration Screen
             !uiState.isRegistered -> {
                RegisterUserDialog(
                    uiState,
                    viewModel::onEvent, //Pass the ViewModel's onEvent directly
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

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ZenithPawTheme {
        Text("Sample Main Screen Preview")
    }
}