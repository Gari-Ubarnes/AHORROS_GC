package com.example.ahorros_gc.data.dao


import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.ahorros_gc.data.entity.Accionistas
import kotlinx.coroutines.flow.Flow


@Dao
interface AccionistasDao {
    @Insert
    suspend fun insertarAccionista(accionista: Accionistas)

    @Query("SELECT * FROM accionistas")
    fun obtenerAccionistas(): Flow<List<Accionistas>>

    @Update
    suspend fun actualizarAccionistas(accionista: Accionistas)

    @Query("SELECT * FROM accionistas WHERE cedula = :cedula LIMIT 1")
    suspend fun buscarXcedula(cedula: String): Accionistas?

    @Query("DELETE FROM accionistas WHERE cedula = :cedula")
    suspend fun eliminarAccionista(cedula: String)



}