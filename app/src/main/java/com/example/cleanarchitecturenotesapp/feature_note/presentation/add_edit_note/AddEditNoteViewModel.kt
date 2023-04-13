package com.example.cleanarchitecturenotesapp.feature_note.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanarchitecturenotesapp.feature_note.domain.model.InvalidNoteException
import com.example.cleanarchitecturenotesapp.feature_note.domain.model.Note
import com.example.cleanarchitecturenotesapp.feature_note.domain.use_case.notes_list_screen.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    // Provee los argumentos de navegación entre screens. Solamente se necesita proveerse aquí y automáticamente se inyecta.
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    /**
     * Necesitamos un estado para el color, para el título, para el contenido y para el hint
     * En este caso, no es bueno tener un estado que contenga todos los estados porque en la
     * medida en la que se escribe y borra contenido, el screen entero que se crea con el estado
     * se recompone, lo cual vuelve la UX y el rendimiento ineficientes y torpes.
     */
    private val _noteTitle = mutableStateOf(NoteTextFieldState(
        hint = "Enter title.."
    ))
    val noteTitle : State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(
        hint = "Enter the content..."
    ))
    val noteContent : State<NoteTextFieldState> = _noteContent

    // Tipo ARGB color int es tipo Int
    private val _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColor : State<Int> = _noteColor // Esta forma también se puede, pero se recomienda la primera: private val noteColor : State<Int> = _noteColor

    /**
     * Los "estados" de eventos se gestionan de la manera como lo dice la siguiente línea.
     * Son aquellos eventos como mostrar el snackbar: sucede una sola vez y no representan realmente estados.
     * Girar la pantalla no debería hacer que este evento vuelva a suceder, ya que se activa con una señal, no es
     * la conservación de un estado.
     * No hay una forma propia de Jetpack Compose para hacerlo, sino que se usa la que siempre
     * se ha utilizado en XML.
     * Así se pueden enviar eventos que se disparan una vez, con el SharedFlow
     */
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        // Cuando damos click en el save button y queremos navegar atrás de regreso a la lista.
        object SaveNote : UiEvent()
    }

    var currentNoteId : Int? = null

    init {
        // Lo que se ejecuta primero cuando se accede a la pantalla
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            /**
             * -1 Es el argumento por defecto que se pasa cuando se navega a la pantalla de edición sin dar click en una nota, es decir, cuando le damos click al FloatingActionButton
             * Por lo tanto, cuando se pasa por este condicional, si es diferente de -1, significa que se accedió a la pantalla al darle click a una nota.
             * En este caso, se debe llamar a la base de datos a través del repositorio y los casos de uso para obtener la información de la nota y pintarla en la UI.
            */
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNoteById(noteId)?.also {note ->
                        // Aquí ya se tiene acceso a la nota, al obtenerse cuando no es nula
                        currentNoteId = note.id
                        // Actualizamos el estado del título
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false,
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false,
                        )
                        _noteColor.value = note.color

                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent){
        when (event) {
            is AddEditNoteEvent.ChangeColor -> _noteColor.value = event.color
            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    // Se muestra el hint cuando no está enfocado y viceversa
                    isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    // Se muestra el hint cuando no está enfocado y viceversa
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(text = event.value)
            }
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(text = event.value)
            }
            AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.insertNote(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                color = noteColor.value,
                                timestamp = System.currentTimeMillis(),
                                /**
                                 * Pasarse como un argumento de navegación. Cuando es nulo, significa
                                 * que se creó una nueva nota. No es que esté muy de acuerdo con esta forma de implementar el ID incremental
                                  */
                                id = currentNoteId
                            )
                        )
                        // Se emite la señal al Navigation Component para que navegue de requeso a atrás.
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException){
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
        }
    }

}