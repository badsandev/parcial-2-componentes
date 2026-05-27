package com.taller.parcial2.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.taller.parcial2.ui.theme.detail.DetailScreen
import com.taller.parcial2.ui.theme.goals.GoalsScreen

// ─── Paleta Dark Theme ───────────────────────────────────────────────────────
object AppColors {
    val Background      = Color(0xFF0F0E1A)   // fondo casi negro azulado
    val Surface         = Color(0xFF1A1930)   // tarjetas oscuras
    val SurfaceVariant  = Color(0xFF252340)   // input / chip
    val Primary         = Color(0xFF7C4DFF)   // violeta principal
    val PrimaryVariant  = Color(0xFF651FFF)   // violeta más intenso
    val Accent          = Color(0xFFB388FF)   // lavanda para etiquetas
    val OnBackground    = Color(0xFFEDE7FF)   // texto principal
    val OnSurface       = Color(0xFFD1C4E9)   // texto secundario
    val OnSurfaceSubtle = Color(0xFF9E8FC0)   // texto terciario / hints
    val Success         = Color(0xFF00E5A0)   // verde neón (montos ahorrados)
    val Danger          = Color(0xFFFF5252)   // rojo suave
    val ProgressTrack   = Color(0xFF2E2B4A)   // track de barra de progreso
    val GradientStart   = Color(0xFF7C4DFF)
    val GradientEnd     = Color(0xFF4A90E2)
}

// Rutas de navegación
object Routes {
    const val GOALS = "goals"
    const val DETAIL = "detail/{goalId}"
    fun detail(goalId: String) = "detail/$goalId"
}

// Tema oscuro de la app
@Composable
fun SavingsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background          = AppColors.Background,
            surface             = AppColors.Surface,
            surfaceVariant      = AppColors.SurfaceVariant,
            primary             = AppColors.Primary,
            onPrimary           = Color.White,
            secondary           = AppColors.Accent,
            onSecondary         = AppColors.Background,
            tertiary            = AppColors.Success,
            onBackground        = AppColors.OnBackground,
            onSurface           = AppColors.OnSurface,
            onSurfaceVariant    = AppColors.OnSurfaceSubtle,
            error               = AppColors.Danger,
        ),
        content = content
    )
}

// Navegación principal de la app
@Composable
fun SavingsApp() {
    val navController = rememberNavController()

    SavingsTheme {
        NavHost(navController = navController, startDestination = Routes.GOALS) {

            composable(Routes.GOALS) {
                GoalsScreen(
                    onGoalClick = { goalId -> navController.navigate(Routes.detail(goalId)) }
                )
            }

            composable(
                route = Routes.DETAIL,
                arguments = listOf(navArgument("goalId") { type = NavType.StringType })
            ) { backStackEntry ->
                val goalId = backStackEntry.arguments?.getString("goalId") ?: return@composable
                DetailScreen(
                    goalId = goalId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
