package com.example.guitar_music_app.lecture.rhythmLecture

import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Build
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


    private val _uiState = MutableLiveData(UiState(RhythmType.RHYTHM_1, RhythmType.RHYTHM_1.directions.map {
        UiState.Fling(
            it,
            UiState.FlingState.START
        )
    }))
    val uiState: LiveData<UiState> get() = _uiState


    val slidesNumber = MutableLiveData<Int>()

    val rhythmState = MutableLiveData(RhythmState())

    @RequiresApi(Build.VERSION_CODES.N)
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

    data class RhythmState(
        var isFlingUpValid: Boolean = false
    )

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

    @RequiresApi(Build.VERSION_CODES.N)
    fun addToResult() {
        intResult += 5
        resultState.value =
            Result(getCalendarTime(), intResult.toString(), "rhythm", null)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun newResult() {
        resultState.value =
            Result(getCalendarTime(), intResult.toString(), "rhythm", null)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getCalendarTime(): String {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        val format = SimpleDateFormat("d MMM yyyy HH:mm:ss Z")
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
        when {
            currentState.flings.all { it.state == UiState.FlingState.VALID } -> {
                _uiState.value = UiState(currentState.rhythmType, successMessage = "jupii!")
            }
            isValidFling -> {
                _uiState.value = currentState.copy(flings = mappedFlings, successMessage = null, errorMessage = null, tryAgain = false)
            }
            else -> {
                // TODO zobraz neco
                _uiState.value = UiState(currentState.rhythmType, errorMessage = "posralo se to")
            }
        }
    }

    fun changeRhythm(rhythmType: RhythmType) {
        _uiState.value = UiState(rhythmType)
    }

    fun onIncorrect() {
        _uiState.value = _uiState.value?.copy(tryAgain = true)
    }

    data class UiState(val rhythmType: RhythmType, val flings: List<Fling> = rhythmType.directions.map {
        Fling(
            it,
            FlingState.START
        )
    }, val errorMessage: String? = null, val successMessage: String? = null, val tryAgain: Boolean = false) {
        data class Fling(val direction: Direction, val state: FlingState)
        enum class Direction {
            UP, DOWN
        }

        enum class FlingState {
            START, VALID, INVALID
        }
    }

    enum class RhythmType(val directions: List<UiState.Direction>) {
        RHYTHM_1(listOf(UP, DOWN, UP))
    }
}
