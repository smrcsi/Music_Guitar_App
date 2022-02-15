package com.example.guitar_music_app.menus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.guitar_music_app.general.GET_RESULTS_ERROR
import com.example.guitar_music_app.general.GeneralResult
import com.example.guitar_music_app.results.Result
import com.example.guitar_music_app.results.ResultRepository
import com.example.guitar_music_app.results.resultList.ResultListEvent
import com.example.guitar_music_app.general.BaseViewModel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class HomepageViewModel (
    val resultRepo: ResultRepository,
    uiContext: CoroutineContext
    ) : BaseViewModel<ResultListEvent>(uiContext) {
        private val resultListState = MutableLiveData<List<Result>>()
        val resultList: LiveData<List<Result>> get() = resultListState

        private val editResultState = MutableLiveData<String>()
        val editResult: LiveData<String> get() = editResultState


        override fun handleEvent(event: ResultListEvent) {
            when (event) {
                is ResultListEvent.OnStart -> getResults()
                is ResultListEvent.OnResultItemClick -> editResult(event.position)
            }
        }

        private fun editResult(position: Int) {
            editResultState.value = resultList.value!![position].creationDate
        }

        private fun getResults() = launch {
            val resultsResult = resultRepo.getResults()

            when (resultsResult) {
                is GeneralResult.Value -> resultListState.value = resultsResult.value
                is GeneralResult.Error -> errorState.value = GET_RESULTS_ERROR
            }
        }
}