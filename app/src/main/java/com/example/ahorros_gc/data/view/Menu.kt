package com.example.ahorros_gc.data.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ahorros_gc.data.entity.Accionistas
import com.example.ahorros_gc.data.viewmodel.AccionistasViewModel

class Menu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            mymenu()

        }
    }
}


@Composable
fun mymenu() {
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF999898)) {
        var tabIndex by remember { mutableIntStateOf(0) }
        val tabs = listOf("Home", "Products", "Settings")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .absolutePadding(4.dp, 30.dp, 4.dp, 2.dp)
        ) {
            TabRow(
                selectedTabIndex = tabIndex,
                modifier = Modifier,
                contentColor = Color.Black,
                containerColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.apply {
                        Divider(
                            Modifier
                                .height(5.dp)
                                .padding(horizontal = 12.dp)
                                .tabIndicatorOffset(tabPositions[tabIndex]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                divider = {}) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        icon = {
                            when (index) {
                                0 -> Icon(
                                    imageVector = Icons.Default.Home, contentDescription = null
                                )

                                1 -> Icon(
                                    imageVector = Icons.Default.List, contentDescription = null
                                )

                                2 -> Icon(
                                    imageVector = Icons.Default.Settings, contentDescription = null
                                )
                            }
                        },
                        selectedContentColor = Color.Black
                    )
                }
            }

            when (tabIndex) {
                0 -> HomeScreen()
                1 -> ProductsScreen()
                2 -> SettingsScreen()
            }
        }
    }
}

@Composable
fun HomeScreen(viewModel: AccionistasViewModel = viewModel()) {

    val saldoCaja by viewModel.saldoCaja.collectAsState(initial = 0L)
    val saldoInteres by viewModel.saldoInteres.collectAsState(initial = 0L)
    val resumenDeAccionesCompradas by viewModel.resumenAccionistas.collectAsState()


    Column(
        Modifier.fillMaxSize(),
        //verticalArrangement = Arrangement.Center,
        //horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Caja", fontSize = 25.sp)
        Text(text = "$ $saldoCaja", fontSize = 30.sp)
        Spacer(modifier = Modifier.height((30.dp)))
        Text(text = "Gastos de Administrativos", fontSize = 25.sp)
        Text(text = "$ $saldoInteres", fontSize = 30.sp)

        Spacer(modifier = Modifier.height((35.dp)))
        if (resumenDeAccionesCompradas.isEmpty()) {
            Text(
                text = "No hay Accionistas", fontSize = 18.sp
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(resumenDeAccionesCompradas) { accionistas ->
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "${accionistas.nombre} ${accionistas.apellido}",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = "Cedula: ${accionistas.cedula}", fontSize = 12.sp)
                            Text(
                                text = "Cantidad Accion: ${accionistas.cantidadAcciones}",
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Total Accion: ${accionistas.valorTotal}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun ProductsScreen(viewModel: AccionistasViewModel = viewModel()) {
    val context = LocalContext.current
    val accionistas by viewModel.accionistasobtenidos.collectAsState()
    var mostrarModal by remember {
        mutableStateOf(false)
    }
    var accionistaSeleccionado by remember {
        mutableStateOf<Accionistas?>(null)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Text(
            text = "Accionistas", fontSize = 25.sp
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(accionistas) { accionista ->
                Row(
                    modifier = Modifier.fillMaxWidth(),

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${accionista.nombre} ${accionista.apellido}",
                        fontSize = 20.sp,
                        modifier = Modifier.width(260.dp)


                    )

                    Button(
                        onClick = {
                            accionistaSeleccionado = accionista
                            mostrarModal = true
                        }) {
                        Text("Comprar")
                    }


                    if (mostrarModal && accionistaSeleccionado != null) {

                        AlertDialog(
                            onDismissRequest = {
                                mostrarModal = false
                            },

                            title = {
                                Text("Comprar Acción")
                            },

                            text = {
                                Column {

                                    Text(
                                        text = "Accionista: ${accionistaSeleccionado!!.nombre}"
                                    )

                                    Spacer(
                                        modifier = Modifier.height(10.dp)
                                    )

                                    Text(
                                        text = "Valor + Interes de la acción: $6.000"
                                    )

                                }
                            },

                            confirmButton = {
                                Button(
                                    onClick = {
                                        accionistaSeleccionado?.let { accionista ->
                                            viewModel.comprarAccion(accionista)
                                        }
                                        mostrarModal = false
                                        accionistaSeleccionado = null
                                        Toast.makeText(
                                            context,
                                            "Compra realizada con exito",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }) {
                                    Text("Confirmar Compra")
                                }
                            },

                            dismissButton = {
                                Button(
                                    onClick = {
                                        mostrarModal = false
                                    }) {
                                    Text("Cancelar")
                                }
                            })
                    }

                }
            }
        }


    }
}

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text(
            text = "Settings",
            fontSize = 25.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 25.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Button(onClick = { registraAccionistas(context) }) { Text(text = "Registrar Accionistas") }
        Spacer(modifier = Modifier.size(10.dp))
        Button(onClick = { editarAccionistas(context) }) { Text(text = "Editar Accionistas") }
        Spacer(modifier = Modifier.size(10.dp))
        Button(onClick = {}) { Text(text = "Cambiar Contraseña") }
        Spacer(modifier = Modifier.size(10.dp))
        Button(onClick = {}) { Text(text = "Copias de seguridad") }
        Spacer(modifier = Modifier.size(10.dp))
        Button(onClick = {}) { Text(text = "Informacion de APP") }
        Spacer(modifier = Modifier.size(10.dp))
        Button(onClick = { MandarViewReporte(context) }) { Text(text = "Reportes") }
        Spacer(modifier = Modifier.size(10.dp))
        Button(onClick = { cerrarSesion(context) }) { Text(text = "Cerrar Sesion") }
    }
}


fun cerrarSesion(context: Context) {
    val intent = Intent(context, Inicioactivity::class.java)
    context.startActivity(intent)
    (context as? Activity)?.finish()

}

fun registraAccionistas(context: Context) {
    val intent = Intent(context, com.example.ahorros_gc.data.view.RegistrarAcciones::class.java)
    context.startActivity(intent)
    (context as? Activity)?.finish()
}

fun editarAccionistas(context: Context) {
    val intent = Intent(context, EditarAccionistas::class.java)
    context.startActivity(intent)

}

fun MandarViewReporte(context: Context) {
    val intent = Intent(context, Reportes::class.java)
    context.startActivity(intent)
}