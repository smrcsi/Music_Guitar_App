package com.example.guitar_music_app.lecture.chordLecture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.guitar_music_app.general.BaseViewModel
import com.example.guitar_music_app.general.GeneralResult
import com.example.guitar_music_app.lecture.ButtonState
import com.example.guitar_music_app.lecture.Chord
import com.example.guitar_music_app.lecture.LectureEvent
import com.example.guitar_music_app.lecture.Note
import com.example.guitar_music_app.results.Result
import com.example.guitar_music_app.results.ResultRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class ChordsViewModel(

    val resultRepo: ResultRepository,
    uiContext: CoroutineContext
) : BaseViewModel<LectureEvent>(uiContext) {

    //Na prevedeni do intu
    private var intResult = 0
    private var intChordNumber = 0

    val state = MutableLiveData(State())

    private val resultState = MutableLiveData<Result>()
    val result: LiveData<Result> get() = resultState

    private val updatedState = MutableLiveData<Boolean>()
    val updated: LiveData<Boolean> get() = updatedState

    val chordsNumber = MutableLiveData<Int>()


    override fun handleEvent(event: LectureEvent) {
        when (event) {
            is LectureEvent.OnStart -> {
                val randomChord = randomChord()
                state.value = state.value?.copy(chord = randomChord)
                newResult()
                chordsNumber.value = intChordNumber
            }
            is LectureEvent.OnDoneClick -> {
                updateResult(event.contents)
                println("Uspech" + event.contents)
            }
            is LectureEvent.OnLectureItemClick -> TODO()
            LectureEvent.OnNewLectureClick -> TODO()
        }
    }

    data class State(
        val chord: Chord = Chord.C_DUR,
        val isChordValid: Boolean = false,
        val buttonsTouched: Set<ButtonState> = emptySet(),
        val chordPlayed: Boolean = false,
        val assistant: Boolean = false
    )


    fun buttonTouched(note: Note, touched: Boolean) {
        val currentState = state.value!!
        val updatesTouchedButtons = currentState.buttonsTouched.toMutableSet()
        val exists = updatesTouchedButtons.any { it.note == note }
        if (touched && !exists) {
            updatesTouchedButtons.add(ButtonState(note))
        } else if (!touched && exists) {
            updatesTouchedButtons.remove(ButtonState(note))
        } else {
            return
        }
        val isChordValid = isChordValid(currentState.chord, updatesTouchedButtons)
        var newChord: Chord? = null
        val chordPlayed = if (isChordValid) {
            true
        } else if (updatesTouchedButtons.isEmpty() && currentState.chordPlayed) {
            newChord = randomChord()
            false
        } else {
            currentState.chordPlayed
        }
        state.value = currentState.copy(
            isChordValid = isChordValid,
            buttonsTouched = updatesTouchedButtons,
            chordPlayed = chordPlayed,
            chord = newChord ?: currentState.chord
        )

    }


    private fun isChordValid(chord: Chord, buttons: Set<ButtonState>): Boolean {
        val isValid = buttons.size == chord.notes.size && buttons.map { it.note }.containsAll(chord.notes)
        if (isValid) {
            println("wellplayed")
            addToResult()
            intChordNumber += 1
            chordsNumber.value = intChordNumber
        }
        return isValid
    }

    private fun randomChord(): Chord {
        val pick = Random().nextInt(Chord.values().size)
        val chord = Chord.values()[pick]
        println("what" + state.value?.chord)
        return chord
    }

    fun assistantSet(isChecked: Boolean) {
        val assistant = state.value?.assistant
        if (assistant == false && isChecked) {
            state.value = state.value?.copy(assistant = true)
        } else if (assistant == true && !isChecked) {
            state.value = state.value?.copy(assistant = false)
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

    //TODO-Jede to mnoooohokrat
    private fun addToResult() {
        intResult += if (state.value?.assistant == true) {
            1
        } else {
            2
        }
        resultState.value =
            Result(getCalendarTime(), intResult.toString(), "chords", null)
    }


    private fun newResult() {
        resultState.value =
            Result(getCalendarTime(), intResult.toString(), "chords", null)
    }

    private fun getCalendarTime(): String {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        val format = SimpleDateFormat("d MMM yyyy HH:mm:ss Z", Locale.getDefault())
//       format.timeZone = cal.timeZone
        return format.format(cal.time)
    }
}
