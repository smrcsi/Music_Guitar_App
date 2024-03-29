package com.example.guitar_music_app.statistics.lectureResult

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.guitar_music_app.general.BaseViewModel
import com.example.guitar_music_app.general.GET_RESULTS_ERROR
import com.example.guitar_music_app.general.GeneralResult
import com.example.guitar_music_app.results.Result
import com.example.guitar_music_app.results.ResultRepository
import com.example.guitar_music_app.statistics.StatisticsEvent
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class LectureResultViewModel(
    val resultRepo: ResultRepository,
    uiContext: CoroutineContext
) : BaseViewModel<StatisticsEvent>(uiContext) {
    private val resultListState = MutableLiveData<List<Result>>()
    val resultList: LiveData<List<Result>> get() = resultListState

    private val lastResultState = MutableLiveData<String>()
    val lastResult: LiveData<String> get() = lastResultState

    private val bestResultState = MutableLiveData<String>()
    val bestResult: LiveData<String> get() = bestResultState

    val lecturePlayed = MutableLiveData<String>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun handleEvent(event: StatisticsEvent) {
        when (event) {
            is StatisticsEvent.OnStart -> getResults()
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun getResults() = launch {
        when (val resultsResult = resultRepo.getResults()) {
            is GeneralResult.Value -> {
                resultListState.value = resultsResult.value
                lastResultState.value = resultsResult.value.last().score
                if (resultsResult.value.last().type == "chords") {
                    val chordsResults =
                        resultsResult.value.filter { result: Result -> result.type == "chords" }
                    bestResultState.value = chordsResults.map {it.score.toInt()}.sorted().lastOrNull().toString()
//                        chordsResults.maxWithOrNull(Comparator.comparingInt { it.score.toInt() })?.score
                } else if (resultsResult.value.last().type == "notes") {
                    val notesResults =
                        resultsResult.value.filter { result: Result -> result.type == "notes" }
                    println(notesResults)
                    bestResultState.value = notesResults.map {it.score.toInt()}.sorted().lastOrNull().toString()
//                        notesResults.maxWithOrNull(Comparator.comparingInt { it.score.toInt() })?.score
                } else if (resultsResult.value.last().type == "rhythm") {
                    val rhythmResults =
                        resultsResult.value.filter { result: Result -> result.type == "rhythm" }
                    bestResultState.value = rhythmResults.map {it.score.toInt()}.sorted().lastOrNull().toString()
//                        rhythmResults.maxWithOrNull(Comparator.comparingInt { it.score.toInt() })?.score
                }
                lecturePlayed.value = resultsResult.value.last().type
            }
            is GeneralResult.Error -> errorState.value = GET_RESULTS_ERROR
        }

    }

}