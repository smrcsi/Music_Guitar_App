package com.example.guitar_music_app.general

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<T>(protected val uiContext: CoroutineContext) : ViewModel(),
    CoroutineScope {
    abstract fun handleEvent(event: T)
    //Generic type, <T> sealed trida ktera reprezentuje ruzne eventy ktere muzou propagovat z UI
    //Abstraktni trida, kterou rozsiruje UserViewModel a obsahuje vsechno spolecne pro ViewModely
   //Behem kazdeho launch je to rozsireni CoroutineScope tudiz ViewModelu
    protected lateinit var jobTracker: Job

    init {
        jobTracker = Job()
    }

    protected val loadingState = MutableLiveData<Unit>()
    val loading: LiveData<Unit> get() = loadingState


    protected val errorState = MutableLiveData<String>()
    val error: LiveData<String> get() = errorState

    override val coroutineContext: CoroutineContext
    get() = uiContext + jobTracker
}