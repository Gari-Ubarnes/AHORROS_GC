package com.example.ahorros_gc.data.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName="acciones")
data class Acciones(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val cedulaAccionista: String,
    val codigoCuenta: String="1205",
    val debito: Long,
    val credito: Long,
    val fechaCompra: String
)
