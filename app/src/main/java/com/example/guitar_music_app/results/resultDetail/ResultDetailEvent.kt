package com.example.guitar_music_app.results.resultDetail

sealed class ResultDetailEvent {
    data class OnDoneClick(val contents: String) : ResultDetailEvent()
    object OnDeleteClick : ResultDetailEvent()
    object OnDeleteConfirmed : ResultDetailEvent()
    data class OnStart(val resultId: String) : ResultDetailEvent()
}