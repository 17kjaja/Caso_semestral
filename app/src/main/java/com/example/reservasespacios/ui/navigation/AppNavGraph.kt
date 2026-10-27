package com.example.reservasespacios.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.reservasespacios.ui.screens.CatalogoScreen
import com.example.reservasespacios.ui.screens.LoginScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginExitoso = {
                    navController.navigate("catalogo") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("catalogo") {
            CatalogoScreen()
        }
    }
}