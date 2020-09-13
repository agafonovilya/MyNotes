package ru.geekbrains.mynotes.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

object Repository {
    private val notesListLiveData = MutableLiveData<List<Note>>()
    private val notesList: MutableList<Note> = mutableListOf(
        Note(UUID.randomUUID().toString(), "титул1", "текст заметки", Color.WHITE),
        Note(UUID.randomUUID().toString(), "титул2", "текст заметки", Color.BLUE),
        Note(UUID.randomUUID().toString(), "титул3", "текст заметки", Color.GREEN),
        Note(UUID.randomUUID().toString(), "титул4", "текст заметки", Color.PINK),
        Note(UUID.randomUUID().toString(), "титул5", "текст заметки", Color.RED),
        Note(UUID.randomUUID().toString(), "титул6", "текст заметки", Color.VIOLET),
        Note(UUID.randomUUID().toString(), "титул7", "текст заметки", Color.YELLOW),
        Note(UUID.randomUUID().toString(), "титул8", "текст заметки", Color.WHITE),
        Note(UUID.randomUUID().toString(), "титул9", "текст заметки", Color.BLUE),
        Note(UUID.randomUUID().toString(), "титул10", "текст заметки", Color.GREEN),
        Note(UUID.randomUUID().toString(), "титул11", "текст заметки", Color.PINK),
        Note(UUID.randomUUID().toString(), "титул12", "текст заметки", Color.RED),
        Note(UUID.randomUUID().toString(), "титул13", "текст заметки", Color.VIOLET),
        Note(UUID.randomUUID().toString(), "титул14", "текст заметки", Color.YELLOW),
        Note(UUID.randomUUID().toString(), "титул15", "текст заметки", Color.WHITE),
        Note(UUID.randomUUID().toString(), "титул16", "текст заметки", Color.BLUE),
        Note(UUID.randomUUID().toString(), "титул17", "текст заметки", Color.GREEN),
        Note(UUID.randomUUID().toString(), "титул18", "текст заметки", Color.PINK),
        Note(UUID.randomUUID().toString(), "титул19", "текст заметки", Color.RED),
        Note(UUID.randomUUID().toString(), "титул20", "текст заметки", Color.VIOLET),
        Note(UUID.randomUUID().toString(), "титул21", "текст заметки", Color.YELLOW)
        )

    init {
        notesListLiveData.value = notesList
    }

    fun getNotesList(): LiveData<List<Note>> {
        return notesListLiveData
    }

    fun saveNote(note: Note) {
        addOrReplaceNote(note)
        notesListLiveData.value = notesList
    }

    private fun addOrReplaceNote(note: Note) {
        for (i in 0 until notesList.size) {
            if (notesList[i] == note) {
                notesList[i] = note
                return
            }
        }

        notesList.add(note)
    }

}