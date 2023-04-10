package com.example.cleanarchitecturenotesapp.feature_note.domain.use_case

import com.example.cleanarchitecturenotesapp.feature_note.domain.model.InvalidNoteException
import com.example.cleanarchitecturenotesapp.feature_note.domain.model.Note
import com.example.cleanarchitecturenotesapp.feature_note.domain.repository.NoteRepository

class InsertNote(private val repository: NoteRepository) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        // Arrojar exceptions cuando el contenido de la nota es vacío.
        when {
            note.title.isBlank() -> throw InvalidNoteException("The title of the note cannot be empty")
            note.content.isBlank() -> throw InvalidNoteException("The content of the note cannot be empty")
        }
        repository.insertNote(note)
    }
}
