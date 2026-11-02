package com.example.monitoreo_silos.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.monitoreo_silos.ui.AuthViewModel
import com.example.monitoreo_silos.ui.screens.AdminDashboardScreen
import com.example.monitoreo_silos.ui.screens.CamaraGaleriaScreen
import com.example.monitoreo_silos.ui.screens.FormMovimientoScreen
import com.example.monitoreo_silos.ui.screens.HomeScreen
import com.example.monitoreo_silos.ui.screens.LoginScreen
import com.example.monitoreo_silos.ui.screens.RegisterScreen
import com.example.monitoreo_silos.ui.screens.SiloDetailScreen
import com.example.monitoreo_silos.ui.viewmodel.FormMovimientoViewModel
import com.example.monitoreo_silos.ui.viewmodel.SiloUiState
import com.example.monitoreo_silos.ui.viewmodel.SiloViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object AdminDashboard : Screen("admin_dashboard")
    object CameraGallery : Screen("camera_gallery")

    object SiloDetail : Screen("silo_detail/{siloId}") {
        fun createRoute(siloId: Int) = "silo_detail/$siloId"
    }

    object FormMovimiento : Screen("form_movimiento/{siloId}") {
        fun createRoute(siloId: Int) = "form_movimiento/$siloId"
    }
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    siloViewModel: SiloViewModel,
    formMovimientoViewModel: FormMovimientoViewModel
) {
    val navController = rememberNavController()
    val silosUiState by siloViewModel.uiState.collectAsState()

    val silosList = when (val state = silosUiState) {
        is SiloUiState.Success -> state.silos
        else -> emptyList()
    }

    val usuarioActual = authViewModel.currentUser?.nombre
        ?: authViewModel.currentUser?.username
        ?: "Usuario"

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {

        // LOGIN
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    val userRol = authViewModel.currentUser?.rol

                    val destino = if (
                        userRol == "SUPERVISOR" || userRol == "JEFATURA"
                    ) {
                        Screen.AdminDashboard.route
                    } else {
                        Screen.Home.route
                    }

                    navController.navigate(destino) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // REGISTRO
        composable(Screen.Register.route) {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    val userRol = authViewModel.currentUser?.rol

                    val destino = if (
                        userRol == "SUPERVISOR" || userRol == "JEFATURA"
                    ) {
                        Screen.AdminDashboard.route
                    } else {
                        Screen.Home.route
                    }

                    navController.navigate(destino) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // HOME
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = siloViewModel,
                usuario = usuarioActual,

                onSiloClick = { silo ->
                    navController.navigate(
                        Screen.SiloDetail.createRoute(silo.id)
                    )
                },

                onCameraClick = {
                    navController.navigate(Screen.CameraGallery.route)
                },

                onCerrarSesion = {
                    authViewModel.logout()

                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // ADMINISTRADOR
        // ADMINISTRADOR
        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(
                silos = silosList,

                onAgregarSilo = { nuevoSilo ->
                    siloViewModel.insertarSilo(nuevoSilo)
                },

                onCerrarSesion = {
                    authViewModel.logout()

                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // CÁMARA Y GALERÍA
        composable(Screen.CameraGallery.route) {
            CamaraGaleriaScreen()
        }

        // DETALLE DEL SILO
        composable(
            route = Screen.SiloDetail.route,
            arguments = listOf(
                navArgument("siloId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val siloId = backStackEntry.arguments?.getInt("siloId") ?: 0
            val silo = silosList.find { it.id == siloId }

            silo?.let { siloEncontrado ->
                SiloDetailScreen(
                    silo = siloEncontrado,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onRegistrarMovimiento = {
                        navController.navigate(
                            Screen.FormMovimiento.createRoute(siloEncontrado.id)
                        )
                    }
                )
            }
        }

        // FORMULARIO DE MOVIMIENTO
        composable(
            route = Screen.FormMovimiento.route,
            arguments = listOf(
                navArgument("siloId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val siloId = backStackEntry.arguments?.getInt("siloId") ?: 0
            val silo = silosList.find { it.id == siloId }

            silo?.let { siloEncontrado ->
                FormMovimientoScreen(
                    silo = siloEncontrado,
                    viewModel = formMovimientoViewModel,
                    onGuardarExitoso = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}