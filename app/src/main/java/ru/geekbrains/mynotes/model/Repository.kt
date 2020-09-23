package ru.geekbrains.mynotes.model

import androidx.lifecycle.MutableLiveData
import ru.geekbrains.mynotes.data.provider.RemoteDataProvider

class Repository(private val remoteProvider: RemoteDataProvider) {

    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    fun getCurrentUser() = remoteProvider.getCurrentUser()
    fun deleteNote(noteId: String) = remoteProvider.deleteNote(noteId)

    private val notesLiveData = MutableLiveData<List<Note>>()
    private val notes = mutableListOf<Note>()

    init {
        notesLiveData.value = notes
    }
}