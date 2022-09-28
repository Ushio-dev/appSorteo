package com.example.sorteo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import androidx.room.Room
import kotlinx.coroutines.launch

class SorteoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sorteo)

        val app = Room.databaseBuilder(applicationContext, PersonaDb::class.java, "personas").build()

        var ganadorTv = findViewById<TextView>(R.id.ganadorTv)
        var emailGanadorTv = findViewById<TextView>(R.id.mailGanadorTv)
        var generarBtn = findViewById<Button>(R.id.ganadorBtn)
        var imagenGandor = findViewById<ImageView>(R.id.ganador_img)
        var buscandoGanador = findViewById<TextView>(R.id.buscandoGanador)

        generarBtn.setOnClickListener {
            var total: LiveData<Int> = liveData {
                var valor = app.personaDao().countPeople()
                emit(valor)
            }
            total.observe(this, Observer<Int> {value ->
                buscandoGanador.visibility = View.VISIBLE
                if (value > 0) {
                    lifecycleScope.launch {
                        if (ganadorTv.isVisible) {
                            ganadorTv.visibility = View.INVISIBLE
                            emailGanadorTv.visibility = View.INVISIBLE
                            imagenGandor.visibility = View.INVISIBLE
                            buscandoGanador.visibility = View.VISIBLE
                        }
                        Thread.sleep(1000)
                        var min = app.personaDao().traerPrimero()
                        Log.d("GANADOR ID", min.toString())
                        var max = app.personaDao().traerUltimo()
                        Log.d("INDICE MIN", min.toString())
                        Log.d("INDICE MAX: ", max.toString())
                        var indice = (min..max).random()

                        var ganador = app.personaDao().findById(indice)

                        Thread.sleep(1000)

                        ganadorTv.text = ganador.nombre
                        emailGanadorTv.text = ganador.email
                        app.personaDao().updateGanador(true, indice)

                        buscandoGanador.visibility = View.INVISIBLE
                        ganadorTv.visibility = View.VISIBLE
                        emailGanadorTv.visibility = View.VISIBLE
                        imagenGandor.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(this, "No hay participantes cargados", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_ganadores, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ir_aganadores -> {
                val intent = Intent(this, GanadoresActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

