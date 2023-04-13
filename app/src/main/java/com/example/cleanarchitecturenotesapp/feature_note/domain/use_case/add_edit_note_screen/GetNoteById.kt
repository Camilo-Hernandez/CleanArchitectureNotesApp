package com.example.cleanarchitecturenotesapp.feature_note.domain.use_case.add_edit_note_screen

import com.example.cleanarchitecturenotesapp.feature_note.domain.model.Note
import com.example.cleanarchitecturenotesapp.feature_note.domain.repository.NoteRepository

class GetNoteById(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note? {
        // Simplemente, llama el método del repositorio
        return repository.getNoteById(id)
    }
}