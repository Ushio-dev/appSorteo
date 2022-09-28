package com.example.sorteo

import androidx.room.*

@Dao
interface PersonaDao {
    @Query("SELECT * FROM Persona")
    suspend fun getAll(): List<Persona>

    @Query("SELECT * FROM Persona WHERE id = :id")
    suspend fun findById(id: Int): Persona

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(personas: Persona)

    @Query("SELECT COUNT(nombre) FROM persona")
    suspend fun countPeople(): Int

    @Query("DELETE FROM persona")
    suspend fun deleteAll()

    @Query("UPDATE persona SET ganador = :gan WHERE id = :id")
    suspend fun updateGanador(gan: Boolean, id: Int)

    @Query("SELECT id FROM persona LIMIT 1")
    suspend fun traerPrimero(): Int

    @Query("SELECT id FROM persona ORDER BY id DESC LIMIT 1 ")
    suspend fun traerUltimo(): Int

    @Query("UPDATE persona SET ganador = :gan")
    suspend fun setearFalse(gan: Boolean)
}