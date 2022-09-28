package com.example.sorteo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AlertDialog.Builder
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.launch

class GanadoresActivity : AppCompatActivity() {
    private lateinit var adapter: GanadorAdapter
    private var listaGanadores = ArrayList<Persona>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ganadores)
        val app = Room.databaseBuilder(applicationContext, PersonaDb::class.java, "personas").build()
        val ganadores = liveData<List<Persona>> {
            var gan = app.personaDao().getAll()
            emit(gan)
        }

        ganadores.observe(this, Observer { value ->
            value.forEach {
                Log.d("Ganador", it.ganador.toString())
                if(it.ganador) {
                    listaGanadores.add(it)
                }
            }
            initRecyclerView(listaGanadores)
        })
    }

    private fun initRecyclerView(ganadores: List<Persona>) {
        adapter = GanadorAdapter(ganadores)
        val recyclerView = findViewById<RecyclerView>(R.id.ganadores_rv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_ganadores_lista, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.eliminar_ganadores -> {
                var builder = AlertDialog.Builder(this)

                builder.setTitle("¡Atencion!")
                    .setMessage("¿Esta seguro que desea eleminar lista de ganadores?")
                    .setPositiveButton("SI") { dialogInterface, it ->
                        val app = Room.databaseBuilder(applicationContext, PersonaDb::class.java, "personas").build()
                        lifecycleScope.launch {
                            app.personaDao().setearFalse(gan = false)
                        }
                        listaGanadores.clear()

                        adapter.notifyDataSetChanged()
                    }
                    .setNegativeButton("No") { dialogInterface, it ->
                        dialogInterface.cancel()
                    }
                    .show()
            }

        }
        return super.onOptionsItemSelected(item)
    }
}