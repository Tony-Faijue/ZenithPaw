package com.example.zenithpaw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.zenithpaw.ui.navigation.Screen
import com.example.zenithpaw.ui.theme.ZenithPawTheme
import com.example.zenithpaw.ui.uievents.NavigationEvent
import com.example.zenithpaw.ui.uiscreens.mainscreen.MainScreenContent
import com.example.zenithpaw.ui.uiscreens.registerscreen.RegisterScreenContent
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
                    composable(route = Screen.Main.route){
                        MainScreen(
                            onNavigate = { route, popUpToRoute, inclusive ->
                                navController.navigate(route){
                                    popUpToRoute?.let { popRoute ->
                                        popUpTo(popRoute){ this.inclusive = inclusive
                                        }
                                    }
                                }
                            },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable(route = Screen.Registration.route){
                        RegistrationScreen(
                            onNavigate = { route, popUpToRoute, inclusive ->
                                navController.navigate(route){
                                    popUpToRoute?.let { popRoute ->
                                        popUpTo(popRoute){ this.inclusive = inclusive
                                        }
                                    }
                                }
                            },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable(route = Screen.Login.route){}
                    composable(route = Screen.Profile.route){}
                    composable(route = Screen.Shop.route){}
                    composable(route = Screen.Task.route){}
                    composable(route = Screen.TaskDetails.route){}
                    composable(route = Screen.Pets.route){}
                    composable(route = Screen.PetDetails.route){}
                }
//              PixelLoadingScreen(true, {})
//              SpriteSheetAnimation(R.drawable.cat_run_jump, 3, modifier = Modifier.size(width = 200.dp, height = 150.dp, ))
            }
        }
    }
}

/**
 * The main screen of the app.
 * @param viewModel a UserViewModel for the screen which is a HiltViewModel dependency
 * @param onNavigate a function to navigate to a new screen
 * @param onNavigateBack a function to navigate back to the previous screen
 */
@Composable
fun MainScreen(viewModel: UserViewModel = hiltViewModel(), onNavigate: (String, String?, Boolean) -> Unit, onNavigateBack: () -> Unit) {
    // Collect the UI state from the ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Listen for navigation events from the ViewModel and collect it
    LaunchedEffect(viewModel.navigationEvent){
        viewModel.navigationEvent.collect{event ->
            when(event){
                is NavigationEvent.Navigate -> onNavigate(event.route, event.popUpToRoute, event.inclusive)
                is NavigationEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    MainScreenContent(uiState = uiState, onEvent = viewModel::onEvent)
}

@Composable
fun RegistrationScreen(viewModel: UserViewModel = hiltViewModel(), onNavigate: (String, String?, Boolean) -> Unit, onNavigateBack: () -> Unit){
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.navigationEvent){
        viewModel.navigationEvent.collect{event ->
            when(event){
                is NavigationEvent.Navigate -> onNavigate(event.route, event.popUpToRoute, event.inclusive)
                is NavigationEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    RegisterScreenContent(uiState = uiState, onEvent = viewModel::onEvent)
}