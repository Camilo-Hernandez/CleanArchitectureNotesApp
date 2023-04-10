package com.example.cleanarchitecturenotesapp.feature_note.domain.use_case

/**
 * Clase wrapper, o sea contenedora, de los casos de uso.
 * Es para simplificar la inyección de la dependencia de los casos de uso, en lugar de pasar
 * todos los Use Cases, se pasa este wrapper.
 * Así, si se agregan más casos de uso, serán aquí y no directamente en el constructor
 * del View Model.
 */
data class NoteUseCases (
    val getNotes: GetNotes,
    val deleteNote: DeleteNote,
    val insertNote: InsertNote,
)