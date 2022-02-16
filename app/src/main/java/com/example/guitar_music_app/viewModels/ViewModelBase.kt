package com.example.guitar_music_app.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

//Mluvi o tom ve videu 7

abstract class BaseViewModel<T>(protected val uiContext: CoroutineContext) : ViewModel(), CoroutineScope {
    abstract fun handleEvent(event: T)
    //cancellation
    private var jobTracker : Job = Job()

    //suggestion from Al Warren: to promote encapsulation and immutability, hide the MutableLiveData objects behind
    //LiveData references:
    private val errorState = MutableLiveData<String>()
    val error: LiveData<String> get() = errorState

    private val loadingState = MutableLiveData<Unit>()
    val loading: LiveData<Unit> get() = loadingState

    override val coroutineContext: CoroutineContext
        get() = uiContext + jobTracker

}