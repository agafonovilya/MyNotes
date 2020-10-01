package ru.geekbrains.mynotes.ui.note

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.model.Repository
import ru.geekbrains.mynotes.model.Result
import ru.geekbrains.mynotes.viewmodel.note.NoteViewModel

class NoteViewModelTest {
    @get:Rule
    val taskExecutionRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<Repository>()
    private val channel = Channel<Result>(Channel.CONFLATED)
    private val testNote = Note("TESTNOTEID")
    private lateinit var viewModel: NoteViewModel

    @Before
    fun setup(){
        clearAllMocks()
        every { mockRepository.getNotes()} returns channel
        viewModel = NoteViewModel(mockRepository)
    }

    @Test
    fun `loadNote should return Note`()= runBlocking {
        coEvery { mockRepository.getNoteById(testNote.id) } returns testNote
        val deferred = async {
            viewModel.getViewState().receive()
        }
        viewModel.loadNote(testNote.id)
    }


    @Test
    fun `loadNote should return error`() {

    }

    @Test
    fun `delete should return NoteData with isDeleted`() {
        coEvery { mockRepository.deleteNote(testNote.id) } returns Unit
    }

}