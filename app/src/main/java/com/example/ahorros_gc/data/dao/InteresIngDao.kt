package com.example.ahorros_gc.data.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.example.ahorros_gc.data.entity.InteresIng
import kotlinx.coroutines.flow.Flow

@Dao
interface InteresIngDao {
    @Insert
    suspend fun insertarInteresIng(interesing: InteresIng)

    @Query("SELECT * FROM interesing")
    fun obtenerInteres(): Flow<List<InteresIng>>

    @Query("SELECT COALESCE(SUM(credito), 0) - COALESCE(SUM(debito), 0) FROM interesing")
    fun obtenerSaldoInteres(): Flow<Long>

    @Query("DELETE FROM interesing WHERE cedulaAccionista = :cedula")
    suspend fun eliminarInteresesPorCedula(cedula: String)
}