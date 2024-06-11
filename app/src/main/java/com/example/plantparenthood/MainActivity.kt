package com.example.plantparenthood

import DetailsScreen
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
import RegisterViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.plantparenthood.ui.theme.PlantParenthoodTheme
import com.example.plantparenthood.utils.AuthHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlantParenthoodTheme {
                val navController = rememberNavController()
                var startDestination by remember { mutableStateOf("loading_screen") }

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

                NavHost(navController = navController, startDestination = startDestination) {
                    composable("loading_screen") {
                        LoadingScreen(navController = navController)
                    }
                    composable("welcome_screen") {
                        WelcomeScreen(navController = navController)
                    }
                    composable("main_screen") {
                        MainScreen(context = this@MainActivity, navController = navController, mainViewModel = MainViewModel())
                    }
                    composable("register_screen") {
                        RegisterScreen(context = this@MainActivity, navController = navController, registerViewModel = RegisterViewModel())
                    }
                    composable("login_screen") {
                        LoginScreen(context = this@MainActivity, navController = navController, loginViewModel = LoginViewModel())
                    }
                    composable("garden_screen") {
                        GardenScreen(context = this@MainActivity, navController = navController, gardenViewModel = GardenViewModel())
                    }
                    composable("plant_screen") {
                        PlantScreen()
                    }
                    composable("details_screen") {
                        DetailsScreen()
                    }
                    composable("edit_screen") {
                        EditScreen(context = this@MainActivity, navController = navController, editViewModel = EditViewModel())
                    }
                }
            }
        }
    }
}
