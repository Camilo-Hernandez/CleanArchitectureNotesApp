package com.example.cleanarchitecturenotesapp.feature_note.domain.use_case.notes_list_screen

import com.example.cleanarchitecturenotesapp.feature_note.domain.model.Note
import com.example.cleanarchitecturenotesapp.feature_note.domain.repository.NoteRepository

class DeleteNote(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}