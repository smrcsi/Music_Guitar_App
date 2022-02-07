package com.example.guitar_music_app.results.resultList

sealed class ResultListEvent {
    data class OnResultItemClick(val position: Int) : ResultListEvent()
    object OnNewResultClick : ResultListEvent()
    object OnStart : ResultListEvent()
}