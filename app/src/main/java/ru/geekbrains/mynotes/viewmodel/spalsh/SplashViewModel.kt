package ru.geekbrains.mynotes.viewmodel.spalsh

import ru.geekbrains.mynotes.data.errors.NoAuthException
import ru.geekbrains.mynotes.model.Repository
import ru.geekbrains.mynotes.viewmodel.base.BaseViewModel

class SplashViewModel(private val repository: Repository) : BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        repository.getCurrentUser().observeForever {
            viewStateLiveData.value = if (it != null) {
                SplashViewState(isAuth = true)
            } else {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}