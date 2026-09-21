package com.example.ahorros_gc.data.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ahorros_gc.data.viewmodel.AccionistasViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


class RegistrarAcciones : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            loginregistar()
        }
    }

}

@Composable
fun loginregistar() {

    Surface(
        modifier = Modifier.fillMaxSize(), color = Color(0xFF4F1497)

    ) {
        Text(
            text = "Registrar Accionista",
            fontSize = 45.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 65.dp),
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier.fillMaxWidth(1f), contentAlignment = Alignment.Center
        ) {


            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                var context = LocalContext.current
                val viewModel: AccionistasViewModel = viewModel()
                var nombre by remember { mutableStateOf("") }
                var apellido by remember { mutableStateOf("") }
                var cedula by remember { mutableStateOf("") }
                var celular by remember { mutableStateOf("") }
                var correo by rememberSaveable { mutableStateOf("") }
                var direccion by remember { mutableStateOf("") }



                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    placeholder = { Text("Nombre") })
                Spacer(modifier = Modifier.size(10.dp))
                TextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    label = { Text("Apellido") },
                    placeholder = { Text("Apellido") })
                Spacer(modifier = Modifier.size(10.dp))

                TextField(
                    value = cedula,
                    onValueChange = { cedula = it },
                    label = { Text("Cedula") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),

                    )
                Spacer(modifier = Modifier.size(10.dp))
                TextField(
                    value = celular,
                    onValueChange = { celular = it },
                    label = { Text("Celular") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    placeholder = { Text("3000000000") })
                Spacer(modifier = Modifier.size(10.dp))
                TextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Gmail") },
                    placeholder = { Text("example@gmail.com") })
                Spacer(modifier = Modifier.size(10.dp))
                TextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Direccion") },
                    placeholder = { Text("Barrio N°Aparta...") })
                Spacer(modifier = Modifier.size(15.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        if (nombre.isBlank() || apellido.isBlank() || cedula.isBlank() || celular.isBlank() || correo.isBlank() || direccion.isBlank()) {
                            Toast.makeText(
                                context, "Todos los campos son obligatorios!!", Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            viewModel.buscarporcedulasviewmodel(cedula) { existe ->
                                if (existe) {
                                    Toast.makeText(
                                        context,
                                        "Esta cedula ya esta registrada",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    nombre = ""
                                    apellido = ""
                                    cedula = ""
                                    celular = ""
                                    correo = ""
                                    direccion = ""
                                } else {
                                    viewModel.registrarAccionista(
                                        nombre = nombre,
                                        apellido = apellido,
                                        cedula = cedula,
                                        celular = celular,
                                        gmail = correo,
                                        direccion = direccion
                                    )
                                    Toast.makeText(
                                        context, "accionista guardado con exito", Toast.LENGTH_SHORT
                                    ).show()
                                    nombre = ""
                                    apellido = ""
                                    cedula = ""
                                    celular = ""
                                    correo = ""
                                    direccion = ""
                                }
                            }

                        }


                    }) { Text("Registrar") }
                    Spacer(modifier = Modifier.size(10.dp))
                    Button(onClick = { regresar(context) }) { Text("Regresar") }
                }


            }
        }
    }

}

fun regresar(context: Context) {
    val intent = Intent(context, Menu::class.java)
    context.startActivity(intent)
    (context as? Activity)?.finish()

}
