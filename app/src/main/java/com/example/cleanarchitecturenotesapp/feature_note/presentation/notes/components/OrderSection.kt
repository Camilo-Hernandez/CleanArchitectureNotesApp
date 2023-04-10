package com.example.cleanarchitecturenotesapp.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cleanarchitecturenotesapp.feature_note.domain.util.NoteOrder
import com.example.cleanarchitecturenotesapp.feature_note.domain.util.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    onOrderChange: (NoteOrder) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        Row (
            modifier = modifier.fillMaxWidth()
        ){
            DefaultRadioButton(
                text = "Title",
                selected = noteOrder is NoteOrder.Title,
                onClick = {onOrderChange(NoteOrder.Title(noteOrder.orderType))}, // Mantenemos el mismo orderType, únicamente cambia el Title
            )
            DefaultRadioButton(
                text = "Date",
                selected = noteOrder is NoteOrder.Date,
                onClick = {onOrderChange(NoteOrder.Date(noteOrder.orderType))}, // Mantenemos el mismo orderType, únicamente cambia el Title
            )
            DefaultRadioButton(
                text = "Color",
                selected = noteOrder is NoteOrder.Color,
                onClick = {onOrderChange(NoteOrder.Color(noteOrder.orderType))}, // Mantenemos el mismo orderType, únicamente cambia el Title
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row (
            modifier = modifier.fillMaxWidth()
        ){
            DefaultRadioButton(
                text = "Ascending",
                selected = noteOrder.orderType is OrderType.Ascending,
                onClick = {
                    onOrderChange(
                        noteOrder.copy(OrderType.Ascending) // Se retorna un nuevo NoteOrder igual al antiguo
                    )
                }, // Mantenemos el mismo orderType, únicamente cambia el Title
            )
            DefaultRadioButton(
                text = "Descending",
                selected = noteOrder.orderType is OrderType.Descending,
                onClick = {
                    onOrderChange(
                        noteOrder.copy(OrderType.Descending) // Se retorna un nuevo NoteOrder igual al antiguo
                    )}, // Mantenemos el mismo orderType, únicamente cambia el Title
            )
        }
    }
}