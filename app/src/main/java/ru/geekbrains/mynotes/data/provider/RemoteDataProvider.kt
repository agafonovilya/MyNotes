package ru.geekbrains.mynotes.data.provider

import androidx.lifecycle.LiveData
import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.model.Result
import ru.geekbrains.mynotes.model.User

interface RemoteDataProvider {
    fun subscribeToAllNotes(): LiveData<Result>
    fun getNoteById(id: String): LiveData<Result>
    fun saveNote(note: Note): LiveData<Result>
    fun getCurrentUser(): LiveData<User?>
    fun deleteNote(id: String): LiveData<Result>
}