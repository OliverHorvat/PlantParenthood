package com.example.plantparenthood

import DetailsScreen
import DetailsViewModel
import EditScreen
import EditViewModel
import LoginScreen
import MainScreen
import PlantScreen
import GardenScreen
import GardenViewModel
import RegisterScreen
import WelcomeScreen
import LoadingScreen
import LoginViewModel
import MainViewModel
import PlantViewModel
import RegisterViewModel
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.plantparenthood.ui.theme.PlantParenthoodTheme
import com.example.plantparenthood.utils.AuthHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var isBackPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlantParenthoodTheme {
                val navController = rememberNavController()
                var startDestination by remember { mutableStateOf("loading_screen") }
                var gardenViewModel: GardenViewModel = viewModel()
                var plantViewModel: PlantViewModel = viewModel()

                LaunchedEffect(Unit) {
                    val (savedEmail, savedPassword) = AuthHelper.loadCredentials(this@MainActivity)
                    val isRememberMeChecked = AuthHelper.isRememberMeChecked(this@MainActivity)

                    if (isRememberMeChecked && savedEmail.isNotEmpty() && savedPassword.isNotEmpty()) {
                        AuthHelper.signInSynchronously(savedEmail, savedPassword)
                        startDestination = "main_screen"
                    } else {
                        startDestination = "welcome_screen"
                    }
                }
                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None } )
                {
                    composable("loading_screen") {
                        LoadingScreen()
                    }
                    composable("welcome_screen") {
                        WelcomeScreen(navController = navController)
                    }
                    composable("main_screen") {
                        gardenViewModel = viewModel()
                        MainScreen(context = this@MainActivity, navController = navController, mainViewModel = MainViewModel())
                    }
                    composable("register_screen") {
                        RegisterScreen(context = this@MainActivity, navController = navController, registerViewModel = RegisterViewModel())
                    }
                    composable("login_screen") {
                        LoginScreen(context = this@MainActivity, navController = navController, loginViewModel = LoginViewModel())
                    }
                    composable("garden_screen") {
                        GardenScreen(context = this@MainActivity, navController = navController, gardenViewModel = gardenViewModel)
                    }
                    composable("plant_screen") {
                        plantViewModel = viewModel()
                        PlantScreen(context = this@MainActivity, plantId = gardenViewModel.screenPlantId, navController = navController, plantViewModel = plantViewModel)
                    }
                    composable("details_screen") {
                        DetailsScreen(context = this@MainActivity, type = plantViewModel.type, navController = navController, detailsViewModel = DetailsViewModel())
                    }
                    composable("edit_screen") {
                        EditScreen(context = this@MainActivity, plantId = gardenViewModel.screenPlantId, navController = navController, editViewModel = EditViewModel())
                    }
                }
            }
        }
    }
    override fun onBackPressed() {
        if (isBackPressedOnce) {
            moveTaskToBack(true)
        } else {
            isBackPressedOnce = true
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

            lifecycleScope.launch {
                delay(2000)
                isBackPressedOnce = false
            }
        }
    }
}
