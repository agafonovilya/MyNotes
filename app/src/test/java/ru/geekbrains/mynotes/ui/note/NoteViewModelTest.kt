package ru.geekbrains.mynotes.ui.note

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.model.Repository
import ru.geekbrains.mynotes.model.Result
import ru.geekbrains.mynotes.viewmodel.note.NoteViewModel
import ru.geekbrains.mynotes.viewmodel.note.NoteViewState


class NoteViewModelTest {
    @get:Rule
    val taskExecutionRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<Repository>()
    private val noteLiveData = MutableLiveData<Result>()
    private val testNote = Note("TESTNOTEID")
    private lateinit var viewModel: NoteViewModel

    @Before
    fun setup(){
        clearAllMocks()
        every { mockRepository.getNoteById(testNote.id) } returns noteLiveData
        every { mockRepository.deleteNote(testNote.id) } returns noteLiveData
        viewModel = NoteViewModel(mockRepository)
    }

    @Test
    fun `loadNote should return Note`(){
        var result: NoteViewState.Data? = null
        val testData = NoteViewState.Data(false, testNote)
        viewModel.getViewState().observeForever {
            result = it?.data
        }
        viewModel.loadNote(testNote.id)
        noteLiveData.value = Result.Success(testNote)
        assertEquals(testData, result)
    }


    @Test
    fun `loadNote should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.getViewState().observeForever {
            result = it?.error
        }
        viewModel.loadNote(testNote.id)
        noteLiveData.value = Result.Error(testData)
        assertEquals(testData, result)
    }

    @Test
    fun `delete should return NoteData with isDeleted`() {
        var result: NoteViewState.Data? = null
        val testData = NoteViewState.Data(true, null)
        viewModel.getViewState().observeForever {
            result = it?.data
        }

        viewModel.saveChanges(testNote)
        viewModel.deleteNote()
        noteLiveData.value = Result.Success(null)
        assertEquals(testData, result)
    }

    @Test
    fun `delete should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.getViewState().observeForever {
            result = it?.error
        }
        viewModel.saveChanges(testNote)
        viewModel.deleteNote()
        noteLiveData.value = Result.Error(error = testData)
        assertEquals(testData, result)
    }

}