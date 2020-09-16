package ru.geekbrains.mynotes.viewmodel.base

open class BaseViewState<T>(val data: T, val error: Throwable?) {
}