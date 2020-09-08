package ru.geekbrains.mynotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.mynotes.model.Repository
import ru.geekbrains.mynotes.view.MainViewState

class MainViewModel : ViewModel() {
    private val viewStateLiveData: MutableLiveData<List<Note>> = MutableLiveData()

    init {
        viewStateLiveData.value = MainViewState(Repository.notesList).notes
    }

    fun getViewState(): LiveData<List<Note>> = viewStateLiveData
}