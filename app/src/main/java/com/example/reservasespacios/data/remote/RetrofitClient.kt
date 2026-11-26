package com.example.reservasespacios.data.remote

import com.example.reservasespacios.data.remote.api.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/** Crea una instancia de Retrofit por cada base URL (3 microservicios + 1 API externa). */
object RetrofitClient {

    private val http: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(8, TimeUnit.SECONDS)
            .readTimeout(8, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()
    }

    private fun crear(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(http)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val retrofitUsuarios by lazy { crear(ApiConfig.URL_USUARIOS) }
    private val retrofitEspacios by lazy { crear(ApiConfig.URL_ESPACIOS) }
    private val retrofitReservas by lazy { crear(ApiConfig.URL_RESERVAS) }
    private val retrofitFeriados by lazy { crear(ApiConfig.URL_FERIADOS) }

    val usuarioApi: UsuarioApi by lazy { retrofitUsuarios.create(UsuarioApi::class.java) }
    val espacioApi: EspacioApi by lazy { retrofitEspacios.create(EspacioApi::class.java) }
    val bloqueoApi: BloqueoApi by lazy { retrofitEspacios.create(BloqueoApi::class.java) }
    val reservaApi: ReservaApi by lazy { retrofitReservas.create(ReservaApi::class.java) }
    val notificacionApi: NotificacionApi by lazy { retrofitReservas.create(NotificacionApi::class.java) }
    val feriadosApi: FeriadosApi by lazy { retrofitFeriados.create(FeriadosApi::class.java) }
}
