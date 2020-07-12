package com.example.taxiexpress2.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize //No sé que rayos hace esto
class Alfil(val uid: String,
            val activo: Boolean,
            val numero: String,
            val nombre: String,
            val apellido_paterno: String,
            val apellido_materno: String,
            val placas: String,
            val licencia: String,
            val email: String,
            val foto: String,
            //Datos fuera de registro
            val calificacion: Float,
            val acumulado: Float,
            val viajes_completados: Short,
            val viajes_rechazados: Byte,
            val retardos: Byte,
            val faltas: Byte,
            val penalizaciones_monto: Float): Parcelable {
    constructor(): this(
        "",
        true,
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        0.0f,
        0.0f,
        0,
        0,
        0,
        0,
        0.0f)
}