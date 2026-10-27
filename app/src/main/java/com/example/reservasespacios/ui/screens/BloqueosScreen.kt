package com.example.reservasespacios.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reservasespacios.ui.components.CampoFormulario
import com.example.reservasespacios.viewmodel.BloqueosViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BloqueosScreen(
    onVolver: () -> Unit,
    viewModel: BloqueosViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsState()
    val espacios by viewModel.espacios.collectAsState()
    val bloqueos by viewModel.bloqueos.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(estado.mensajeExito) {
        estado.mensajeExito?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.consumirMensaje()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bloquear horarios") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Espacio", style = MaterialTheme.typography.titleSmall)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        espacios.forEach { espacio ->
                            FilterChip(
                                selected = estado.espacioId == espacio.id,
                                onClick = { viewModel.onEspacioChange(espacio.id) },
                                label = { Text(espacio.nombre) }
                            )
                        }
                    }
                    estado.espacioError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                    CampoFormulario(
                        value = estado.fecha,
                        onValueChange = viewModel::onFechaChange,
                        label = "Fecha (AAAA-MM-DD)",
                        icono = Icons.Default.Event,
                        error = estado.fechaError
                    )
                    CampoFormulario(
                        value = estado.horaInicio,
                        onValueChange = viewModel::onHoraInicioChange,
                        label = "Hora de inicio (HH:mm)",
                        icono = Icons.Default.Schedule,
                        error = estado.horaInicioError
                    )
                    CampoFormulario(
                        value = estado.horaFin,
                        onValueChange = viewModel::onHoraFinChange,
                        label = "Hora de término (HH:mm)",
                        icono = Icons.Default.Schedule,
                        error = estado.horaFinError
                    )
                    CampoFormulario(
                        value = estado.motivo,
                        onValueChange = viewModel::onMotivoChange,
                        label = "Motivo (ej. mantenimiento)",
                        icono = Icons.Default.Edit,
                        error = estado.motivoError
                    )
                    estado.errorGeneral?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                    Button(
                        onClick = viewModel::guardar,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Bloquear horario")
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Bloqueos registrados", style = MaterialTheme.typography.titleMedium)
                    if (bloqueos.isEmpty()) {
                        Text("No hay bloqueos registrados", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            items(bloqueos) { item ->
                val b = item.bloqueo
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.espacioNombre, style = MaterialTheme.typography.titleSmall)
                            Text("${b.fecha} · ${b.horaInicio} a ${b.horaFin}", style = MaterialTheme.typography.bodyMedium)
                            Text("Motivo: ${b.motivo}", style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { viewModel.eliminar(b.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar bloqueo")
                        }
                    }
                }
            }
        }
    }
}