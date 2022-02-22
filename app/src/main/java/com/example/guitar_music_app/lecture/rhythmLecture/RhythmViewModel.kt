package com.example.guitar_music_app.lecture.rhythmLecture


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.guitar_music_app.general.BaseViewModel
import com.example.guitar_music_app.general.GeneralResult
import com.example.guitar_music_app.lecture.LectureEvent
import com.example.guitar_music_app.lecture.rhythmLecture.RhythmViewModel.UiState.Direction.*
import com.example.guitar_music_app.results.Result
import com.example.guitar_music_app.results.ResultRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext


class RhythmViewModel(
    val resultRepo: ResultRepository,
    uiContext: CoroutineContext
) : BaseViewModel<LectureEvent>(uiContext) {

    private var intResult = 0
    private var intSlidesNumber = 0


    private val resultState = MutableLiveData<Result>()
    val result: LiveData<Result> get() = resultState

    private val updatedState = MutableLiveData<Boolean>()
    val updated: LiveData<Boolean> get() = updatedState


    private val _uiState =
        MutableLiveData(UiState(RhythmType.FREE_RHYTHM, RhythmType.FREE_RHYTHM.directions.map {
            UiState.Fling(
                it,
                UiState.FlingState.START
            )
        }))
    val uiState: LiveData<UiState> get() = _uiState


    val slidesNumber = MutableLiveData<Int>()

    override fun handleEvent(event: LectureEvent) {
        when (event) {
            is LectureEvent.OnStart -> {
                newResult()
                slidesNumber.value = intSlidesNumber
            }
            is LectureEvent.OnDoneClick -> {
                updateResult(event.contents)
                println("Uspech" + event.contents)
            }
        }
    }

    private fun updateResult(contents: String) = launch {
        val updateGeneralResult = resultRepo.updateResult(
            result.value!!
                .copy(score = contents)
        )

        when (updateGeneralResult) {
            is GeneralResult.Value -> updatedState.value = true
            is GeneralResult.Error -> updatedState.value = false
        }
    }


    fun addToResult() {
        intResult += 5
        resultState.value =
            Result(getCalendarTime(), intResult.toString(), "rhythm", null)
    }


    private fun newResult() {
        resultState.value =
            Result(getCalendarTime(), intResult.toString(), "rhythm", null)
    }

    private fun getCalendarTime(): String {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        val format = SimpleDateFormat("d MMM yyyy HH:mm:ss Z", Locale.getDefault())
//       format.timeZone = cal.timeZone
        return format.format(cal.time)
    }

    fun onFling(direction: UiState.Direction) {
        val currentState = _uiState.value
        var flingFound = false
        var isValidFling = true
        val mappedFlings = currentState!!.flings.map {
            if (flingFound || it.state != UiState.FlingState.START) {
                it
            } else {
                flingFound = true
                if (it.direction == direction) {
                    it.copy(state = UiState.FlingState.VALID)
                } else {
                    isValidFling = false
                    it.copy(state = UiState.FlingState.INVALID)
                }
            }
        }
        Log.d("vojta", "Updating view model on fling")
        when {
            currentState.flings.all { it.state == UiState.FlingState.VALID } -> {
                Log.d("vojta", "State je validni")
                _uiState.value = UiState(currentState.rhythmType, successMessage = "jupii!")
            }
            isValidFling -> {
                Log.d("vojta", "Kopiruju updatly flingy")
                _uiState.value = currentState.copy(
                    flings = mappedFlings,
                    successMessage = null,
                    errorMessage = null,
                    tryAgain = false
                )
            }
            else -> {
                // TODO zobraz neco
                _uiState.value = UiState(currentState.rhythmType, errorMessage = "posralo  se to")
            }
        }
    }

    fun changeRhythm(rhythmType: RhythmType) {
        _uiState.value = UiState(rhythmType)
    }

    fun onIncorrect() {
        _uiState.value = _uiState.value?.copy(tryAgain = true)
    }

    data class UiState(
        val rhythmType: RhythmType,
        val flings: List<Fling> = rhythmType.directions.map {
            Fling(
                it,
                FlingState.START
            )
        },
        val errorMessage: String? = null,
        val successMessage: String? = null,
        val tryAgain: Boolean = false
    ) {
        data class Fling(val direction: Direction, val state: FlingState)
        enum class Direction {
            UP, DOWN
        }

        enum class FlingState {
            START, VALID, INVALID
        }
    }

    enum class RhythmType(val directions: List<UiState.Direction>) {
        FREE_RHYTHM(listOf(DOWN)),
        RHYTHM_1(listOf(DOWN, DOWN, DOWN, UP, DOWN)),
        RHYTHM_2(listOf(DOWN, DOWN, UP, DOWN, UP)),
        RHYTHM_3(listOf(DOWN, UP, DOWN, UP, DOWN)),
        RHYTHM_4(listOf(UP, UP, DOWN, UP, DOWN))
    }
}
