package com.example.ahorros_gc.data.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ahorros_gc.data.database.AppDatabase
import com.example.ahorros_gc.data.entity.Acciones
import com.example.ahorros_gc.data.entity.Accionistas
import com.example.ahorros_gc.data.entity.Caja
import com.example.ahorros_gc.data.entity.InteresIng
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.emptyList
import com.example.ahorros_gc.data.dto.ResumenAccionista
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AccionistasViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).accionistasDao()
    private val accionDao = AppDatabase.getDatabase(application).accionDao()
    private val cajaDao = AppDatabase.getDatabase(application).cajaDao()

    private val interesdao = AppDatabase.getDatabase(application).interesingDao()

    val accionistasobtenidos = dao.obtenerAccionistas().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun registrarAccionista(
        nombre: String,
        apellido: String,
        cedula: String,
        celular: String,
        gmail: String,
        direccion: String,

        ) {
        viewModelScope.launch {

            val cantidadAccionista = dao.contarAccionistasParaValidar()
            if (cantidadAccionista >= 20) {
                Toast.makeText(
                    getApplication<Application>(), "Solo se pueden registrar 20 accionistas",
                    Toast.LENGTH_SHORT
                ).show()

                return@launch
            } else {
                val accionistas = Accionistas(
                    nombre = nombre,
                    apellido = apellido,
                    cedula = cedula,
                    celular = celular,
                    gmail = gmail,
                    direccion = direccion
                )
                dao.insertarAccionista(accionistas)
            }


        }
    }

    fun ActualizarAccionistas(accionistas: Accionistas) {
        viewModelScope.launch {
            dao.actualizarAccionistas(accionistas)
        }
    }

    fun buscarporcedulasviewmodel(cedula: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val accionista = dao.buscarXcedula(cedula)
            callback(accionista != null)
        }
    }

    fun EliminarAccionistaViewModel(cedula: String) {
        viewModelScope.launch {
            // Eliminar movimientos de caja
            cajaDao.eliminarCajaPorCedula(cedula)

            // Eliminar movimientos de intereses
            interesdao.eliminarInteresesPorCedula(cedula)

            // Eliminar accionista
            dao.eliminarAccionista(cedula)

            //eliminar accion
            accionDao.EliminarAccionXcedula(cedula)

        }
    }

    fun comprarAccion(accionistas: Accionistas) {

        viewModelScope.launch {
            val cantidadAccion = accionDao.contarAccionXcedula(accionistas.cedula)
            //val fechaCompra=accionDao.obtenerfechaDeLaCompraAccion(Acciones.fechacompra)
            if (cantidadAccion >= 5) {

                Toast.makeText(getApplication<Application>(),"${accionistas.nombre} Solo puedes comprar 5 acciones",
                    Toast.LENGTH_SHORT).show()
                return@launch
            } else {
                val fechaCompra = SimpleDateFormat(
                    "dd/MM/yyyy HH:mm:ss",
                    Locale.getDefault()
                ).format(Date())
                val accion = Acciones(
                    cedulaAccionista = accionistas.cedula,
                    codigoCuenta = "1205",
                    debito = 0,
                    credito = 5000,
                    fechaCompra = fechaCompra
                )
                accionDao.insertarAccionDao(accion)

                val caja = Caja(
                    cedulaAccionista = accionistas.cedula,
                    codigoCuenta = "1105",
                    debito = 6000,
                    credito = 0,
                    fechaCompra = fechaCompra
                )
                cajaDao.insertarCajaDao(caja)

                val interes = InteresIng(
                    cedulaAccionista = accionistas.cedula,
                    codigoCuenta = "415020",
                    debito = 0,
                    credito = 1000,
                    fechaCompra = fechaCompra
                )
                interesdao.insertarInteresIng(interes)

            }

        }


    }

    val saldoCaja = cajaDao.obtenersaldoCaja()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0L
        )
    val saldoInteres = interesdao.obtenerSaldoInteres()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1000),
            0L
        )

    val resumenAccionistas = accionDao.obtenerAccionistasConAcciones()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    //un no esta funcionando
    fun BuscarCajaXcedula(cedula: String, callback: (List<Caja>) -> Unit) {
        viewModelScope.launch {
            val resultado = cajaDao.obtenerCajaXcedula(cedula)
            callback(resultado)
        }

    }

    fun BuscarAccionistaXcedula(cedula: String, callback: (Accionistas?) -> Unit) {
        viewModelScope.launch {
            val accionista = dao.buscarXcedula(cedula)
            callback(accionista)
        }
    }

    fun BuscarAccionXcedulaVM(cedula: String, callback: (List<Acciones>) -> Unit) {
        viewModelScope.launch {
            val accionista = accionDao.obtenerAccionXcedula(cedula)
            callback(accionista)
        }
    }

}

