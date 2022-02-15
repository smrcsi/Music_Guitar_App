package com.example.guitar_music_app.lecture.chordLecture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.guitar_music_app.results.ResultRepository
import kotlinx.coroutines.Dispatchers

class ChordsViewModelFactory(
    private val resultRepo: ResultRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return ChordsViewModel(resultRepo, Dispatchers.Main) as T
    }

}