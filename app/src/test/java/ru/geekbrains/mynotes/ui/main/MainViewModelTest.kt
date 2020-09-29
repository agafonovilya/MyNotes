package ru.geekbrains.mynotes.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.model.Repository
import ru.geekbrains.mynotes.model.Result
import ru.geekbrains.mynotes.viewmodel.main.MainViewModel
import ru.geekbrains.mynotes.viewmodel.main.MainViewState

class MainViewModelTest{

    @get:Rule
    val taskExecutionRule = InstantTaskExecutorRule()


    private val mockRepository = mockk<Repository>()
    private val notesLiveData = MutableLiveData<Result>()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup(){
        clearAllMocks()
        every { mockRepository.getNotes() } returns notesLiveData
        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `should call getNotes once`() {
        verify(exactly = 1) { mockRepository.getNotes() }
    }

    @Test
    fun `should return Notes`() {
        var result: MainViewState? = null
        val testData = listOf(Note(), Note())

        viewModel.getViewState().observeForever{
            result = it
        }
        notesLiveData.value = Result.Success(testData)

        assertEquals(result?.data, testData)
        assertEquals(result?.error, null)
    }

    @Test
    fun `should return error`() {
        var result: MainViewState? = null
        val testData = Throwable("error")

        viewModel.getViewState().observeForever{
            result = it
        }
        notesLiveData.value = Result.Error(testData)

        assertEquals(result?.error, testData)
        assertEquals(result?.notes, null)
    }

    @Test
    fun `should remove observer`(){
        viewModel.onCleared()

        assertFalse(notesLiveData.hasObservers())
    }

}