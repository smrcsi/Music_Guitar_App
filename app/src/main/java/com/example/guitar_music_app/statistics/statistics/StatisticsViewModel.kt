package com.example.guitar_music_app.statistics.statistics

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


class StatisticsViewModel(
    val resultRepo: ResultRepository,
    uiContext: CoroutineContext
) : BaseViewModel<StatisticsEvent>(uiContext) {


    private val resultListState = MutableLiveData<List<Result>>()
    val resultList: LiveData<List<Result>> get() = resultListState

    private val chordsResultState = MutableLiveData(ChordsState())
    val updateChords: LiveData<ChordsState> get() = chordsResultState

    private val notesResultState = MutableLiveData(NotesState())
    val updateNotes: LiveData<NotesState> get() = notesResultState

    private val rhythmResultState = MutableLiveData(RhythmState())
    val updateRhythm: LiveData<RhythmState> get() = rhythmResultState

    val finalResultState = MutableLiveData<Int>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun handleEvent(event: StatisticsEvent) {
        when (event) {
            is StatisticsEvent.OnStart -> getResults()
        }
    }

    data class NotesState(
        var notesLastValue: String = "",
        var notesBestValue: String = "",
        var notesCompleteValue: String = ""
    )

    data class ChordsState(
        var chordsLastValue: String = "",
        var chordsBestValue: String = "",
        var chordsCompleteValue: String = ""
    )

    data class RhythmState(
        var rhythmLastValue: String = "",
        var rhythmBestValue: String = "",
        var rhythmCompleteValue: String = ""
    )


    @RequiresApi(Build.VERSION_CODES.N)
    private fun getResults() = launch {
        when (val resultsResult = resultRepo.getResults()) {
            is GeneralResult.Value -> {
                //Notes fill
                resultListState.value = resultsResult.value
                val notesResults =
                    resultsResult.value.filter { result: Result -> result.type == "notes" }
                val currentNotesState = notesResultState.value
                val notesLastResult = notesResults.last().score
                val notesBestResult =
                    notesResults.maxWithOrNull(Comparator.comparingInt { it.score.toInt() })!!.score
                val notesCompleteResult =
                    notesResults.sumOf { result: Result -> result.score.toInt() }.toString()
                notesResultState.value = currentNotesState?.copy(
                    notesLastValue = notesLastResult,
                    notesBestValue = notesBestResult,
                    notesCompleteValue = notesCompleteResult
                )

                //chords fill
                val chordsResults =
                    resultsResult.value.filter { result: Result -> result.type == "chords" }
                val currentChordsState = chordsResultState.value
                val chordsLastResult = chordsResults.last().score
                val chordsBestResult =
                    chordsResults.maxWithOrNull(Comparator.comparingInt { it.score.toInt() })!!.score
                val chordsCompleteResult =
                    chordsResults.sumOf { result: Result -> result.score.toInt() }.toString()
                chordsResultState.value = currentChordsState?.copy(
                    chordsLastValue = chordsLastResult,
                    chordsBestValue = chordsBestResult,
                    chordsCompleteValue = chordsCompleteResult
                )

                //rhythm fill
                val rhythmResults =
                    resultsResult.value.filter { result: Result -> result.type == "rhythm" }
                val currentRhythmState = rhythmResultState.value
                val rhythmLastResult = rhythmResults.last().score
                val rhythmBestResult =
                    rhythmResults.maxWithOrNull(Comparator.comparingInt { it.score.toInt() })!!.score
                val rhythmCompleteResult =
                    rhythmResults.sumOf { result: Result -> result.score.toInt() }.toString()
                rhythmResultState.value = currentRhythmState?.copy(
                    rhythmLastValue = rhythmLastResult,
                    rhythmBestValue = rhythmBestResult,
                    rhythmCompleteValue = rhythmCompleteResult
                )

                //Final fill
                finalResultState.value = notesCompleteResult.toInt() + chordsCompleteResult.toInt() + rhythmCompleteResult.toInt()
            }
            is GeneralResult.Error -> errorState.value = GET_RESULTS_ERROR
        }
    }
}