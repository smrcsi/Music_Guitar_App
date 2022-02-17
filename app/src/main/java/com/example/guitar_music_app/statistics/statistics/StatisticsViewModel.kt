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
import kotlin.Exception
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
        var notesCompleteValue: String = "0",
        var notesErrorState: Int = 0
    )

    data class ChordsState(
        var chordsLastValue: String = "",
        var chordsBestValue: String = "",
        var chordsCompleteValue: String = "0",
        var chordsErrorState: Int = 0
    )

    data class RhythmState(
        var rhythmLastValue: String = "",
        var rhythmBestValue: String = "",
        var rhythmCompleteValue: String = "0",
        var rhythmErrorState: Int = 0
    )


    @RequiresApi(Build.VERSION_CODES.N)
    private fun getResults() = launch {
        when (val resultsResult = resultRepo.getResults()) {
            is GeneralResult.Value -> {
                //Notes fill
                resultListState.value = resultsResult.value
                try {
                    val notesResults =
                        resultsResult.value.filter { result: Result -> result.type == "notes" }
                    println(notesResults)
                    val currentNotesState = notesResultState.value
                    val notesLastResult = notesResults.last().score
                    val notesBestResult = notesResults.map {it.score.toInt()}.sorted().lastOrNull().toString()
//                        notesResults.maxWithOrNull(Comparator.comparingInt { it.score.toInt() })!!.score

                    val notesCompleteResult =
                        notesResults.sumOf { result: Result -> result.score.toInt() }.toString()
                    notesResultState.value = currentNotesState?.copy(
                        notesLastValue = notesLastResult,
                        notesBestValue = notesBestResult,
                        notesCompleteValue = notesCompleteResult,
                        notesErrorState = 1
                    )
                }
                catch (e: Exception) {
                    notesResultState.value?.notesErrorState = 0
                    println("no value for notes yet")
                }

                //chords fill
                try {
                    val chordsResults =
                        resultsResult.value.filter { result: Result -> result.type == "chords" }

                        val currentChordsState = chordsResultState.value
                        val chordsLastResult = chordsResults.last().score
                        val chordsBestResult = chordsResults.map {it.score.toInt()}.sorted().lastOrNull().toString()
//                            chordsResults.maxWithOrNull(Comparator.comparingInt { it.score.toInt() })!!.score
                        val chordsCompleteResult =
                            chordsResults.sumOf { result: Result -> result.score.toInt() }
                                .toString()
                        chordsResultState.value = currentChordsState?.copy(
                            chordsLastValue = chordsLastResult,
                            chordsBestValue = chordsBestResult,
                            chordsCompleteValue = chordsCompleteResult,
                            chordsErrorState = 1
                        )
                }
                catch(e: Exception) {
                    chordsResultState.value?.chordsErrorState = 0
                    println("No value for chords yet")
                }

                //rhythm fill
                try {
                    val rhythmResults =
                        resultsResult.value.filter { result: Result -> result.type == "rhythm" }
                        val currentRhythmState = rhythmResultState.value
                        val rhythmLastResult = rhythmResults.last().score
                        val rhythmBestResult = rhythmResults.map {it.score.toInt()}.sorted().lastOrNull().toString()
//                            rhythmResults.maxWithOrNull(Comparator.comparingInt { it.score.toInt() })!!.score
                        val rhythmCompleteResult =
                            rhythmResults.sumOf { result: Result -> result.score.toInt() }
                                .toString()
                        rhythmResultState.value = currentRhythmState?.copy(
                            rhythmLastValue = rhythmLastResult,
                            rhythmBestValue = rhythmBestResult,
                            rhythmCompleteValue = rhythmCompleteResult,
                            rhythmErrorState = 1
                        )
                    }
                catch(e: Exception) {
                    rhythmResultState.value?.rhythmErrorState = 0
                    println("No value for chords yet")
                }
                finalResultState.value = notesResultState.value?.notesCompleteValue!!.toInt() + chordsResultState.value?.chordsCompleteValue!!.toInt() + rhythmResultState.value?.rhythmCompleteValue!!.toInt()
            }
            is GeneralResult.Error -> errorState.value = GET_RESULTS_ERROR
        }
    }
}