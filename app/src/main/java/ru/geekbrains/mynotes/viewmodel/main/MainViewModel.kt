package ru.geekbrains.mynotes.viewmodel.main

import androidx.lifecycle.Observer
import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.model.Result
import ru.geekbrains.mynotes.model.Repository
import ru.geekbrains.mynotes.viewmodel.base.BaseViewModel

class MainViewModel(private val repository: Repository) :
    BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = object : Observer<Result>{

        override fun onChanged(t: Result?) {
            if (t == null) return

            when(t) {
                is Result.Success<*> -> {
                    viewStateLiveData.value = MainViewState(notes = t.data as? List<Note>)
                }
                is Result.Error ->{
                    viewStateLiveData.value = MainViewState(error = t.error)
                }
            }
        }
    }

    private val repositoryNotes = repository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }

}