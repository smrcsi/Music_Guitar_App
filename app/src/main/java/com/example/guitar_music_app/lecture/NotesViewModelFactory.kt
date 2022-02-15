package com.example.guitar_music_app.lecture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.guitar_music_app.results.ResultRepository
import com.example.guitar_music_app.results.resultDetail.ResultViewModel
import kotlinx.coroutines.Dispatchers

class NotesViewModelFactory(
    private val resultRepo: ResultRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return NotesViewModel(resultRepo, Dispatchers.Main) as T
    }

}