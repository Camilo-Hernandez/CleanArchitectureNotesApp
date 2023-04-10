package com.example.cleanarchitecturenotesapp.feature_note.presentation.notes

import com.example.cleanarchitecturenotesapp.feature_note.domain.model.Note
import com.example.cleanarchitecturenotesapp.feature_note.domain.util.NoteOrder
import com.example.cleanarchitecturenotesapp.feature_note.domain.util.OrderType


data class NotesState(
    val notes: List<Note> = emptyList(), // by default
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSelectionVisible: Boolean = true,
)
