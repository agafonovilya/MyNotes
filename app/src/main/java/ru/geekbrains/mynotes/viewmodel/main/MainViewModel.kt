package ru.geekbrains.mynotes.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.mynotes.model.Repository

class MainViewModel : ViewModel() {
    private val viewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()

    init {
        Repository.getNotesList().observeForever {
            viewStateLiveData.value =
                viewStateLiveData.value?.copy(notes = it!!)
                    ?: MainViewState(it!!)
        }
    }

    fun getViewState(): LiveData<MainViewState> = viewStateLiveData
}