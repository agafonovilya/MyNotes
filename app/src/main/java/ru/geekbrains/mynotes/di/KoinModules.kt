package ru.geekbrains.mynotes.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ru.geekbrains.mynotes.data.provider.FireStoreProvider
import ru.geekbrains.mynotes.data.provider.RemoteDataProvider
import ru.geekbrains.mynotes.model.Repository
import ru.geekbrains.mynotes.viewmodel.main.MainViewModel
import ru.geekbrains.mynotes.viewmodel.note.NoteViewModel
import ru.geekbrains.mynotes.viewmodel.spalsh.SplashViewModel

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireStoreProvider(get(), get()) } bind RemoteDataProvider::class
    single { Repository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}