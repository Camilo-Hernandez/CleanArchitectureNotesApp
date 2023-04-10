package com.example.cleanarchitecturenotesapp.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitecturenotesapp.feature_note.domain.model.Note
import com.example.cleanarchitecturenotesapp.feature_note.domain.use_case.NoteUseCases
import com.example.cleanarchitecturenotesapp.feature_note.domain.util.NoteOrder
import com.example.cleanarchitecturenotesapp.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel () {
    // Se define el estado que contiene los valores que únicamente el ViewModel puede ver (private) y modificar (mutable)
    private val _state = mutableStateOf(NotesState())
    // Se define el estado que contiene los valores que la UI observará (public)
    var state : State<NotesState> = _state

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob : Job? = null

    init {
        // Al inicio, por default
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        // Miramos si el modo de ordenamiento cambió para saber si se llama al Use Case
        // TODO: Verificar que funciona con _state o cambiarlo por state
        when(event) {
            // Llamar al NoteUseCases.GetNotes para actualizar el NotesState.notes
            is NotesEvent.Order ->
                if (_state.value.noteOrder::class != event.noteOrder::class
                    || _state.value.noteOrder.orderType != event.noteOrder.orderType
                ){
                    getNotes(event.noteOrder)
                }


            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    /**
                    * deleteNote Se llama como una función a pesar de ser una clase porque se sobreescribió el método invoke() que recibe una nota
                    * Se accede directamente al parámetro note del event porque con el when se da por hecho que el event es tipo NotesEvent.DeleteNote
                    * donde DeleteNote es la clase de datos con el parámetro note: Note
                     */
                    noteUseCases.deleteNote(event.note)
                    // Se guarda una referencia de la nota eliminada para restaurarla dado el caso
                    recentlyDeletedNote = event.note
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    /**
                     * La siguiente línea debería tener el mismo efecto que: recentlyDeletedNote?.let { noteUseCases.insertNote(it) }
                     * return@launch dentro de una corrutina significa que se quiere retornar desde
                     * la función que se pasó como argumento al operador launch, y no desde la
                     * función que contiene el launch1. Es una forma de usar una etiqueta o label
                     * para indicar el alcance del retorno.
                     * Por ejemplo:

                    ```kotlin
                    fun foo() {
                        launch {
                            if (condition) return@launch // retorna desde la función lambda que se pasó al launch
                            // sigue ejecutando la corrutina
                        }
                        // sigue ejecutando la función foo
                    }
                    ```
                    * Si no se utiliza el return@launch, el retorno sería desde la función foo,
                     * y no solo desde la corrutina1. Por ejemplo:

                    ```kotlin
                    fun foo() {
                        launch {
                            if (condition) return // retorna desde la función foo
                            // no llega a ejecutar esta parte
                            }
                        // no llega a ejecutar esta parte
                    }
                    ```
                    */
                    noteUseCases.insertNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }
            // Actualizar el estado de NotesState.isOrderSelectionVisible directamente
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                        isOrderSelectionVisible = !state.value.isOrderSelectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        // Se necesita cancelar el Flow anterior de las notas antes de sincronizar con un flow nuevo
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder) // Retorna un Flow de la lista de la DB
             .onEach {// operar con el flujo para acomodarlo al Compose State
                 _state.value = _state.value.copy(
                     notes = it,
                     noteOrder = noteOrder,
                 )
             }
             .launchIn(viewModelScope)
    }
}