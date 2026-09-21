package com.example.ahorros_gc.data.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.example.ahorros_gc.data.dto.ResumenAccionista
import com.example.ahorros_gc.data.entity.Acciones
import kotlinx.coroutines.flow.Flow

@Dao
interface AccionDao {
    @Insert
    suspend fun insertarAccionDao(acciones: Acciones)

    @Query(
        """
    SELECT 
        accionistas.nombre AS nombre,
        accionistas.apellido AS apellido,
        accionistas.cedula AS cedula,
        COUNT(acciones.id) AS cantidadAcciones,
        SUM(acciones.credito) AS valorTotal
    FROM accionistas
    INNER JOIN acciones
        ON accionistas.cedula = acciones.cedulaAccionista
    GROUP BY 
        accionistas.cedula,
        accionistas.nombre,
        accionistas.apellido
"""
    )
    fun obtenerAccionistasConAcciones(): Flow<List<ResumenAccionista>>


    @Query("SELECT * FROM acciones WHERE cedulaAccionista = :cedula")
    suspend fun obtenerAccionXcedula(cedula: String): List<Acciones>

}