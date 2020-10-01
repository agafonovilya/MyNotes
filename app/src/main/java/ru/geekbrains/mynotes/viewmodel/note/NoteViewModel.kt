package ru.geekbrains.mynotes.viewmodel.note

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.model.Result
import ru.geekbrains.mynotes.model.Repository
import ru.geekbrains.mynotes.viewmodel.base.BaseViewModel

class NoteViewModel(private val repository: Repository): BaseViewModel<NoteData>() {

    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        launch {
            pendingNote?.let {
                repository.saveNote(it)
            }
        }
    }

    fun loadNote(noteId: String)  = launch {
        try {
            repository.getNoteById(noteId)?.let {
                setData(NoteData(note = it))
            }
        } catch (t: Throwable) {
            setError(t)
        }
    }


    fun deleteNote() = launch{
        try {
            pendingNote?.let {
                repository.getNoteById(it.id)
                repository.deleteNote(it.id)
                pendingNote = null
            }
            setData(NoteData(isDeleted = true, note = null))
        } catch (e: Throwable) {
            setError(e)
        }
    }

}