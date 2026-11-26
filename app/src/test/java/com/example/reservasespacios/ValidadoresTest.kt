package com.example.reservasespacios

import com.example.reservasespacios.domain.validation.BloqueoValidator
import com.example.reservasespacios.domain.validation.EspacioValidator
import com.example.reservasespacios.domain.validation.LoginValidator
import com.example.reservasespacios.domain.validation.ReservaValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class ValidadoresTest {

    // --- LoginValidator ---
    @Test
    fun loginValidator_camposVaciosYValidos() {
        assertFalse(LoginValidator.validarUsuario("").ok)
        assertTrue(LoginValidator.validarUsuario("docente1").ok)

        assertFalse(LoginValidator.validarClave("").ok)
        assertFalse(LoginValidator.validarClave("123").ok)
        assertTrue(LoginValidator.validarClave("1234").ok)
    }

    // --- EspacioValidator ---
    @Test
    fun espacioValidator_nombreTipoDescripcionUbicacion() {
        assertFalse(EspacioValidator.validarNombre("").ok)
        assertFalse(EspacioValidator.validarNombre("AB").ok)
        assertTrue(EspacioValidator.validarNombre("Aula 101").ok)

        assertFalse(EspacioValidator.validarTipo(" ").ok)
        assertTrue(EspacioValidator.validarTipo("Laboratorio").ok)

        assertFalse(EspacioValidator.validarDescripcion("").ok)
        assertTrue(EspacioValidator.validarDescripcion("Equipado con proyector").ok)

        assertFalse(EspacioValidator.validarUbicacion("").ok)
        assertTrue(EspacioValidator.validarUbicacion("Piso 2, Edificio A").ok)
    }

    @Test
    fun espacioValidator_capacidadYHorarios() {
        assertFalse(EspacioValidator.validarCapacidad("").ok)
        assertFalse(EspacioValidator.validarCapacidad("abc").ok)
        assertFalse(EspacioValidator.validarCapacidad("0").ok)
        assertFalse(EspacioValidator.validarCapacidad("501").ok)
        assertTrue(EspacioValidator.validarCapacidad("40").ok)

        assertFalse(EspacioValidator.validarApertura("").ok)
        assertFalse(EspacioValidator.validarApertura("8:00").ok)
        assertTrue(EspacioValidator.validarApertura("08:00").ok)

        assertFalse(EspacioValidator.validarCierre("08:00", "").ok)
        assertFalse(EspacioValidator.validarCierre("08:00", "25:00").ok)
        assertFalse(EspacioValidator.validarCierre("10:00", "09:00").ok)
        assertTrue(EspacioValidator.validarCierre("08:00", "18:00").ok)
    }

    // --- BloqueoValidator ---
    @Test
    fun bloqueoValidator_espacioMotivoYFecha() {
        assertFalse(BloqueoValidator.validarEspacio(0).ok)
        assertTrue(BloqueoValidator.validarEspacio(1).ok)

        assertFalse(BloqueoValidator.validarMotivo("").ok)
        assertFalse(BloqueoValidator.validarMotivo("Mant").ok)
        assertTrue(BloqueoValidator.validarMotivo("Mantención eléctrica").ok)

        val hoy = LocalDate.of(2026, 11, 25)
        assertFalse(BloqueoValidator.validarFecha("", hoy).ok)
        assertFalse(BloqueoValidator.validarFecha("25-11-2026", hoy).ok)
        assertFalse(BloqueoValidator.validarFecha("2026-11-24", hoy).ok)
        assertTrue(BloqueoValidator.validarFecha("2026-11-26", hoy).ok)
    }

    // --- ReservaValidator ---
    @Test
    fun reservaValidator_actividadCursoAsistentesJustificacion() {
        assertFalse(ReservaValidator.validarActividad("").ok)
        assertFalse(ReservaValidator.validarActividad("Cl").ok)
        assertTrue(ReservaValidator.validarActividad("Clase práctica").ok)

        assertFalse(ReservaValidator.validarCurso("").ok)
        assertTrue(ReservaValidator.validarCurso("TI-401").ok)

        assertFalse(ReservaValidator.validarAsistentes("", 30).ok)
        assertFalse(ReservaValidator.validarAsistentes("diez", 30).ok)
        assertFalse(ReservaValidator.validarAsistentes("0", 30).ok)
        assertFalse(ReservaValidator.validarAsistentes("31", 30).ok)
        assertTrue(ReservaValidator.validarAsistentes("25", 30).ok)

        assertFalse(ReservaValidator.validarJustificacion("").ok)
        assertFalse(ReservaValidator.validarJustificacion("No").ok)
        assertTrue(ReservaValidator.validarJustificacion("Aula en reparación").ok)
    }

    @Test
    fun reservaValidator_fechaYProximoDiaHabil() {
        val viernes = LocalDate.of(2026, 11, 27)
        assertEquals(LocalDate.of(2026, 11, 30), ReservaValidator.proximoDiaHabil(viernes))

        assertFalse(ReservaValidator.validarFecha("", viernes).ok)
        assertFalse(ReservaValidator.validarFecha("formato-malo", viernes).ok)
        assertFalse(ReservaValidator.validarFecha("2026-11-28", viernes).ok) // Sábado
        assertFalse(ReservaValidator.validarFecha("2026-11-27", viernes).ok) // Sin anticipación
        assertTrue(ReservaValidator.validarFecha("2026-11-30", viernes).ok)  // Lunes válido
    }

    @Test
    fun reservaValidator_parseHoraYHorarios() {
        assertNull(ReservaValidator.parseHora("9:00"))
        assertNull(ReservaValidator.parseHora("25:99"))
        assertNotNull(ReservaValidator.parseHora("09:00"))

        assertFalse(ReservaValidator.validarHoraInicio("", "08:00", "20:00").ok)
        assertFalse(ReservaValidator.validarHoraInicio("9:00", "08:00", "20:00").ok)
        assertFalse(ReservaValidator.validarHoraInicio("07:30", "08:00", "20:00").ok)
        assertFalse(ReservaValidator.validarHoraInicio("20:00", "08:00", "20:00").ok)
        assertTrue(ReservaValidator.validarHoraInicio("09:00", "08:00", "20:00").ok)

        assertFalse(ReservaValidator.validarHoraFin("09:00", "", "20:00").ok)
        assertFalse(ReservaValidator.validarHoraFin("09:00", "10:0", "20:00").ok)
        assertFalse(ReservaValidator.validarHoraFin("09:00", "09:00", "20:00").ok)
        assertFalse(ReservaValidator.validarHoraFin("09:00", "20:30", "20:00").ok)
        assertTrue(ReservaValidator.validarHoraFin("09:00", "11:00", "20:00").ok)
    }
}