package com.example.cleanarchitecturenotesapp.di

import android.app.Application
import androidx.room.Room
import com.example.cleanarchitecturenotesapp.feature_note.data.data_source.NoteDatabase
import com.example.cleanarchitecturenotesapp.feature_note.data.repository.NoteRepositoryImpl
import com.example.cleanarchitecturenotesapp.feature_note.domain.repository.NoteRepository
import com.example.cleanarchitecturenotesapp.feature_note.domain.use_case.add_edit_note_screen.GetNoteById
import com.example.cleanarchitecturenotesapp.feature_note.domain.use_case.notes_list_screen.DeleteNote
import com.example.cleanarchitecturenotesapp.feature_note.domain.use_case.notes_list_screen.GetNotes
import com.example.cleanarchitecturenotesapp.feature_note.domain.use_case.notes_list_screen.InsertNote
import com.example.cleanarchitecturenotesapp.feature_note.domain.use_case.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application) : NoteDatabase {
        return Room.databaseBuilder(
            app, // Contexto de la app
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME,
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase) : NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(
        repository: NoteRepository
    ) : NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            insertNote = InsertNote(repository),
            getNoteById = GetNoteById(repository),
        )
    }
}