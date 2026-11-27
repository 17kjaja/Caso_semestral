package com.example.reservasespacios.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MinimalDarkColorScheme = darkColorScheme(
    primary = Color(0xFF22C55E),           // Botones principales verdes
    onPrimary = Color(0xFF052E16),         // Texto oscuro legible sobre el botón verde
    primaryContainer = Color(0xFF14532D),  // Contenedores / badges verdes oscuros
    onPrimaryContainer = Color(0xFFDCFCE7),
    secondary = Color(0xFF4ADE80),         // Acento verde secundario
    onSecondary = Color(0xFF052E16),
    background = Color(0xFF0F172A),        // Fondo principal oscuro minimalista
    onBackground = Color(0xFFFFFFFF),      // Texto blanco sobre el fondo
    surface = Color(0xFF1E293B),           // Fondo de tarjetas (Cards) gris oscuro
    onSurface = Color(0xFFFFFFFF),         // Texto blanco dentro de las tarjetas
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFFCBD5E1)   // Subtítulos gris muy claro para jerarquía visual
)

@Composable
fun ReservasEspaciosTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MinimalDarkColorScheme,
        typography = Typography,
        content = content
    )
}