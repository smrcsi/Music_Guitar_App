package com.example.guitar_music_app.rules

import com.example.guitar_music_app.general.BaseViewModel
import com.example.guitar_music_app.results.ResultRepository
import com.example.guitar_music_app.results.resultList.ResultListEvent
import kotlin.coroutines.CoroutineContext


class RulesViewModel (

    val resultRepo: ResultRepository,
    uiContext: CoroutineContext
    ) : BaseViewModel<ResultListEvent>(uiContext) {
    override fun handleEvent(event: ResultListEvent) {
        TODO("Not yet implemented")
    }









}

