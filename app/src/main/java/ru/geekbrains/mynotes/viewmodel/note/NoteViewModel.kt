package ru.geekbrains.mynotes.viewmodel.note

import androidx.lifecycle.ViewModel
import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.model.Repository

class NoteViewModel(private val repository: Repository = Repository) : ViewModel() {

    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        if (pendingNote != null) {
            repository.saveNote(pendingNote!!)
        }
    }
}