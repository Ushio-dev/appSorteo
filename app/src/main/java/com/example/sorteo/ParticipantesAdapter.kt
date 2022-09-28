package com.example.sorteo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ParticipantesAdapter(private val participantes: List<Persona>) : RecyclerView.Adapter<ParticipanteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipanteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ParticipanteViewHolder(layoutInflater.inflate(R.layout.item_persona, parent, false))
    }

    override fun onBindViewHolder(holder: ParticipanteViewHolder, position: Int) {
        var item = participantes[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return participantes.size
    }
}