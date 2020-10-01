package ru.geekbrains.mynotes.viewmodel.main

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.model.Repository
import ru.geekbrains.mynotes.viewmodel.base.BaseViewModel
import ru.geekbrains.mynotes.model.Result

class MainViewModel(private val repository: Repository) : BaseViewModel<List<Note>?>() {

    private val notesChanel = repository.getNotes()

    init {
        launch {
            notesChanel.consumeEach {
                when (it) {
                    is Result.Success<*> -> setData(it.data as? List<Note>)
                    is Result.Error -> setError(it.error)
                }
            }
        }
    }

    @VisibleForTesting
    public override fun onCleared() {
        super.onCleared()
        notesChanel.cancel()
    }

}