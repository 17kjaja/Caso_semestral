package com.example.reservasespacios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.reservasespacios.ui.screens.CatalogoScreen
import com.example.reservasespacios.ui.theme.ReservasEspaciosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReservasEspaciosTheme {
                CatalogoScreen()
            }
        }
    }
}