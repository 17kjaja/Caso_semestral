package com.example.reservasespacios.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.reservasespacios.ui.components.BannerSinConexion
import com.example.reservasespacios.ui.screens.AdminScreen
import com.example.reservasespacios.ui.screens.BloqueosScreen
import com.example.reservasespacios.ui.screens.CatalogoScreen
import com.example.reservasespacios.ui.screens.EspacioFormScreen
import com.example.reservasespacios.ui.screens.EspaciosAdminScreen
import com.example.reservasespacios.ui.screens.LoginScreen
import com.example.reservasespacios.ui.screens.MisSolicitudesScreen
import com.example.reservasespacios.ui.screens.NotificacionesScreen
import com.example.reservasespacios.ui.screens.NuevaReservaScreen
import com.example.reservasespacios.util.SesionActual

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    Box {
        NavHost(
            navController = navController,
            startDestination = "login",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it / 3 }) + fadeOut() },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it / 3 }) + fadeIn() },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
        ) {
            composable("login") {
                LoginScreen(
                    onLoginExitoso = { perfil ->
                        val destino = if (perfil == "ADMIN") "admin" else "catalogo"
                        navController.navigate(destino) {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable("catalogo") {
                CatalogoScreen(
                    onReservar = { espacioId ->
                        navController.navigate("nueva_reserva/$espacioId")
                    },
                    onMisSolicitudes = { navController.navigate("mis_solicitudes") },
                    onNotificaciones = { navController.navigate("notificaciones") },
                    onCerrarSesion = {
                        SesionActual.usuarioId = 0
                        SesionActual.perfil = ""
                        navController.navigate("login") {
                            popUpTo("catalogo") { inclusive = true }
                        }
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
            composable("mis_solicitudes") {
                MisSolicitudesScreen(onVolver = { navController.popBackStack() })
            }
            composable("notificaciones") {
                NotificacionesScreen(onVolver = { navController.popBackStack() })
            }
            composable("admin") {
                AdminScreen(
                    onEspacios = { navController.navigate("admin_espacios") },
                    onBloqueos = { navController.navigate("bloqueos") },
                    onCerrarSesion = {
                        SesionActual.usuarioId = 0
                        SesionActual.perfil = ""
                        navController.navigate("login") {
                            popUpTo("admin") { inclusive = true }
                        }
                    }
                )
            }
            composable("admin_espacios") {
                EspaciosAdminScreen(
                    onVolver = { navController.popBackStack() },
                    onNuevo = { navController.navigate("espacio_form/0") },
                    onEditar = { id -> navController.navigate("espacio_form/$id") }
                )
            }
            composable(
                route = "espacio_form/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entrada ->
                val id = entrada.arguments?.getInt("id") ?: 0
                EspacioFormScreen(
                    espacioId = id,
                    onVolver = { navController.popBackStack() }
                )
            }
            composable("bloqueos") {
                BloqueosScreen(onVolver = { navController.popBackStack() })
            }
        }

        BannerSinConexion(modifier = Modifier.align(Alignment.BottomCenter))
    }
}