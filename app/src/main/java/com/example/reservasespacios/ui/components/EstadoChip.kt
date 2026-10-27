package com.example.reservasespacios.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EstadoChip(estado: String) {
    val (fondoObjetivo, textoObjetivo) = when (estado) {
        "PENDIENTE", "MANTENIMIENTO" ->
            MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        "ACEPTADA", "DISPONIBLE" ->
            MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        "RECHAZADA", "FUERA_DE_SERVICIO" ->
            MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
        else ->
            MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }

    val fondo by animateColorAsState(
        targetValue = fondoObjetivo,
        animationSpec = tween(durationMillis = 500),
        label = "fondoChip"
    )
    val texto by animateColorAsState(
        targetValue = textoObjetivo,
        animationSpec = tween(durationMillis = 500),
        label = "textoChip"
    )

    Surface(color = fondo, shape = RoundedCornerShape(50)) {
        Text(
            text = estado.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() },
            color = texto,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}