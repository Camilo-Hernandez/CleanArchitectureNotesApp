package com.example.cleanarchitecturenotesapp.feature_note.domain.repository

import com.example.cleanarchitecturenotesapp.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * Se declara como interfaz para el testing. Se pueden crear fácilmente versiones falsas del
 * repositorio. Las pruebas deben ser rápidas, acceder a los datos reales es lento y representa
 * un riesgo en cuanto a los casos extremos, pues los datos reales podrían no cumplir con las
 * condiciones de borde. A los Use Cases no les interesa de dónde proviene la data, por lo que
 * tener fake repository es idóneo para probar los Use Cases también.
 */
interface NoteRepository {
    fun getNotes(): Flow<List<Note>>

    suspend fun getNoteById(id: Int): Note?

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)
}