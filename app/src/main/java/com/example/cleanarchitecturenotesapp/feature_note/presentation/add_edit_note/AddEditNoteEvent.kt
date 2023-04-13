package com.example.cleanarchitecturenotesapp.feature_note.presentation.add_edit_note

import androidx.compose.ui.focus.FocusState

sealed class AddEditNoteEvent {
    // Eventos para cuando se ingresa el texto
    data class EnteredTitle(val value: String) : AddEditNoteEvent()
    data class EnteredContent(val value: String) : AddEditNoteEvent()
    // Eventos para cuando se enfoca un campo de texto
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState) : AddEditNoteEvent()
    // Evento de cambio el color
    data class ChangeColor(val color: Int) : AddEditNoteEvent()
    // Evento de click en el floatingActionButton
    object SaveNote : AddEditNoteEvent()
}