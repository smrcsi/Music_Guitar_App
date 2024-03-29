package com.example.guitar_music_app.lecture.rhythmLecture

import android.view.GestureDetector
import android.view.MotionEvent
import java.lang.Exception
import kotlin.math.abs



class MyGestureListener(private val viewModel: RhythmViewModel) : GestureDetector.SimpleOnGestureListener() {
    private val swipeMinDistance = 120
    private val swipeMaxOffPath = 250
    private val swipeThresholdVelocity = 200
    var slides = 0

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
//        val currentState = viewModel.rhythmState.value
        var result = false
        var direction: RhythmViewModel.UiState.Direction? = null
        try {
            if (e1 != null && e2 != null) {
                if (abs(e1.x - e2.x) > swipeMaxOffPath) {

                    println("mimo drahu")
                    result = false
                }
                if (e1.y - e2.y > swipeMinDistance
                    && abs(velocityX) > swipeThresholdVelocity
                ) {
                    println("nahoru")
                    slides += 1
                    direction = RhythmViewModel.UiState.Direction.UP
                    result = true

                }
                else if (e2.y - e1.y > swipeMinDistance
                    && abs(velocityX) > swipeThresholdVelocity
                ) {
                    println("dolu")
                    slides += 1
                    direction = RhythmViewModel.UiState.Direction.DOWN
                    result = false
                }
            }
        }
        catch (e: Exception) {
            println("nevyslo to")
        }
        direction?.let {
            viewModel.onFling(it)
        } ?: viewModel.onIncorrect()
        viewModel.slidesNumber.value = slides
        return result
    }
}
