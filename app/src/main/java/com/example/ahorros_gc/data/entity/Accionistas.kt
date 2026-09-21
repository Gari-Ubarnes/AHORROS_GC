package com.example.ahorros_gc.data.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName="accionistas")
data class Accionistas(

    @PrimaryKey
    val cedula : String,

    val nombre: String,
    val apellido: String,
    val celular: String,
    val gmail: String,
    val direccion: String

)
