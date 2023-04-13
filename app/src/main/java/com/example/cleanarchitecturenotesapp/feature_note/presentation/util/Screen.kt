package com.example.cleanarchitecturenotesapp.feature_note.presentation.util

sealed class Screen(val route: String) {
    object NotesScreen: Screen("notes_screen")
    object AddEditScreen: Screen("add_edit_screen")
}
