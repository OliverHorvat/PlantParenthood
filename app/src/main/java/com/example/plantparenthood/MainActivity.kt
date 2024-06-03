package com.example.plantparenthood

import DetailsScreen
import EditScreen
import LoginScreen
import MainScreen
import PlantScreen
import GardenScreen
import RegisterScreen
import WelcomeScreen
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.plantparenthood.ui.theme.PlantParenthoodTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "welcome_screen") {
                composable("welcome_screen") {
                    WelcomeScreen(
                        navController = navController
                    )
                }
                composable("main_screen") {
                    MainScreen(
                        context = this@MainActivity,
                        navController = navController,
                        mainViewModel = MainViewModel()
                        )
                }
                composable("register_screen") {
                    RegisterScreen(
                        context = this@MainActivity,
                        navController = navController,
                        registerViewModel = RegisterViewModel()
                    )
                }
                composable("login_screen") {
                    LoginScreen(
                        context = this@MainActivity,
                        navController = navController,
                        loginViewModel = LoginViewModel()
                    )
                }
                composable("garden_screen") {
                    GardenScreen(
                    )
                }
                composable("plant_screen") {
                    PlantScreen(
                    )
                }
                composable("details_screen") {
                    DetailsScreen(
                    )
                }
                composable("edit_screen") {
                    EditScreen(
                    )
                }
            }
        }
    }
}