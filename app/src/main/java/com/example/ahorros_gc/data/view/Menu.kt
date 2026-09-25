package com.example.ahorros_gc.data.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ahorros_gc.data.entity.Accionistas
import com.example.ahorros_gc.data.ui.theme.BottomActive
import com.example.ahorros_gc.data.ui.theme.ChipBlue
import com.example.ahorros_gc.data.ui.theme.ChipBlueText
import com.example.ahorros_gc.data.ui.theme.ChipGray
import com.example.ahorros_gc.data.ui.theme.TextDark
import com.example.ahorros_gc.data.ui.theme.TextGray
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
    var pantallaActual by remember {
        mutableStateOf("inicio")
    }
    Surface(
        modifier = Modifier.fillMaxSize(), color = Color(0xFFF7F7FB)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            when (pantallaActual) {
                "inicio" -> {
                    HomeScreen(
                        modifier = Modifier.weight(1f)
                    )

                }

                "perfil" -> {
                    ProductsScreen(
                        viewModel = viewModel(), modifier = Modifier.weight(1f)
                    )
                }

            }


            BottomBar(
                pantallaActual = pantallaActual, cambiarPantalla = { pantalla ->
                    pantallaActual = pantalla
                })
        }
    }
}

@Composable
fun BottomItem(
    label: String, icon: ImageVector, active: Boolean, onClick: () -> Unit

) {
    val color = if (active) {
        Color(0xFF6C63FF)
    } else {
        Color.Gray
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .clickable {
            onClick()
        }
        .padding(horizontal = 12.dp)) {
        Icon(
            imageVector = icon, contentDescription = label, tint = color
        )

        Text(
            text = label, fontSize = 12.sp, color = color
        )
    }
}

@Composable
fun BottomBar(
    pantallaActual: String, cambiarPantalla: (String) -> Unit
) {

    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(
                top = 10.dp, bottom = 10.dp
            ),

        horizontalArrangement = Arrangement.SpaceAround,

        verticalAlignment = Alignment.CenterVertically
    ) {

        BottomItem(
            label = "Inicio",
            icon = Icons.Default.Home,
            active = pantallaActual == "inicio",
            onClick = {
                cambiarPantalla("inicio")
            })

        BottomItem(
            label = "Socios", icon = Icons.Default.Person, active = false, onClick = {
                editarAccionistas(context)
            })

        // BOTÓN +

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(BottomActive),

            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar",
                tint = Color.White,
                modifier = Modifier.clickable {
                    registraAccionistas(context)
                })
        }

        BottomItem(
            label = "Reportes", icon = Icons.Default.BarChart, active = false, onClick = {
                MandarViewReporte(context)
            })

        BottomItem(
            label = "Perfil",
            icon = Icons.Default.Person,
            active = pantallaActual == "perfil",
            onClick = {
                cambiarPantalla("perfil")
            })
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, viewModel: AccionistasViewModel = viewModel()
) {

    val saldoCaja by viewModel.saldoCaja.collectAsState(initial = 0L)
    val saldoInteres by viewModel.saldoInteres.collectAsState(initial = 0L)

    val resumenDeAccionesCompradas by viewModel.resumenAccionistas.collectAsState()
    val porcentaje = (resumenDeAccionesCompradas.size * 100) / 100
    val totalAccionesCompradas = resumenDeAccionesCompradas.sumOf { it.cantidadAcciones }
    val porcentajeTotalAccionesC = (totalAccionesCompradas * 100) / 100
    LazyColumn(
        modifier = modifier.fillMaxSize(),

        contentPadding = PaddingValues(
            start = 20.dp, end = 20.dp, top = 55.dp, bottom = 20.dp
        ),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {

            Column {

                Text(
                    text = "Hola 👋", fontSize = 15.sp, color = TextGray
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Panel principal",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "Resumen de tus accionistas", fontSize = 14.sp, color = TextGray
                )
            }
        }

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                CardBox(
                    modifier = Modifier.weight(1f),
                    bg = Color(0xFFEDE7F6),
                    title = "Accionistas",
                    value = resumenDeAccionesCompradas.size.toString(),
                    badge = "Registrados $porcentaje%",
                    badgeBg = Color(0xFFDFF5E7)
                )

                CardBox(
                    modifier = Modifier.weight(1f),
                    bg = Color(0xFFE3F2FD),
                    title = "Acciones",
                    value = "$totalAccionesCompradas",
                    badge = "Compradas $porcentajeTotalAccionesC%",
                    badgeBg = Color(0xFFDFF5E7)
                )
            }
        }

        item {

            CardBox(
                modifier = Modifier.fillMaxWidth(),
                bg = Color(0xFFFFF3E0),
                title = "Valor total",
                value = "$ $saldoCaja",
                badge = "Capital acumulado",
                badgeBg = Color(0xFFFFE0B2)
            )
        }

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Accionistas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Text(
                    text = "Ver todos",
                    fontSize = 13.sp,
                    color = BottomActive,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        items(resumenDeAccionesCompradas) { accionista ->

            SocioCard(
                socio = Socio(
                    name = "${accionista.nombre} ${accionista.apellido}",
                    initials = ("${accionista.nombre.firstOrNull() ?: ""}" + "${accionista.apellido.firstOrNull() ?: ""}").uppercase(),
                    cedula = accionista.cedula,
                    acciones = accionista.cantidadAcciones.toString(),
                    total = "$ ${accionista.valorTotal}",
                    color = Color(0xFF6C63FF)
                )
            )
        }
    }

}


