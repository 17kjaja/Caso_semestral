package com.example.reservasespacios.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun CampoFormulario(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icono: ImageVector,
    error: String?,
    teclado: KeyboardType = KeyboardType.Text,
    unaLinea: Boolean = true
) {
    // Guardamos el último error para que el texto no desaparezca de golpe mientras se anima la salida
    var ultimoError by remember { mutableStateOf("") }
    if (error != null) ultimoError = error

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icono, contentDescription = null) },
        trailingIcon = {
            AnimatedVisibility(
                visible = error != null,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
        isError = error != null,
        supportingText = {
            AnimatedVisibility(
                visible = error != null,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Text(ultimoError)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        singleLine = unaLinea,
        modifier = Modifier.fillMaxWidth()
    )
}