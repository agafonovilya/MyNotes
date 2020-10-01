package ru.geekbrains.mynotes.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.model.Repository
import ru.geekbrains.mynotes.model.Result
import ru.geekbrains.mynotes.viewmodel.main.MainViewModel

class MainViewModelTest{

    @get:Rule
    val taskExecutionRule = InstantTaskExecutorRule()


    private val mockRepository = mockk<Repository>()
    private val channel = Channel<Result>(Channel.CONFLATED)
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup(){
        clearAllMocks()
        every { (mockRepository.getNotes())} returns channel
        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `should call getNotes once`() {
        verify(exactly = 1) { mockRepository.getNotes() }
    }

    @Test
    fun `should return Notes`() = runBlocking{
        val testData = listOf(Note(), Note())

        val deferred = async {
            viewModel.getViewState().receive()
        }
        channel.send(Result.Success(testData))

        assertEquals(deferred.await(), testData)
    }

    @Test
    fun `should return error`() = runBlocking {
        val testData = Throwable("error")
        val deferred = async {
            viewModel.getErrorChannel().receive()
        }
        channel.send(Result.Error(testData))
        assertEquals(deferred.await(), testData)
    }

    @Test
    fun `should remove observer`() = runBlocking{
        val testData = listOf(Note(), Note())
        channel.send(Result.Success(testData))//нополняем
        viewModel.onCleared()                     //очищаем
        assertFalse(channel.isFull)
    }

}