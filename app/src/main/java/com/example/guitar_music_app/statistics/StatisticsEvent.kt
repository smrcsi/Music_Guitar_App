package com.example.guitar_music_app.statistics

sealed class StatisticsEvent {
    data class OnDoneClick(val contents: String) : StatisticsEvent()
    object OnStart: StatisticsEvent()
}