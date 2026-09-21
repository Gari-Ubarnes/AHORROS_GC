package com.example.ahorros_gc.data.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlin.jvm.java


class Inicioactivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            loginUsuario()
        }

    }
}

@Composable
fun loginUsuario() {
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF4F1497)) {
        Box(
            modifier = Modifier.fillMaxWidth(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                var nombre by rememberSaveable { mutableStateOf("") }
                var pass by rememberSaveable { mutableStateOf("") }
                val context = LocalContext.current

                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    placeholder = { Text("Nombre") },

                    )
                Spacer(modifier = Modifier.height(40.dp))

                TextField(
                    value = pass,
                    onValueChange = { pass = it },
                    label = { Text("Contraseña") },
                    placeholder = { Text("contraseña") },
                    visualTransformation = PasswordVisualTransformation()


                )
                Spacer(modifier = Modifier.height(30.dp))
                Button(onClick = {
                    if (nombre.isNotEmpty() && pass.isNotEmpty()) {
                        val intent =
                            Intent(context, com.example.ahorros_gc.data.view.Menu::class.java)
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "campos vacioas", Toast.LENGTH_LONG).show()
                    }
                }, colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White,
                    containerColor = Color.Blue
                )) { Text("Entrar") }


            }

        }
    }

}

