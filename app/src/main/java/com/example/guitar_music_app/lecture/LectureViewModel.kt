package com.example.guitar_music_app.lecture

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.viewModelScope
import com.example.guitar_music_app.general.BaseViewModel
import com.example.guitar_music_app.login.LoginEvent
import com.google.common.io.Files.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.coroutines.CoroutineContext

class LectureViewModel(

    val lectureRepo: LectureRepository,
    uiContext: CoroutineContext
) : BaseViewModel<LectureEvent>(uiContext) {


    val state = MutableLiveData(State())

    val chordTextChange = MutableLiveData<String>()


    override fun handleEvent(event: LectureEvent) {
        when (event) {

            is LectureEvent.OnStart -> chordTextChange.value = randomChord().toString()
        }
    }

    data class State(
        var chord: Chord = Chord.C_DUR,
        val isChordValid: Boolean = false,
        val buttonsTouched: Set<ButtonState> = emptySet(),
        var chordPlayed: Boolean = false,
        var assistant: Boolean = false
    )


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


    private fun isChordValid(chord: Chord, buttons: Set<ButtonState>): Boolean {
        chord.notes.forEach {
//            return if (chord.notes.sortedBy { it.name } == buttons.map { it.note }.sortedBy { it.name }) {
            return if (chord.notes == buttons.map { it.note }.toSet()) {
                println("wellplayed")
                state.value?.chordPlayed = true
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
}
