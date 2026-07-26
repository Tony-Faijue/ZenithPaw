package com.example.zenithpaw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.zenithpaw.ui.navigation.Screen
import com.example.zenithpaw.ui.theme.ZenithPawTheme
import com.example.zenithpaw.ui.uiscreens.mainscreen.MainScreenContent
import com.example.zenithpaw.ui.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZenithPawTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.Main.route){
                    // Navigation graph for the app
                    composable(route = Screen.Main.route){}
                    composable(route = Screen.Registration.route){}
                    composable(route = Screen.Login.route){}
                    composable(route = Screen.Profile.route){}
                    composable(route = Screen.Shop.route){}
                    composable(route = Screen.Task.route){}
                    composable(route = Screen.TaskDetails.route){}
                    composable(route = Screen.Pets.route){}
                    composable(route = Screen.PetDetails.route){}
                }
//              MainScreen()
//              PixelLoadingScreen(true, {})
//              SpriteSheetAnimation(R.drawable.cat_run_jump, 3, modifier = Modifier.size(width = 200.dp, height = 150.dp, ))
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