package com.example.guitar_music_app.lecture

sealed class LectureEvent {
    data class OnDoneClick(val contents: String) : LectureEvent()
    data class OnLectureItemClick(val position: Int) : LectureEvent()
    object OnNewLectureClick : LectureEvent()
    object OnStart : LectureEvent()
}