package com.example.guitar_music_app.lecture.notesLecture

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.guitar_music_app.results.ResultRepoImpl
import com.example.guitar_music_app.results.ResultRepository
import com.example.guitar_music_app.results.room.RoomResultDatabase
import com.google.firebase.FirebaseApp

class NotesInjector(application: Application): AndroidViewModel(application) {
    private fun getResultRepository(): ResultRepository {
        FirebaseApp.initializeApp(getApplication())
        return ResultRepoImpl(
            local = RoomResultDatabase.getInstance(getApplication()).roomResultDao())
    }

    fun provideNotesViewModelFactory(): NotesViewModelFactory =
        NotesViewModelFactory(
            getResultRepository()
        )


}