package com.example.sorteo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class GanadorAdapter(val ganadores: List<Persona>): RecyclerView.Adapter<GanadorViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GanadorViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return GanadorViewHolder(layoutInflater.inflate(R.layout.item_persona, parent, false))
    }

    override fun onBindViewHolder(holder: GanadorViewHolder, position: Int) {
        var item = ganadores[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return ganadores.size
    }
}