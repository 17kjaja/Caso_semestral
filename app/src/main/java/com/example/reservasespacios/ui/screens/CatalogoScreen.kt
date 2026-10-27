package com.example.reservasespacios.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reservasespacios.data.local.EspacioEntity
import com.example.reservasespacios.ui.components.EstadoChip
import com.example.reservasespacios.viewmodel.EspacioViewModel
import com.example.reservasespacios.viewmodel.NotificacionesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    onReservar: (Int) -> Unit,
    onMisSolicitudes: () -> Unit,
    onNotificaciones: () -> Unit,
    onCerrarSesion: () -> Unit,
    viewModel: EspacioViewModel = viewModel(),
    notificacionesViewModel: NotificacionesViewModel = viewModel()
) {
    val espacios by viewModel.espacios.collectAsState()
    val noLeidas by notificacionesViewModel.noLeidas.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Espacios disponibles") },
                actions = {
                    IconButton(onClick = onNotificaciones) {
                        BadgedBox(
                            badge = {
                                if (noLeidas > 0) {
                                    Badge { Text(noLeidas.toString()) }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
                        }
                    }
                    IconButton(onClick = onMisSolicitudes) {
                        Icon(Icons.Default.History, contentDescription = "Mis solicitudes")
                    }
                    IconButton(onClick = onCerrarSesion) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(espacios) { espacio ->
                EspacioCard(espacio, onReservar = { onReservar(espacio.id) })
            }
        }
    }
}

@Composable
fun EspacioCard(espacio: EspacioEntity, onReservar: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = espacio.nombre, style = MaterialTheme.typography.titleMedium)
                EstadoChip(espacio.estado)
            }
            Text(text = espacio.descripcion, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "${espacio.ubicacion} · Capacidad: ${espacio.capacidadMax}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Horario: ${espacio.horaApertura} a ${espacio.horaCierre}",
                style = MaterialTheme.typography.bodySmall
            )
            Button(
                onClick = onReservar,
                enabled = espacio.estado == "DISPONIBLE",
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Solicitar")
            }
        }
    }
}