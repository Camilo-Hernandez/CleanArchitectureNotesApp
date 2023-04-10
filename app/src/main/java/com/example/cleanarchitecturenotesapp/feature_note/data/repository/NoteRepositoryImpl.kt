package com.example.cleanarchitecturenotesapp.feature_note.data.repository

import com.example.cleanarchitecturenotesapp.feature_note.data.data_source.NoteDao
import com.example.cleanarchitecturenotesapp.feature_note.domain.model.Note
import com.example.cleanarchitecturenotesapp.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl (
    private val dao: NoteDao
) : NoteRepository {
    /**
     * Estas funciones únicamente retornan el método del dao, puesto que hay una única base de datos
     * que es de Room. Si hubiera diferentes fuentes de datos como una local y una API, habría
     * algo más de lógica aquí, con tal de seleccionar la base de datos correcta para recuperar
     * el dato pedido.
     */
    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        return dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        return dao.deleteNote(note)
    }
}