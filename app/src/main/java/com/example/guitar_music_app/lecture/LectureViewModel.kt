package com.example.guitar_music_app.lecture

import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.guitar_music_app.general.BaseViewModel
import com.example.guitar_music_app.general.GeneralResult
import com.example.guitar_music_app.results.Result
import com.example.guitar_music_app.results.ResultRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class LectureViewModel(

    val resultRepo: ResultRepository,
    uiContext: CoroutineContext
) : BaseViewModel<LectureEvent>(uiContext) {

    //Na prevedeni do intu
    private var intResult = 0
    private var intChordNumber = 0

    val state = MutableLiveData(State())

    val chordTextChange = MutableLiveData<String>()

//    val noteState = MutableLiveData(NoteState())

    val rhythmState = MutableLiveData(RhythmState())

    private val resultState = MutableLiveData<Result>()
    val result: LiveData<Result> get() = resultState

    private val updatedState = MutableLiveData<Boolean>()
    val updated: LiveData<Boolean> get() = updatedState

    val chordsNumber = MutableLiveData<Int>()


    @RequiresApi(Build.VERSION_CODES.N)
    override fun handleEvent(event: LectureEvent) {
        when (event) {
            is LectureEvent.OnStart -> {
                chordTextChange.value = randomChord().toString()
                newResult()
                chordsNumber.value = intChordNumber
            }
            is LectureEvent.OnDoneClick -> {
                updateResult(event.contents)
                println("Uspech" + event.contents)
            }
        }
    }

    data class State(
        var chord: Chord = Chord.C_DUR,
        val isChordValid: Boolean = false,
        val buttonsTouched: Set<ButtonState> = emptySet(),
        var chordPlayed: Boolean = false,
        var assistant: Boolean = false,
    )

//    data class NoteState(
//        var targetNote: Note = Note.G,
//        var note: Note = Note.G,
//        var isNoteValid: Boolean = false,
//        var notePlayed: Boolean = false,
//        val notesTouched: Set<ButtonState> = emptySet()
//    )

    data class RhythmState(
        var isFlingUpValid: Boolean = false
    )


    @RequiresApi(Build.VERSION_CODES.N)
    fun buttonTouched(note: Note, touched: Boolean) {
        val currentState = state.value!!
        val mutableList = currentState.buttonsTouched.toMutableSet()
        val exists = mutableList.any { it.note == note }
        if (touched && !exists) {
            mutableList.add(ButtonState(note))
        } else if (!touched && exists) {
            mutableList.remove(ButtonState(note))
        }
        if (mutableList.isEmpty() && state.value!!.chordPlayed) {
            chordTextChange.value = randomChord().toString()
            state.value!!.chordPlayed = false
        }
        val isChordValid = isChordValid(currentState.chord, mutableList)
        state.value = currentState.copy(isChordValid = isChordValid, buttonsTouched = mutableList)
        //TODO- Zkontrolovat jestli je potreba duplikovat cyklus
        if (mutableList.isEmpty() && state.value!!.chordPlayed) {
            chordTextChange.value = randomChord().toString()
            state.value!!.chordPlayed = false
        }

    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun isChordValid(chord: Chord, buttons: Set<ButtonState>): Boolean {
        chord.notes.forEach { _ ->
//            return if (chord.notes.sortedBy { it.name } == buttons.map { it.note }.sortedBy { it.name }) {
            return if (chord.notes == buttons.map { it.note }.toSet()) {
                println("wellplayed")
                addToResult()
                intChordNumber += 1
                state.value?.chordPlayed = true
                chordsNumber.value = intChordNumber
                true
            } else {
                false
            }
        }
        return isChordValid(chord, buttons)
    }

    private fun randomChord(): Chord {
        val pick = Random().nextInt(Chord.values().size)
        val chord = Chord.values()[pick]
        state.value = state.value?.copy(chord = chord)

        println("what" + state.value?.chord)
        return chord
    }

    fun assistantSet() {
        if (state.value?.assistant == false) {
            state.value = state.value?.copy(assistant = true)
        } else {
            state.value = state.value?.copy(assistant = false)
        }
    }

//    fun noteTouched(note: Note, touched: Boolean) {
//        val currentState = noteState.value!!
//        val mutableList = currentState.notesTouched.toMutableSet()
//        val exists = mutableList.any { it.note == note }
//        if (touched && !exists) {
//            mutableList.add(ButtonState(note))
//        } else if (!touched && exists) {
//            mutableList.remove(ButtonState(note))
//        }
//        if (currentState.targetNote == note && touched && !currentState.notePlayed) {
//            currentState.targetNote = randomNote()
//            noteState.value =
//                currentState.copy(isNoteValid = true, note = currentState.note, notePlayed = touched, targetNote = currentState.targetNote)
//        } else {
//            if (noteState.value?.notePlayed == true) {
//
//                noteState.value = currentState.copy(isNoteValid = false, notesTouched = mutableList, notePlayed = touched, note = currentState.targetNote)
//            }
//            else {
//                noteState.value = currentState.copy(isNoteValid = false, notesTouched = mutableList)
//            }
//        }
//    }
//
//    private fun randomNote(): Note {
//        val pick = Random().nextInt(Note.values().size)
//        return Note.values()[pick]
//    }

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
    @RequiresApi(Build.VERSION_CODES.N)
    private fun addToResult() {
        intResult += if (state.value?.assistant == true) {
            1
        } else {

            2
        }

        resultState.value =
            Result(getCalendarTime(), intResult.toString(), "chords", null)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun newResult() {
        resultState.value =
            Result(getCalendarTime(), intResult.toString(), "chords", null)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getCalendarTime(): String {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        val format = SimpleDateFormat("d MMM yyyy HH:mm:ss Z")
//       format.timeZone = cal.timeZone
        return format.format(cal.time)
    }
}
