package com.example.ahorros_gc.data.view

import android.R.attr.text
import android.content.Context
import android.icu.text.CaseMap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ahorros_gc.data.viewmodel.AccionistasViewModel
import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.ahorros_gc.data.entity.Accionistas

class EditarAccionistas : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EditarAccionista()


        }
    }

}


@Composable
fun EditarAccionista(
    viewModel: AccionistasViewModel = viewModel()
) {
    var mostrarConfirmacionEliminar by remember {
        mutableStateOf(false)
    }
    val accionistas by viewModel.accionistasobtenidos.collectAsState()

    // Controla si el modal está abierto
    var mostrarModal by remember {
        mutableStateOf(false)
    }

    // Accionista seleccionado
    var accionistaSeleccionado by remember {
        mutableStateOf<Accionistas?>(null)
    }

    Surface(
        modifier = Modifier.fillMaxSize(), color = Color(0xFFF5F7FA)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp, end = 16.dp, top = 60.dp, bottom = 16.dp
                )
        ) {

            Text(
                text = "ACCIONISTAS",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A5F),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                items(accionistas) { accionista ->

                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {

                                // Guardamos el accionista seleccionado
                                accionistaSeleccionado = accionista

                                // Abrimos el modal
                                mostrarModal = true
                            },

                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {

                            Text(
                                text = "${accionista.nombre} ${accionista.apellido}",
                                fontSize = 21.sp,
                                color = Color(0xFF1E3A5F),
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Cédula: ${accionista.cedula}", fontSize = 16.sp
                            )

                            Text(
                                text = "Celular: ${accionista.celular}", fontSize = 16.sp
                            )

                            Text(
                                text = "Correo: ${accionista.gmail}", fontSize = 16.sp
                            )

                            Text(
                                text = "Dirección: ${accionista.direccion}", fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }

    // ============================
    // MODAL PARA EDITAR
    // ============================
    var context = LocalContext.current
    if (mostrarModal && accionistaSeleccionado != null) {

        var nombre by remember {
            mutableStateOf(accionistaSeleccionado!!.nombre)
        }

        var apellido by remember {
            mutableStateOf(accionistaSeleccionado!!.apellido)
        }

        var cedula by remember {
            mutableStateOf(accionistaSeleccionado!!.cedula)
        }

        var celular by remember {
            mutableStateOf(accionistaSeleccionado!!.celular)
        }

        var gmail by remember {
            mutableStateOf(accionistaSeleccionado!!.gmail)
        }

        var direccion by remember {
            mutableStateOf(accionistaSeleccionado!!.direccion)
        }

        AlertDialog(

            onDismissRequest = {
                mostrarModal = false
            },

            title = {
                Text("Editar accionista")
            },

            text = {

                Column {

                    OutlinedTextField(value = nombre, onValueChange = {
                        nombre = it
                    }, label = {
                        Text("Nombre")
                    })

                    OutlinedTextField(value = apellido, onValueChange = {
                        apellido = it
                    }, label = {
                        Text("Apellido")
                    })

                    OutlinedTextField(
                        value = cedula,
                        onValueChange = {},
                        label = { Text("Cédula") },
                        readOnly = true
                    )

                    OutlinedTextField(value = celular, onValueChange = {
                        celular = it
                    }, label = {
                        Text("Celular")
                    })

                    OutlinedTextField(value = gmail, onValueChange = {
                        gmail = it
                    }, label = {
                        Text("Correo")
                    })

                    OutlinedTextField(value = direccion, onValueChange = {
                        direccion = it
                    }, label = {
                        Text("Dirección")
                    })
                }
            },

            confirmButton = {

                Button(

                    onClick = {


                        if (nombre.isBlank() || apellido.isBlank() || celular.isBlank() || gmail.isBlank() || direccion.isBlank()) {
                            Toast.makeText(context, "campos obligatorios", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            val accionistaActualizado = Accionistas(
                                cedula = accionistaSeleccionado!!.cedula,

                                nombre = nombre,
                                apellido = apellido,
                                celular = celular,
                                gmail = gmail,
                                direccion = direccion
                            )

                            viewModel.ActualizarAccionistas(accionistaActualizado)

                            mostrarModal = false
                        }

                    }

                ) {
                    Text("Guardar")
                }
            },

            dismissButton = {
                if (mostrarConfirmacionEliminar && accionistaSeleccionado != null) {

                    AlertDialog(onDismissRequest = {
                        mostrarConfirmacionEliminar = false
                    }, title = {
                        Text("Confirmar eliminación")
                    }, text = {
                        Text(
                            "¿Está seguro de eliminar a " + "${accionistaSeleccionado!!.nombre} " + "${accionistaSeleccionado!!.apellido}?"
                        )
                    }, confirmButton = {
                        Button(
                            onClick = {
                                viewModel.EliminarAccionistaViewModel(
                                    accionistaSeleccionado!!.cedula
                                )

                                mostrarConfirmacionEliminar = false
                                mostrarModal = false
                                accionistaSeleccionado = null
                                Toast.makeText(
                                    context,
                                    "Se elimino Con EXITO!! ${nombre} ${apellido}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }, colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.White, containerColor = Color.Red
                            )
                        ) {
                            Text("Sí, eliminar")
                        }
                    }, dismissButton = {
                        TextButton(
                            onClick = {
                                mostrarConfirmacionEliminar = false
                            }, colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.White,
                                containerColor = Color.Green
                            )

                        ) {
                            Text("Cancelar")
                        }
                    })
                }
                TextButton(
                    onClick = {
                        mostrarConfirmacionEliminar = true


                    }, colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.White, containerColor = Color.Red
                    )
                ) { Text("Eliminar") }


                TextButton(

                    onClick = {
                        mostrarModal = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.White,
                        containerColor = Color.Gray
                    )

                ) {
                    Text("Cancelar")
                }
            })
    }


}/*viewModel.EliminarAccionistaViewModel(accionistaSeleccionado!!.cedula)
mostrarModal=false*/
