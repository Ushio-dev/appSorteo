package com.example.sorteo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import androidx.room.Room
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val app = Room.databaseBuilder(applicationContext, PersonaDb::class.java, "personas").build()
        var nombreEd = findViewById<EditText>(R.id.nombreEd)
        var telefonoEd = findViewById<EditText>(R.id.telefonoEd)
        var emailEd = findViewById<EditText>(R.id.emailEd)
        var registrarBtn = findViewById<Button>(R.id.registrarBtn)

        var personas = ArrayList<Persona>()
        registrarBtn.setOnClickListener {
            
            if(!nombreEd.text.isEmpty() && !telefonoEd.text.isEmpty() && !emailEd.text.isEmpty()) {
                var builder = AlertDialog.Builder(this)

                builder.setTitle("Atencion!")
                    .setMessage("Confirme que sus datos esten correctos:\nNombre: ${nombreEd.text.toString()}\nTelefono: ${telefonoEd.text.toString()}\nEmail: ${emailEd.text.toString()}")
                    .setCancelable(true)
                    .setPositiveButton("Aceptar") {dialogInterface, it ->
                        val persona = Persona(nombre = nombreEd.text.toString(), telefono = telefonoEd.text.toString().toLong(), email = emailEd.text.toString(), ganador = false)
                        lifecycleScope.launch {
                            app.personaDao().insert(persona)
                        }
                        Toast.makeText(this, "Registrado", Toast.LENGTH_SHORT).show()
                        nombreEd.editableText.clear()
                        telefonoEd.editableText.clear()
                        emailEd.editableText.clear()
                    }
                    .setNegativeButton("Cancelar") { dialogInterface, it ->
                        dialogInterface.cancel()
                    }
                    .show()

            } else {
                Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show()
            }

            var inputmethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputmethodManager.hideSoftInputFromWindow(emailEd.windowToken,0)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_irparticipantes ->{
                val intent = Intent(this, ParticipantesActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_irSorteo -> {
                val intent = Intent(this, SorteoActivity::class.java)

                startActivity(intent)
            }
            R.id.ganadores_lista -> {
                val intent = Intent(this, GanadoresActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}