package com.example.ahorros_gc.data.database

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.ahorros_gc.data.dao.AccionDao
import com.example.ahorros_gc.data.dao.AccionistasDao
import com.example.ahorros_gc.data.dao.CajaDao
import com.example.ahorros_gc.data.dao.InteresIngDao
import com.example.ahorros_gc.data.entity.Acciones
import com.example.ahorros_gc.data.entity.Accionistas
import com.example.ahorros_gc.data.entity.Caja
import com.example.ahorros_gc.data.entity.InteresIng

@Database(
    entities = [Accionistas::class, Acciones::class, Caja::class, InteresIng::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase:RoomDatabase(){
    abstract fun accionistasDao(): AccionistasDao
    abstract fun accionDao(): AccionDao
    abstract fun cajaDao(): CajaDao
    abstract fun interesingDao(): InteresIngDao



    companion object{
        @Volatile
        private var INSTANCE : AppDatabase?=null
        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE?:synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ahorros_database"
                ).build()
                INSTANCE=instance
                instance
            }
        }
    }
}
