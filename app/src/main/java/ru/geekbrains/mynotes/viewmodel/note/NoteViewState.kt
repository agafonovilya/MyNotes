package ru.geekbrains.mynotes.viewmodel.note

import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.viewmodel.base.BaseViewState

class NoteViewState(data: Data = Data(), error: Throwable? = null)
    : BaseViewState<NoteViewState.Data>(data, error) {

    data class Data(
        val isDeleted: Boolean = false, val note: Note? = null)
}