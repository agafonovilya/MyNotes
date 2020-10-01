package ru.geekbrains.mynotes.viewmodel.spalsh

import kotlinx.coroutines.launch
import ru.geekbrains.mynotes.data.errors.NoAuthException
import ru.geekbrains.mynotes.model.Repository
import ru.geekbrains.mynotes.viewmodel.base.BaseViewModel

class SplashViewModel(private val repository: Repository): BaseViewModel<Boolean?>(){
    fun requestUser() = launch {
        repository.getCurrentUser()?.let {
            setData(true)
        } ?: setError(NoAuthException())
    }
}