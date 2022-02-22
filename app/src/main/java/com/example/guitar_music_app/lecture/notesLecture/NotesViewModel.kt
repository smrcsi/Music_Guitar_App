package com.example.guitar_music_app.lecture.notesLecture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.guitar_music_app.general.BaseViewModel
import com.example.guitar_music_app.general.GeneralResult
import com.example.guitar_music_app.lecture.ButtonState
import com.example.guitar_music_app.lecture.LectureEvent
import com.example.guitar_music_app.lecture.Note
import com.example.guitar_music_app.results.Result
import com.example.guitar_music_app.results.ResultRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class NotesViewModel(
    val resultRepo: ResultRepository,
    uiContext: CoroutineContext
) : BaseViewModel<LectureEvent>(uiContext) {

    private var intResult = 0
    private var intNotesNumber = 0


    private val resultState = MutableLiveData<Result>()
    val result: LiveData<Result> get() = resultState

    private val updatedState = MutableLiveData<Boolean>()
    val updated: LiveData<Boolean> get() = updatedState

    val noteState = MutableLiveData(UiState())

    val notesNumber = MutableLiveData<Int>()



    override fun handleEvent(event: LectureEvent) {
        when (event) {
            is LectureEvent.OnStart -> {
                newResult()
                notesNumber.value = intNotesNumber
            }
            is LectureEvent.OnDoneClick -> {
                updateResult(event.contents)
                println("Uspech" + event.contents)
            }
        }
    }

    data class UiState(
        val targetNote: Note = Note.G,
        val targetAccomplished: Boolean = false,
        val notesTouched: Set<ButtonState> = emptySet()
    )


    fun noteTouched(note: Note, touched: Boolean) {
        val currentState = noteState.value!!
        val updatesTouchedNotes = currentState.notesTouched.toMutableSet()
        val exists = updatesTouchedNotes.any { it.note == note }
        if (touched && !exists) {
            updatesTouchedNotes.add(ButtonState(note))
        } else if (!touched && exists) {
            updatesTouchedNotes.remove(ButtonState(note))
        } else {
            return
        }
        if (updatesTouchedNotes.isEmpty() && currentState.targetAccomplished) {
            noteState.value = UiState(randomNote(), false, emptySet())
        } else if (!currentState.targetAccomplished && updatesTouchedNotes.any { it.note == currentState.targetNote }) {
            addToResult()
            intNotesNumber += 1
            notesNumber.value = intNotesNumber
            noteState.value = currentState.copy(targetAccomplished = true, notesTouched = updatesTouchedNotes)
        } else {
            noteState.value = currentState.copy(notesTouched = updatesTouchedNotes)
        }
    }

    private fun randomNote(): Note {
        val pick = Random().nextInt(Note.values().size)
        return Note.values()[pick]
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


    private fun addToResult() {
        intResult += 5
        resultState.value =
            Result(getCalendarTime(), intResult.toString(), "notes", null)
    }



    private fun newResult() {
        resultState.value =
            Result(getCalendarTime(), intResult.toString(), "notes", null)
    }


    private fun getCalendarTime(): String {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        val format = SimpleDateFormat("d MMM yyyy HH:mm:ss Z", Locale.getDefault())
//       format.timeZone = cal.timeZone
        return format.format(cal.time)
    }


}
