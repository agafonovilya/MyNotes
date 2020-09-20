package ru.geekbrains.mynotes.viewmodel.spalsh

import ru.geekbrains.mynotes.viewmodel.base.BaseViewState

class SplashViewState(isAuth: Boolean? = null, error: Throwable? = null) :
    BaseViewState<Boolean?>(isAuth, error)