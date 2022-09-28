package com.example.sorteo

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.launch
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class ParticipantesActivity : AppCompatActivity() {

    private var listaPersona = ArrayList<Persona>()
    private lateinit var adapter: ParticipantesAdapter
    var REQUEST_CODE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_participantes)
        val app = Room.databaseBuilder(applicationContext, PersonaDb::class.java, "personas").build()
        var participantes = liveData<List<Persona>> {
            var personas = app.personaDao().getAll()
            emit(personas)
        }
        participantes.observe(this, Observer { value ->
            value.forEach {
                Log.d("INDICE", it.id.toString())
            }
            listaPersona.clear()
            listaPersona.addAll(value)

            initRecyclerView(listaPersona)
        })
    }

    fun initRecyclerView(participantes: List<Persona>) {
        adapter = ParticipantesAdapter(participantes)
        val recyclerView = findViewById<RecyclerView>(R.id.participantes_rv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_participantes, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_eliminar_paritipantes -> {

                // crear alert dialog antes de eleiminar
                var builder = AlertDialog.Builder(this)

                builder.setTitle("¡Atencion!")
                    .setMessage("¿Estas seguro que desea eliminar todos los elementos?")
                    .setCancelable(true)
                    .setPositiveButton("Si") { dialogInterface, it ->
                        val app = Room.databaseBuilder(applicationContext, PersonaDb::class.java, "personas")
                            .build()
                        lifecycleScope.launch {
                            app.personaDao().deleteAll()

                        }
                        listaPersona.clear()
                        adapter.notifyDataSetChanged()
                        //finish() esto cierra la acivity
                    }
                    .setNegativeButton("No") { dialogInterface, it ->
                        dialogInterface.cancel()
                    }
                    .show()
            }
            R.id.crear_excel -> {

                var permiso = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if(permiso == PackageManager.PERMISSION_GRANTED) {
                    generarExcel()
                    Toast.makeText(this, "Se creo excel", Toast.LENGTH_SHORT).show()
                } else {
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun generarExcel() {
        lifecycleScope.launch {
            var workbook = XSSFWorkbook()
            var sheet = workbook.createSheet("hoja1")
            var row = sheet.createRow(0)
            var cell = row.createCell(0)
            cell.setCellValue("NOMBRE")

            //row = sheet.createRow(0)
            cell = row.createCell(1)
            cell.setCellValue("TELEFONO")

            //row = sheet.createRow(0)
            cell = row.createCell(2)
            cell.setCellValue("EMAIL")

            var fila = 0
            while (fila < listaPersona.size) {
                row = sheet.createRow(fila+1)
                cell = row.createCell(0)
                cell.setCellValue(listaPersona.get(fila).nombre)


                cell = row.createCell(1)
                cell.setCellValue(listaPersona.get(fila).telefono.toString())


                cell = row.createCell(2)
                cell.setCellValue(listaPersona.get(fila).email)

                fila++

            }
            sheet.setColumnWidth(0, 10000)
            sheet.setColumnWidth(1, 7000)
            sheet.setColumnWidth(2,7000)
            var file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "participantes.xlsx")

            try {
                var outputStream = FileOutputStream(file)
                workbook.write(outputStream)
                Log.d("NO EXITE ARCHIVO", "creado")
            } catch (e: FileNotFoundException) {
                Log.d("ERROR", e.message.toString())
            }

        }

    }

    override fun onDestroy() {
        Log.d("status", "Me voy")
        super.onDestroy()

    }
}