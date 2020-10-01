package ru.geekbrains.mynotes.model

import ru.geekbrains.mynotes.data.provider.RemoteDataProvider

class Repository(private val remoteProvider: RemoteDataProvider) {

    fun getNotes() = remoteProvider.subscribeToAllNotes()

    suspend fun getCurrentUser() = remoteProvider.getCurrentUser()
    suspend fun saveNote(note: Note) = remoteProvider.saveNote(note)
    suspend fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    suspend fun deleteNote(id: String) = remoteProvider.deleteNote(id)
}