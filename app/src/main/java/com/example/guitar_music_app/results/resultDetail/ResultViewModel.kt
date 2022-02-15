package com.example.guitar_music_app.results.resultDetail

import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.guitar_music_app.general.GET_RESULT_ERROR
import com.example.guitar_music_app.general.GeneralResult
import com.example.guitar_music_app.results.ResultRepository
import com.example.guitar_music_app.general.BaseViewModel
import com.example.guitar_music_app.results.Result
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat as SimpleDateFormat1

class ResultViewModel(
    val resultRepo: ResultRepository,
    uiContext: CoroutineContext
) : BaseViewModel<ResultDetailEvent>(uiContext) {

    private val resultState = MutableLiveData<Result>()
    val result: LiveData<Result> get() = resultState

    private val deletedState = MutableLiveData<Boolean>()
    val deleted: LiveData<Boolean> get() = deletedState

    private val updatedState = MutableLiveData<Boolean>()
    val updated: LiveData<Boolean> get() = updatedState

    @RequiresApi(Build.VERSION_CODES.N)
    override fun handleEvent(event: ResultDetailEvent) {
        when (event) {
            is ResultDetailEvent.OnStart -> getResult(event.resultId)
            is ResultDetailEvent.OnDeleteClick -> onDelete()
            is ResultDetailEvent.OnDoneClick -> updateResult(event.contents)
        }
    }

    private fun onDelete() = launch {
        val deleteGeneralResult = resultRepo.deleteResult(result.value!!)

        when (deleteGeneralResult) {
            is GeneralResult.Value -> deletedState.value = true
            is GeneralResult.Error -> deletedState.value = false
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getResult(resultId: String) = launch {
        if (resultId == "") newResult()
        else {
            val resultResult = resultRepo.getResultById(resultId)

            when (resultResult) {
                is GeneralResult.Value -> resultState.value = resultResult.value
                is GeneralResult.Error -> errorState.value = GET_RESULT_ERROR
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun newResult() {
        resultState.value =
            Result(getCalendarTime(), "", "chords", null)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun getCalendarTime(): String {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        val format = SimpleDateFormat1("d MMM yyyy HH:mm:ss Z")
//        format.timeZone = cal.timeZone
        return format.format(cal.time)
    }
}