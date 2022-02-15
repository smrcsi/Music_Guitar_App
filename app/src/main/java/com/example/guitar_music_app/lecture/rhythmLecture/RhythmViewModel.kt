package com.example.guitar_music_app.lecture.rhythmLecture

import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.guitar_music_app.general.BaseViewModel
import com.example.guitar_music_app.general.GeneralResult
import com.example.guitar_music_app.lecture.ButtonState
import com.example.guitar_music_app.lecture.LectureEvent
import com.example.guitar_music_app.lecture.Note
import com.example.guitar_music_app.lecture.chordLecture.ChordsViewModel
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
            Result(getCalendarTime(), intResult.toString(), "notes", null)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun newResult() {
        resultState.value =
            Result(getCalendarTime(), intResult.toString(), "notes", null)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getCalendarTime(): String {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        val format = SimpleDateFormat("d MMM yyyy HH:mm:ss Z")
//       format.timeZone = cal.timeZone
        return format.format(cal.time)
    }


}
