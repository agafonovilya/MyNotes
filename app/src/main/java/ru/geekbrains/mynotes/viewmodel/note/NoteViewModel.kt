package ru.geekbrains.mynotes.viewmodel.note

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.model.Result
import ru.geekbrains.mynotes.model.Repository
import ru.geekbrains.mynotes.viewmodel.base.BaseViewModel

class NoteViewModel(val repository: Repository) :
    BaseViewModel<NoteViewState.Data, NoteViewState>() {

    val progress = MutableLiveData<Boolean>(false)

    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        viewStateLiveData.value = NoteViewState(NoteViewState.Data(note = note))
    }

    override fun onCleared() {
        currentNote?.let { repository.saveNote(it) }
    }

    fun loadNote(noteId: String) {
        progress.postValue(true)
        repository.getNoteById(noteId).observeForever { t ->
            //добавил задержку так как все операции очень быстрые и прогресбара не было видно
            Handler().postDelayed({
                t?.let {
                    viewStateLiveData.value = when (t) {
                        is Result.Success<*> -> NoteViewState(NoteViewState.Data(note = t.data as? Note))
                        is Error -> NoteViewState(error = t.error)
                    }
                }
                progress.postValue(false)
            }, 2000)
        }
    }

    private val currentNote: Note?
        get() = viewStateLiveData.value?.data?.note

    fun deleteNote() {
        progress.postValue(true)
        currentNote?.let {
            repository.deleteNote(it.id).observeForever { t ->
                t?.let {
                    viewStateLiveData.value = when (it) {
                        is Result.Success<*> -> NoteViewState(NoteViewState.Data(isDeleted = true))
                        is Error -> NoteViewState(error = it.error)
                    }
                }
            }
            progress.postValue(false)
        }
    }

    fun showProgress():MutableLiveData<Boolean>{
        return progress
    }

}