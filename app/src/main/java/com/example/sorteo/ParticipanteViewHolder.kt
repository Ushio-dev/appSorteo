package com.example.sorteo

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ParticipanteViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val nombreParticipante = view.findViewById<TextView>(R.id.nombreparticipantetv)
    val mailParticipante = view.findViewById<TextView>(R.id.mailparticipantetv)
    val telefonoParticipante = view.findViewById<TextView>(R.id.telefonoparticipantetv)

    fun render(persona: Persona) {
        nombreParticipante.text = persona.nombre
        mailParticipante.text = persona.email
        telefonoParticipante.text = persona.telefono.toString()
    }
}