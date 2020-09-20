package ru.geekbrains.mynotes.data.provider

import androidx.lifecycle.LiveData
import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.model.NoteResult
import ru.geekbrains.mynotes.model.User

interface RemoteDataProvider {
    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
    fun getCurrentUser(): LiveData<User?>
}