package com.example.guitar_music_app.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.guitar_music_app.results.resultList.ResultListViewModel
import kotlinx.coroutines.Dispatchers

class ResultListViewModelFactory(
    private val resultRepo: ResultRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return ResultListViewModel(resultRepo, Dispatchers.Main) as T
    }

}