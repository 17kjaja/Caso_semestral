package com.example.reservasespacios.data.remote

/**
 * URLs base de los microservicios propios y de la API externa.
 *
 * Emulador Android: 10.0.2.2 = el localhost del PC.
 * Celular físico: reemplaza HOST por la IP local del PC (ipconfig -> IPv4), mismo WiFi,
 * y permite los puertos 8081-8083 en el Firewall de Windows.
 */
object ApiConfig {
    private const val HOST = "10.0.2.2"

    const val URL_USUARIOS = "http://$HOST:8081/"
    const val URL_ESPACIOS = "http://$HOST:8082/"
    const val URL_RESERVAS = "http://$HOST:8083/"

    // API externa pública (sin API key): feriados de Chile
    const val URL_FERIADOS = "https://date.nager.at/"
}
