package ru.geekbrains.mynotes.viewmodel.main

import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.viewmodel.base.BaseViewState

class MainViewState(val notes: List<Note>? = null, error: Throwable? = null) :
    BaseViewState<List<Note>?>(notes, error)