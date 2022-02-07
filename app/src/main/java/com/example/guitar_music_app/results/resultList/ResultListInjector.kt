package com.example.guitar_music_app.results.resultList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.guitar_music_app.results.ResultListViewModelFactory
import com.example.guitar_music_app.results.ResultRepoImpl
import com.example.guitar_music_app.results.ResultRepository
import com.example.guitar_music_app.results.room.RoomResult
import com.example.guitar_music_app.results.room.RoomResultDatabase
import com.google.firebase.FirebaseApp

class ResultListInjector(application: Application): AndroidViewModel(application) {
    private fun getResultRepository(): ResultRepository {
        FirebaseApp.initializeApp(getApplication())
        return ResultRepoImpl(
            local = RoomResultDatabase.getInstance(getApplication()).roomResultDao())
    }

    fun provideResultListViewModelFactory(): ResultListViewModelFactory =
        ResultListViewModelFactory(
            getResultRepository()
        )
}