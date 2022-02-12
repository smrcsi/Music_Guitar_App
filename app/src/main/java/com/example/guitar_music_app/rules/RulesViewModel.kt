package com.example.guitar_music_app.rules

import androidx.lifecycle.MutableLiveData
import com.example.guitar_music_app.R
import com.example.guitar_music_app.general.BaseViewModel
import com.example.guitar_music_app.lecture.LectureEvent
import com.example.guitar_music_app.lecture.LectureRepository
import com.example.guitar_music_app.lecture.LectureViewModel
import com.example.guitar_music_app.results.ResultRepository
import com.example.guitar_music_app.results.resultList.ResultListEvent
import kotlin.coroutines.CoroutineContext
import android.content.res.loader.ResourcesProvider

import android.R.attr.text




class RulesViewModel (

    val resultRepo: ResultRepository,
    uiContext: CoroutineContext
    ) : BaseViewModel<ResultListEvent>(uiContext) {
    override fun handleEvent(event: ResultListEvent) {
        TODO("Not yet implemented")
    }









}

