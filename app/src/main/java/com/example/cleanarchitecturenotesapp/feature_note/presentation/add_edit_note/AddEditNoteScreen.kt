package com.example.cleanarchitecturenotesapp.feature_note.presentation.add_edit_note

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cleanarchitecturenotesapp.feature_note.domain.model.Note
import com.example.cleanarchitecturenotesapp.feature_note.presentation.add_edit_note.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel(),
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // El color de fondo puesto con una animación de la transición
    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(if (noteColor != -1) noteColor else viewModel.noteColor.value)
        )
    }

    /**
     * LaunchedEffect es una función componible que tiene dos parámetros: key1 y block. key1 es de
     * tipo Any, lo que significa que puede ser cualquier cosa, como un número, una cadena o un
     * booleano. Cada vez que el valor que se pasa en este parámetro cambia, se activa la función
     * block. block es una función de suspensión de Kotlin que tiene su propio alcance de corrutina.

     * Una función de suspensión es una función que puede pausarse y reanudarse más tarde, sin
     * bloquear el hilo en el que se ejecuta. Una corrutina es una forma de ejecutar funciones de
     * suspensión de manera concurrente, es decir, al mismo tiempo que otras tareas. El alcance de
     * una corrutina es el contexto en el que se crea y se cancela.

     * Una analogía para entender esto es pensar en LaunchedEffect como un lanzador de cohetes. El
     * parámetro key1 es el botón que activa el lanzamiento. Cada vez que presionas el botón con un
     * valor diferente, se lanza un nuevo cohete. El parámetro block es el código que se ejecuta
     * dentro del cohete. El cohete puede pausar su vuelo y reanudarlo más tarde, sin interferir
     * con otros cohetes o con el lanzador. El alcance del cohete es el espacio en el que vuela y
     * se destruye.
     *
     * Este código usa un flujo para recibir eventos desde el viewModel. Un flujo es una forma de
     * emitir y recibir valores de forma asíncrona y secuencial. Es como un río que fluye con datos.
     *
     * El método collectLatest recibe los últimos valores emitidos por el flujo y los procesa con
     * una función lambda. La función lambda usa una expresión when para verificar el tipo de evento
     * y actuar en consecuencia.
     */
    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest {
            when (it){
                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = it.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditNoteEvent.SaveNote)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) { Icon(imageVector = Icons.Rounded.Save, contentDescription = "Guardar nota") }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
        ) {
            // Fila de selección de colores
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Note.noteColors.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color) // se pone aquí para que esté atrás
                            .border(
                                width = 3.dp,
                                color = if (viewModel.noteColor.value == colorInt) {
                                    Color.DarkGray
                                } else Color.Transparent,
                                shape = CircleShape,
                            )
                            .clickable {
                                scope.launch {
                                    noteBackgroundAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(
                                            durationMillis = 500,
                                        )
                                    )
                                }
                                viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                            }
                    )
                }
            }
            // Título de la nota
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                isHintVisible = titleState.isHintVisible,
                textStyle = MaterialTheme.typography.headlineLarge,
            )
            // Contenido de la nota
            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                isHintVisible = contentState.isHintVisible,
                textStyle = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}