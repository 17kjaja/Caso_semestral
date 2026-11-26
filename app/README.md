# ReservasEspacios — Aplicación Móvil de Gestión de Espacios Académicos

Aplicación móvil nativa desarrollada en **Kotlin** y **Jetpack Compose** bajo arquitectura **MVVM**, conectada a un ecosistema de **3 microservicios Spring Boot** y la API externa **Nager.Date** para validación de feriados nacionales en Chile, manteniendo persistencia local con **Room (SQLite)** como respaldo *offline-first*.

---

## 1. Arquitectura y Tecnologías

- **Frontend Móvil:** Kotlin, Jetpack Compose (Material 3), Navigation Compose, ViewModel & StateFlow.
- **Persistencia Local:** Room (`2.8.5`) con soporte offline y sincronización automática.
- **Capa de Red:** Retrofit (`2.11.0`) + Gson + OkHttp Logging Interceptor.
- **Backend (Microservicios Spring Boot - Java 21 / Maven):**
    - `servicio-usuarios` (`http://10.0.2.2:8081`): Autenticación y gestión de perfiles (`ADMIN`, `DOCENTE`).
    - `servicio-espacios` (`http://10.0.2.2:8082`): Catálogo de espacios, disponibilidad y bloqueos administrativos.
    - `servicio-reservas` (`http://10.0.2.2:8083`): Creación, validación de traslapes, aprobación/rechazo y notificaciones.
- **API Externa:** [Nager.Date API](https://date.nager.at/api/v3/PublicHolidays/2026/CL) para bloqueo preventivo de reservas en días feriados de Chile.
- **Testing & Calidad:** JUnit 4, MockK (`1.13.13`), `kotlinx-coroutines-test` (`1.9.0`) con cobertura >80% en lógica de negocio (`domain.validation`) y repositorios (`data.repository`).

---

## 2. Instrucciones de Ejecución

### Levantar los Microservicios
1. Asegurar **Java 21** y **Apache Maven 3.9+** configurados en el sistema.
2. Ejecutar el script `iniciar-servicios.bat` dentro de la carpeta `microservicios/`.
3. Verificar disponibilidad en los puertos locales `8081`, `8082` y `8083`.

### Ejecutar la Aplicación Android
1. Abrir el proyecto en **Android Studio** y ejecutar **Sync Project with Gradle Files**.
2. Iniciar un emulador Android (API 26+) y ejecutar el módulo `:app`.
3. **Credenciales de prueba:**
    - Administrador: `admin` / `1234`
    - Docente: `docente` / `1234`

---

## 3. Pruebas Unitarias y Firma Digital (Release)

- **Ejecución de Pruebas:** `ValidadoresTest.kt` y `RepositoriosMockTest.kt` ejecutados mediante `./gradlew testDebugUnitTest` y *Run with Coverage* en Android Studio.
- **APK Firmado:** Generado mediante *Build > Generate Signed APK* (`app/release/app-release.apk`) con esquema **APK Signature Scheme v2** (`CN=benjamin`, RSA 2048-bit), verificado mediante `apksigner verify`.