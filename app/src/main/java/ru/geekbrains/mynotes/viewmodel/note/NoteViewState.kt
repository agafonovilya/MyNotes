package ru.geekbrains.mynotes.viewmodel.note

import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.viewmodel.base.BaseViewState

class NoteViewState(note: Note? = null, error: Throwable? = null) : BaseViewState<Note?>(note, error)