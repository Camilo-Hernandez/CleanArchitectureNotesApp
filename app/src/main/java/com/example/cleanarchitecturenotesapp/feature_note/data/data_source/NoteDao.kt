package com.example.cleanarchitecturenotesapp.feature_note.data.data_source

import androidx.room.*
import com.example.cleanarchitecturenotesapp.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow


/**
 * El DAO se declaró como interfaz porque Room se encarga de generar la implementación de los
 * métodos que se definen en ella. De esta forma, el programador solo tiene que declarar los métodos
 * con las anotaciones correspondientes y Room se ocupa de crear el código necesario para ejecutar
 * las consultas a la base de datos. Esto simplifica el trabajo y evita errores de sintaxis o lógica.
 */
@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    // Esta anotación es para reemplazar la nota existente cuando se le pasa la que tiene el mismo PrimaryKey, o sea, el id.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}