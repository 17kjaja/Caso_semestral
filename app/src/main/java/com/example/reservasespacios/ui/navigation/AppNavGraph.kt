package com.example.reservasespacios.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.reservasespacios.ui.screens.CatalogoScreen
import com.example.reservasespacios.ui.screens.LoginScreen
import com.example.reservasespacios.ui.screens.NuevaReservaScreen

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
            CatalogoScreen(
                onReservar = { espacioId ->
                    navController.navigate("nueva_reserva/$espacioId")
                }
            )
        }
        composable(
            route = "nueva_reserva/{espacioId}",
            arguments = listOf(navArgument("espacioId") { type = NavType.IntType })
        ) { entrada ->
            val espacioId = entrada.arguments?.getInt("espacioId") ?: 0
            NuevaReservaScreen(
                espacioId = espacioId,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}