package com.example.zenithpaw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.zenithpaw.ui.navigation.Screen
import com.example.zenithpaw.ui.theme.ZenithPawTheme
import com.example.zenithpaw.ui.uievents.NavigationEvent
import com.example.zenithpaw.ui.uiscreens.AppBottomNavigationItem
import com.example.zenithpaw.ui.uiscreens.CustomBottomNavigationBarSlot
import com.example.zenithpaw.ui.uiscreens.NavigationItem
import com.example.zenithpaw.ui.uiscreens.loginscreen.LoginScreenContent
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

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                // Which routes have bottom nav bar navigation
                val showBottomBar = currentRoute in listOf(
                    Screen.Profile.route,
                    Screen.Shop.route,
                    Screen.Tasks.route,
                    Screen.Pets.route,
                )
                Scaffold(
                    bottomBar = {
                        if (showBottomBar){
                            BottomNav(navController, currentRoute)
                        }
                    }
                ){ innerPadding ->
                    NavHost(navController = navController, startDestination = Screen.Main.route, modifier = Modifier.padding(innerPadding)){

                        val navigateAction: (String, String?, Boolean) -> Unit = { route, popUpToRoute, inclusive ->
                            navController.navigate(route){
                                popUpToRoute?.let { popRoute ->
                                    popUpTo(popRoute){ this.inclusive = inclusive
                                    }
                                }
                            }
                        }
                        // Navigation graph for the app
                        composable(route = Screen.Main.route){
                            MainScreen(
                                onNavigate = navigateAction,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(route = Screen.Registration.route){
                            RegistrationScreen(
                                onNavigate = navigateAction,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(route = Screen.Login.route){
                            LoginScreen(
                                onNavigate = navigateAction,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(route = Screen.Profile.route){Text("Profile")}
                        composable(route = Screen.Shop.route){}
                        composable(route = Screen.Tasks.route){}
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
}

/**
 * Composable for the bottom navigation bar.
 */
@Composable
fun BottomNav(navController: NavHostController, currentRoute: String?){
    val items = listOf(
        NavigationItem("Profile", R.drawable.user_solid_full, "Profile", 24.dp, Screen.Profile.route),
        NavigationItem("Shop", R.drawable.shop_solid_full, "Shop", 24.dp, Screen.Shop.route),
        NavigationItem("Tasks", R.drawable.clipboard_solid_full, "Tasks", 24.dp, Screen.Tasks.route),
        NavigationItem("Pets", R.drawable.paw_solid_full, "Pets", 24.dp, Screen.Pets.route)
    )
    CustomBottomNavigationBarSlot {
        items.forEach { item ->
            AppBottomNavigationItem(
                item = item,
                isSelected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route){
                        popUpTo(Screen.Profile.route){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
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

@Composable
fun LoginScreen(viewModel: UserViewModel = hiltViewModel(), onNavigate: (String, String?, Boolean) -> Unit, onNavigateBack: () -> Unit){
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.navigationEvent){
        viewModel.navigationEvent.collect{event ->
            when(event){
                is NavigationEvent.Navigate -> onNavigate(event.route, event.popUpToRoute, event.inclusive)
                is NavigationEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    LoginScreenContent(uiState = uiState, onEvent = viewModel::onEvent)
}