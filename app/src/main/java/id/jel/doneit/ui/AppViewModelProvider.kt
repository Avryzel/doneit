package id.jel.doneit.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import id.jel.doneit.DoneItApp

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            TaskViewModel(DoneItApp().database.taskDao())
        }
    }
}

fun CreationExtras.DoneItApp(): DoneItApp =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as DoneItApp)