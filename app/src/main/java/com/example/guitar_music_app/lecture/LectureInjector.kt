package com.example.guitar_music_app.lecture

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.guitar_music_app.results.ResultRepoImpl
import com.example.guitar_music_app.results.ResultRepository
import com.example.guitar_music_app.results.resultDetail.ResultViewModelFactory
import com.example.guitar_music_app.results.room.RoomResultDatabase
import com.google.firebase.FirebaseApp

class LectureInjector(application: Application): AndroidViewModel(application) {
    private fun getLectureRepository(): LectureRepository {
        FirebaseApp.initializeApp(getApplication())
        return LectureRepoImpl(
            local = RoomResultDatabase.getInstance(getApplication()).roomResultDao())
    }

    fun provideLectureViewModelFactory(): LectureViewModelFactory =
        LectureViewModelFactory(
            getLectureRepository()
        )
}