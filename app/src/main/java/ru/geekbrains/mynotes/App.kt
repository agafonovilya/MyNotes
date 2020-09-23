package ru.geekbrains.mynotes

import androidx.multidex.MultiDexApplication
import org.koin.android.ext.android.startKoin
import ru.geekbrains.mynotes.di.appModule
import ru.geekbrains.mynotes.di.mainModule
import ru.geekbrains.mynotes.di.noteModule
import ru.geekbrains.mynotes.di.splashModule

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }
}
