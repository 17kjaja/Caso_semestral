package com.example.reservasespacios

import com.example.reservasespacios.data.local.*
import com.example.reservasespacios.data.remote.api.*
import com.example.reservasespacios.data.remote.dto.*
import com.example.reservasespacios.data.repository.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RepositoriosMockTest {

    @Test
    fun usuarioRepository_loginExitosoYFallbackOffline() = runTest {
        val dao = mockk<UsuarioDao>(relaxed = true)
        val api = mockk<UsuarioApi>()
        val repo = UsuarioRepository(dao, api)

        coEvery { api.login(LoginRequest("admin", "1234")) } returns
                UsuarioDto(id = 1, nombre = "Administrador", usuario = "admin", perfil = "ADMIN")

        val user = repo.iniciarSesion("admin", "1234")
        assertNotNull(user)
        assertEquals("ADMIN", user?.perfil)

        // Simula caída de red -> fallback a Room
        coEvery { api.login(any()) } throws RuntimeException("Sin red")
        coEvery { dao.iniciarSesion("docente", "1234") } returns
                UsuarioEntity(2, "Docente", "docente", "1234", "DOCENTE")

        val offlineUser = repo.iniciarSesion("docente", "1234")
        assertEquals("DOCENTE", offlineUser?.perfil)
    }

    @Test
    fun espacioRepository_sincronizarYCrud() = runTest {
        val dao = mockk<EspacioDao>(relaxed = true)
        val api = mockk<EspacioApi>()
        val repo = EspacioRepository(dao, api)

        val dto = EspacioDto(1, "Lab Redes", "LAB", "Desc", "Piso 1", 30, "08:00", "18:00", "DISPONIBLE")
        coEvery { api.listar() } returns listOf(dto)
        coEvery { dao.obtenerPorId(1) } returns null

        repo.sincronizar()
        coVerify { dao.insertar(any()) }

        coEvery { api.obtener(1) } returns dto
        val obtenido = repo.obtenerPorId(1)
        assertEquals("Lab Redes", obtenido?.nombre)

        coEvery { api.crear(any()) } returns dto
        repo.insertar(EspacioEntity(0, "Lab Redes", "LAB", "Desc", "Piso 1", 30, "08:00", "18:00", "DISPONIBLE"))
        coVerify(atLeast = 2) { dao.insertar(any()) }
    }

    @Test
    fun reservaRepository_traslapeCrearResolverYCancelar() = runTest {
        val dao = mockk<ReservaDao>(relaxed = true)
        val api = mockk<ReservaApi>()
        val repo = ReservaRepository(dao, api)

        coEvery { api.hayTraslape(1, "2026-12-01", "09:00", "11:00") } returns true
        assertTrue(repo.hayTraslape(1, "2026-12-01", "09:00", "11:00"))

        val dto = ReservaDto(
            id = 10, espacioId = 1, usuarioId = 2, espacioNombre = "Sala 1",
            solicitanteNombre = "Docente", fecha = "2026-12-01",
            horaInicio = "09:00", horaFin = "11:00", actividad = "Clase",
            cursoGrupo = "A1", asistentes = 20, estado = "PENDIENTE"
        )
        coEvery { api.crear(any()) } returns dto
        repo.crear(
            ReservaEntity(0, 1, 2, "2026-12-01", "09:00", "11:00", "Clase", "A1", 20, "", "2026-11-25")
        )
        coVerify { dao.insertar(any()) }

        coEvery { api.resolver(10, any()) } returns dto.copy(estado = "ACEPTADA")
        repo.resolver(10, "ACEPTADA", "OK", 1, "2026-11-26")
        coVerify { dao.resolver(10, "ACEPTADA", "OK", 1, "2026-11-26") }

        coEvery { api.cancelar(10) } returns dto.copy(estado = "CANCELADA")
        repo.cancelar(10)
        coVerify { dao.cancelar(10) }
    }

    @Test
    fun bloqueoYNotificacionRepository_operaciones() = runTest {
        val bDao = mockk<BloqueoDao>(relaxed = true)
        val bApi = mockk<BloqueoApi>()
        val bRepo = BloqueoRepository(bDao, bApi)

        coEvery { bApi.hayTraslape(1, "2026-12-02", "10:00", "12:00") } returns false
        assertFalse(bRepo.hayBloqueo(1, "2026-12-02", "10:00", "12:00"))

        val nDao = mockk<NotificacionDao>(relaxed = true)
        val nApi = mockk<NotificacionApi>()
        val nRepo = NotificacionRepository(nDao, nApi)

        coEvery { nApi.marcarTodasLeidas(2) } returns Unit
        nRepo.marcarTodasLeidas(2)
        coVerify { nDao.marcarTodasLeidas(2) }
    }

    @Test
    fun feriadosRepository_detectaFeriadoChile() = runTest {
        val api = mockk<FeriadosApi>()
        val repo = FeriadosRepository(api)

        coEvery { api.feriados(2026) } returns listOf(
            FeriadoDto(date = "2026-12-25", localName = "Navidad", name = "Christmas Day")
        )

        val navidad = repo.feriadoDe("2026-12-25")
        assertNotNull(navidad)
        assertEquals("Navidad", navidad?.localName)

        val diaNormal = repo.feriadoDe("2026-12-26")
        assertNull(diaNormal)
    }
}