package com.example.reservasespacios.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        EspacioEntity::class,
        UsuarioEntity::class,
        ReservaEntity::class,
        BloqueoEntity::class,
        NotificacionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun espacioDao(): EspacioDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun reservaDao(): ReservaDao
    abstract fun bloqueoDao(): BloqueoDao
    abstract fun notificacionDao(): NotificacionDao

    companion object {
        @Volatile
        private var instancia: AppDatabase? = null

        fun obtener(context: Context): AppDatabase {
            return instancia ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reservas.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            cargarDatosFicticios(db)
                        }
                    })
                    .build().also { instancia = it }
            }
        }

        private fun cargarDatosFicticios(db: SupportSQLiteDatabase) {
            // Usuarios ficticios (clave de prueba: 1234)
            db.execSQL("INSERT INTO usuarios (nombre, usuario, clave, perfil) VALUES ('Inspector Demo', 'inspector', '1234', 'ADMIN')")
            db.execSQL("INSERT INTO usuarios (nombre, usuario, clave, perfil) VALUES ('Docente Demo 1', 'docente1', '1234', 'SOLICITANTE')")
            db.execSQL("INSERT INTO usuarios (nombre, usuario, clave, perfil) VALUES ('UTP Demo', 'utp1', '1234', 'SOLICITANTE')")

            // Espacios ficticios
            db.execSQL("INSERT INTO espacios (nombre, tipo, descripcion, ubicacion, capacidadMax, horaApertura, horaCierre, estado) VALUES ('Laboratorio de Computación', 'Laboratorio', 'Sala con 30 computadores', 'Primer piso', 30, '08:00', '17:00', 'DISPONIBLE')")
            db.execSQL("INSERT INTO espacios (nombre, tipo, descripcion, ubicacion, capacidadMax, horaApertura, horaCierre, estado) VALUES ('Laboratorio de Ciencias', 'Laboratorio', 'Sala con mesones y materiales', 'Primer piso', 25, '08:00', '17:00', 'DISPONIBLE')")
            db.execSQL("INSERT INTO espacios (nombre, tipo, descripcion, ubicacion, capacidadMax, horaApertura, horaCierre, estado) VALUES ('Biblioteca', 'Biblioteca', 'Sala de lectura y consulta', 'Segundo piso', 40, '08:00', '17:00', 'DISPONIBLE')")
            db.execSQL("INSERT INTO espacios (nombre, tipo, descripcion, ubicacion, capacidadMax, horaApertura, horaCierre, estado) VALUES ('Sala Sensorial', 'Apoyo', 'Espacio de apoyo PIE', 'Primer piso', 8, '08:00', '16:00', 'DISPONIBLE')")
            db.execSQL("INSERT INTO espacios (nombre, tipo, descripcion, ubicacion, capacidadMax, horaApertura, horaCierre, estado) VALUES ('Centro de Innovación CIEMP', 'Tecnológico', 'Espacio de innovación y robótica', 'Segundo piso', 20, '08:00', '17:00', 'DISPONIBLE')")
            db.execSQL("INSERT INTO espacios (nombre, tipo, descripcion, ubicacion, capacidadMax, horaApertura, horaCierre, estado) VALUES ('Sala de Reuniones', 'Reuniones', 'Sala para reuniones y capacitaciones', 'Segundo piso', 15, '08:00', '18:00', 'DISPONIBLE')")
            db.execSQL("INSERT INTO espacios (nombre, tipo, descripcion, ubicacion, capacidadMax, horaApertura, horaCierre, estado) VALUES ('Huerto Escolar', 'Recreativo', 'Huerto al aire libre', 'Patio trasero', 30, '09:00', '16:00', 'DISPONIBLE')")
        }
    }
}