package com.example.ahorros_gc.data.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.example.ahorros_gc.data.entity.Caja
import kotlinx.coroutines.flow.Flow

@Dao
interface CajaDao {
    @Insert
    suspend fun insertarCajaDao(caja: Caja)

    @Query("SELECT * FROM caja")
    fun obtenerCaja(): Flow<List<Caja>>

    @Query("SELECT COALESCE(SUM(debito),0)- COALESCE(SUM(credito),0) FROM caja")
    fun obtenersaldoCaja(): Flow<Long>

    @Query("DELETE FROM caja WHERE cedulaAccionista = :cedula")
    suspend fun eliminarCajaPorCedula(cedula: String)

    @Query("SELECT * FROM caja WHERE cedulaAccionista=:cedula")
    suspend fun obtenerCajaXcedula(cedula: String): List<Caja>
}