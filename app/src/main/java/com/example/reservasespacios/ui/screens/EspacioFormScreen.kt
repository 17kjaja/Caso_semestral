package com.example.reservasespacios.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reservasespacios.ui.components.CampoFormulario
import com.example.reservasespacios.viewmodel.EspacioFormViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EspacioFormScreen(
    espacioId: Int,
    onVolver: () -> Unit,
    viewModel: EspacioFormViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(espacioId) { viewModel.cargar(espacioId) }

    LaunchedEffect(estado.guardadoExitoso) {
        if (estado.guardadoExitoso) {
            Toast.makeText(context, "Espacio guardado", Toast.LENGTH_SHORT).show()
            onVolver()
        }
    }

    val opcionesEstado = listOf(
        "DISPONIBLE" to "Disponible",
        "MANTENIMIENTO" to "Mantenimiento",
        "FUERA_DE_SERVICIO" to "Fuera de servicio"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (espacioId == 0) "Nuevo espacio" else "Editar espacio") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CampoFormulario(
                value = estado.nombre,
                onValueChange = viewModel::onNombreChange,
                label = "Nombre",
                icono = Icons.Default.Edit,
                error = estado.nombreError
            )
            CampoFormulario(
                value = estado.tipo,
                onValueChange = viewModel::onTipoChange,
                label = "Tipo (ej. Laboratorio)",
                icono = Icons.Default.Category,
                error = estado.tipoError
            )
            CampoFormulario(
                value = estado.descripcion,
                onValueChange = viewModel::onDescripcionChange,
                label = "Descripción",
                icono = Icons.Default.Description,
                error = estado.descripcionError,
                unaLinea = false
            )
            CampoFormulario(
                value = estado.ubicacion,
                onValueChange = viewModel::onUbicacionChange,
                label = "Ubicación",
                icono = Icons.Default.Place,
                error = estado.ubicacionError
            )
            CampoFormulario(
                value = estado.capacidad,
                onValueChange = viewModel::onCapacidadChange,
                label = "Capacidad máxima",
                icono = Icons.Default.Group,
                error = estado.capacidadError,
                teclado = KeyboardType.Number
            )
            CampoFormulario(
                value = estado.horaApertura,
                onValueChange = viewModel::onAperturaChange,
                label = "Hora de apertura (HH:mm)",
                icono = Icons.Default.Schedule,
                error = estado.aperturaError
            )
            CampoFormulario(
                value = estado.horaCierre,
                onValueChange = viewModel::onCierreChange,
                label = "Hora de cierre (HH:mm)",
                icono = Icons.Default.Schedule,
                error = estado.cierreError
            )

            Text("Estado", style = MaterialTheme.typography.titleSmall)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                opcionesEstado.forEach { (valor, etiqueta) ->
                    FilterChip(
                        selected = estado.estado == valor,
                        onClick = { viewModel.onEstadoChange(valor) },
                        label = { Text(etiqueta) }
                    )
                }
            }

            Button(
                onClick = viewModel::guardar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar espacio")
            }
        }
    }
}