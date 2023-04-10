package com.example.cleanarchitecturenotesapp.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cleanarchitecturenotesapp.ui.theme.*

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    @PrimaryKey val id: Int? = null,
){
    companion object{
        // Para declarar una lista quemada para elegir el color que tiene una nota
        // Companion Object sirve para declarar atributos y métodos estáticos (de la clase)
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

class InvalidNoteException(message: String) : Exception(message)