package com.example.ahorros_gc.data.view


import android.content.ContentValues
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ahorros_gc.data.entity.Acciones
import com.example.ahorros_gc.data.entity.Accionistas
import com.example.ahorros_gc.data.viewmodel.AccionistasViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Reportes : ComponentActivity() {
    private val viewModel: AccionistasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            reportes()
        }

    }

    @Composable
    fun reportes() {

        var mostrarModal by remember { mutableStateOf(false) }
        var cedula by remember { mutableStateOf("") }

        val context = LocalContext.current

        Surface(
            modifier = Modifier.fillMaxSize(), color = Color(0xFF4F1497)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 65.dp),
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    text = "Generar Reportes",
                    fontSize = 45.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier = Modifier.height(50.dp)
                )

                Text(
                    text = "Reporte por accionista",
                    fontSize = 20.sp,
                    color = Color.White,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .clickable {
                            mostrarModal = true
                        })
            }

            // MODAL
            if (mostrarModal) {

                AlertDialog(

                    onDismissRequest = {
                        mostrarModal = false
                    },

                    title = {
                        Text("Reporte por accionista")
                    },

                    text = {

                        TextField(
                            value = cedula, onValueChange = {
                            cedula = it
                        }, label = {
                            Text("Cédula del accionista")
                        }, keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                        )
                    },

                    confirmButton = {

                        Button(
                            onClick = {

                                generarReporte(cedula)
                                mostrarModal = false
                                cedula = ""

                            }) {
                            Text("Generar reporte")
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

    fun generarReporte(cedula: String) {

        viewModel.BuscarAccionistaXcedula(cedula) { accionista ->

            if (accionista == null) {
                Toast.makeText(
                    this, "No existe un accionista con esa cédula", Toast.LENGTH_SHORT
                ).show()
                return@BuscarAccionistaXcedula
            }

            viewModel.BuscarAccionXcedulaVM(cedula) { registrosAccion ->

                val cantidadAcciones = registrosAccion.size

                val valorTotal = registrosAccion.sumOf { accion ->
                    accion.credito
                }

                val fechaHora = SimpleDateFormat(
                    "dd/MM/yyyy HH:mm:ss", Locale.getDefault()
                ).format(Date())

                crearPDF(

                    accionista = accionista,
                    cantidadAcciones = cantidadAcciones,
                    valorTotal = valorTotal,
                    fechaHora = fechaHora,
                    registrosAccion = registrosAccion
                )
            }
        }
    }

    fun crearPDF(
        accionista: Accionistas,
        cantidadAcciones: Int,
        valorTotal: Long,
        fechaHora: String,
        registrosAccion: List<Acciones>
    ) {

        val documentoPDF = PdfDocument()
        val pintarPdf = Paint()

        pintarPdf.textSize = 20f
        pintarPdf.isFakeBoldText = true

        val paginaInformacion = PdfDocument.PageInfo.Builder(
            595, 842, 1
        ).create()

        val pagina = documentoPDF.startPage(paginaInformacion)

        val canvas = pagina.canvas

        // TÍTULO
        canvas.drawText(
            "REPORTE DE ACCIONISTA", 40f, 50f, pintarPdf
        )

        pintarPdf.textSize = 14f
        pintarPdf.isFakeBoldText = false

        // FECHA Y HORA DEL REPORTE
        canvas.drawText(
            "Fecha y hora del reporte: $fechaHora", 40f, 85f, pintarPdf
        )

        // DATOS DEL ACCIONISTA
        pintarPdf.isFakeBoldText = true

        canvas.drawText(
            "DATOS DEL ACCIONISTA", 40f, 130f, pintarPdf
        )

        pintarPdf.isFakeBoldText = false

        canvas.drawText(
            "Cédula: ${accionista.cedula}", 40f, 160f, pintarPdf
        )

        canvas.drawText(
            "Nombre: ${accionista.nombre} ${accionista.apellido}", 40f, 190f, pintarPdf
        )

        canvas.drawText(
            "Celular: ${accionista.celular}", 40f, 220f, pintarPdf
        )

        canvas.drawText(
            "Correo: ${accionista.gmail}", 40f, 250f, pintarPdf
        )

        // INFORMACIÓN DE ACCIONES
        pintarPdf.isFakeBoldText = true

        canvas.drawText(
            "INFORMACIÓN DE ACCIONES", 40f, 300f, pintarPdf
        )

        pintarPdf.isFakeBoldText = false

        canvas.drawText(
            "Cantidad de acciones: $cantidadAcciones", 40f, 330f, pintarPdf
        )

        canvas.drawText(
            "Valor total: $$valorTotal", 40f, 360f, pintarPdf
        )

        /*/ DETALLE DE CAJA
        pintarPdf.isFakeBoldText = true

        canvas.drawText(
            "DETALLE DE MOVIMIENTOS", 40f, 410f, pintarPdf
        )

        pintarPdf.isFakeBoldText = false

        var posicionY = 440f

        for (caja in registrosCaja) {
            canvas.drawText(
                " ${caja.fechaCompra}", 40f, posicionY, pintarPdf
            )

            canvas.drawText(
                "Cuenta: ${caja.codigoCuenta}", 190f, posicionY, pintarPdf
            )

            canvas.drawText(
                "Débito: $${caja.debito}", 300f, posicionY, pintarPdf
            )

            canvas.drawText(
                "Crédito: $${caja.credito}", 420f, posicionY, pintarPdf
            )

            posicionY += 30f
        }
*/
        documentoPDF.finishPage(pagina)

        val nombreArchivo = "reporte_${accionista.nombre}.pdf"
        val valores = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, nombreArchivo)
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS
            )
        }
        val uri = contentResolver.insert(
            MediaStore.Files.getContentUri("external"), valores
        )


        try {
            if (uri != null) {
                contentResolver.openOutputStream(uri)
                    ?.use { outputStream -> documentoPDF.writeTo(outputStream) }
            }
            documentoPDF.close()

            Toast.makeText(
                this, "PDF generado con éxito", Toast.LENGTH_SHORT
            ).show()

            if (uri != null) {
                enviarPDFxCorreo(
                    accionista.gmail, uri
                )
            }

        } catch (e: Exception) {

            documentoPDF.close()

            Toast.makeText(
                this, "ERROR: ${e.message}", Toast.LENGTH_LONG
            ).show()
        }
    }
    fun enviarPDFxCorreo(correo: String, uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(correo))
            putExtra(
                Intent.EXTRA_SUBJECT, "Reporte de accionista"
            )
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(
            Intent.createChooser(intent, "Enviar reporte por")
        )
    }
}