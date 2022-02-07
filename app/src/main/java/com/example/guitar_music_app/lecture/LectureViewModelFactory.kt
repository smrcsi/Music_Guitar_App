package com.example.guitar_music_app.lecture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.guitar_music_app.results.ResultRepository
import com.example.guitar_music_app.results.resultDetail.ResultViewModel
import kotlinx.coroutines.Dispatchers

class LectureViewModelFactory(
    private val lectureRepo: LectureRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return LectureViewModel(lectureRepo, Dispatchers.Main) as T
    }

}