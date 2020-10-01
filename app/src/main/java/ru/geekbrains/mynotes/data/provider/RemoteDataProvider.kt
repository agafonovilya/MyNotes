package ru.geekbrains.mynotes.data.provider

import kotlinx.coroutines.channels.ReceiveChannel
import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.model.Result
import ru.geekbrains.mynotes.model.User

interface RemoteDataProvider {
    fun subscribeToAllNotes() : ReceiveChannel<Result>

    suspend fun getCurrentUser() : User?
    suspend fun saveNote(note: Note) : Note
    suspend fun getNoteById(id: String) : Note?
    suspend fun deleteNote(id: String)
}