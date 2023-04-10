package com.example.cleanarchitecturenotesapp.feature_note.presentation.notes

import com.example.cleanarchitecturenotesapp.feature_note.domain.model.Note
import com.example.cleanarchitecturenotesapp.feature_note.domain.util.NoteOrder

/**
 * Listado de todas las acciones únicas que puede realizar el usuario en la pantalla de lista de notas
 */
sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder) : NotesEvent()
    data class DeleteNote(val note: Note) : NotesEvent()
    object RestoreNote : NotesEvent()
    object ToggleOrderSection : NotesEvent()
}
