package com.example.cleanarchitecturenotesapp.feature_note.domain.use_case

import com.example.cleanarchitecturenotesapp.feature_note.domain.model.Note
import com.example.cleanarchitecturenotesapp.feature_note.domain.repository.NoteRepository
import com.example.cleanarchitecturenotesapp.feature_note.domain.util.NoteOrder
import com.example.cleanarchitecturenotesapp.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * El papel de los casos de uso es el que tenía el View Model en el original MVVM: implementar
 * la lógica del negocio de una única acción que realiza el usuario.
 * Los casos de uso se crean para prevenir la repetición de código si la misma lógica se fuera a
 * utilizar en más de 1 View Model. En este caso, no es necesario pues la app emplea GetNotes
 * en un único View Model, por lo que generar esta función es algo innecesario en este caso.
 */
class GetNotes(
    private val repository: NoteRepository,
) {
    // Los casos de uso solamente deberían tener una única función pública para ser llamada desde afuera
    // Algunos la llaman execute, otros como "operator fun invoke" para sobreescribir invoke y llamar la clase como una función.
    operator fun invoke(noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)): Flow<List<Note>> {
        /**
         * Se usa map para realizar algún tipo de transformación de la función repository.getNotes()
         * En este caso, cambiar el orden
          */

        return repository.getNotes().map { notes ->
            when(noteOrder.orderType) {
                is OrderType.Ascending -> {
                    when (noteOrder){
                        is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedBy { it.timestamp }
                        is NoteOrder.Color -> notes.sortedBy { it.color }
                    }
                }
                is OrderType.Descending -> {
                    when (noteOrder){
                        is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedByDescending { it.timestamp }
                        is NoteOrder.Color -> notes.sortedByDescending { it.color }
                    }
                }
            }
        }
    }
}