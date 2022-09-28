package com.example.sorteo

import android.app.Application
import androidx.room.Room

class PersonaApp : Application() {
    val room = Room.databaseBuilder(this, PersonaDb::class.java, "persona").build()
}