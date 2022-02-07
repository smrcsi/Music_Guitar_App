package com.example.guitar_music_app.lecture

import com.example.guitar_music_app.general.BaseViewModel
import kotlin.coroutines.CoroutineContext

class NotesViewModel(    val lectureRepo: LectureRepository,
                          uiContext: CoroutineContext
) : BaseViewModel<LectureEvent>(uiContext) {
    override fun handleEvent(event: LectureEvent) {
        TODO("Not yet implemented")
    }

}