data class Socio(
    val name: String,
    val initials: String,
    val cedula: String,
    val acciones: String,
    val total: String,
    val color: Color
)

@Composable
fun CardBox(
    modifier: Modifier, bg: Color, title: String, value: String, badge: String, badgeBg: Color
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        Color.White.copy(alpha = 0.6f)
                    ),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "💼", fontSize = 14.sp
                )
            }

            Spacer(
                modifier = Modifier.width(6.dp)
            )

            Text(
                text = title, color = TextGray, fontSize = 13.sp
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = value, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, color = TextDark
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(badgeBg)
                .padding(
                    horizontal = 8.dp, vertical = 4.dp
                )
        ) {

            Text(
                text = badge,
                fontSize = 11.sp,
                color = Color(0xFF22A559),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SocioCard(
    socio: Socio
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(14.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(
                            socio.color, socio.color.copy(alpha = 0.6f)
                        )
                    )
                ),

            contentAlignment = Alignment.Center
        ) {

            Text(
                text = socio.initials, color = Color.White, fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = socio.name,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 1,
                color = TextDark
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            ChipInfo(
                text = "Cédula: ${socio.cedula}"
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                ChipInfo(
                    text = "Acciones: ${socio.acciones}"
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(ChipBlue)
                        .padding(
                            horizontal = 8.dp, vertical = 4.dp
                        )
                ) {

                    Text(
                        text = "Total: ${socio.total}",
                        fontSize = 11.sp,
                        color = ChipBlueText,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ChipInfo(
    text: String
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(ChipGray)
            .padding(
                horizontal = 8.dp, vertical = 4.dp
            )
    ) {

        Text(
            text = text, fontSize = 11.sp, color = TextDark
        )
    }
}/*
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
*/

@Composable
fun SocioPorComprarCard(
    socioXcomprar: SocioPorComprar, onComprar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(socioXcomprar.color),
            contentAlignment = Alignment.Center

        ) {
            Text(
                text = socioXcomprar.iniciales, color = Color.White, fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "${socioXcomprar.socioNombre} ${socioXcomprar.socioApellido}",
            fontSize = 20.sp,
            modifier = Modifier.weight(1f)
        )
        Button(onClick = onComprar) {
            Text(text = "Comprar")
        }
    }
}

data class SocioPorComprar(
    val socioNombre: String, val socioApellido: String, val iniciales: String, val color: Color
)

@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    viewModel: AccionistasViewModel = viewModel(),
) {
    val context = LocalContext.current

    val accionistas by viewModel.accionistasobtenidos.collectAsState()

    var mostrarModal by remember {
        mutableStateOf(false)
    }
    var accionistaSeleccionado by remember {
        mutableStateOf<Accionistas?>(null)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Text(
            text = "Accionistas", fontSize = 25.sp
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            items(accionistas) { accionista ->
                SocioPorComprarCard(
                    socioXcomprar = SocioPorComprar(
                        socioNombre = accionista.nombre,
                        socioApellido = accionista.apellido,
                        iniciales = ("${accionista.nombre.firstOrNull() ?: ""}" + "${accionista.apellido.firstOrNull() ?: ""}").uppercase(),
                        color = Color(0xFF6C63FF)
                    ), onComprar = {
                        accionistaSeleccionado = accionista
                        mostrarModal = true
                    })
            }
        }
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
                            context, "Compra realizada con exito", Toast.LENGTH_SHORT
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