package com.example.guitar_music_app.rules

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.guitar_music_app.menus.HomepageViewModelFactory
import com.example.guitar_music_app.results.ResultRepoImpl
import com.example.guitar_music_app.results.ResultRepository
import com.example.guitar_music_app.results.room.RoomResultDatabase
import com.google.firebase.FirebaseApp

class RulesInjector(application: Application) : AndroidViewModel(application) {

    private fun getRulesRepository(): ResultRepository {

        FirebaseApp.initializeApp(getApplication())
        return ResultRepoImpl(
            local = RoomResultDatabase.getInstance(getApplication()).roomResultDao()
        )
    }

    fun provideRulesViewModelFactory(): RulesViewModelFactory =
        RulesViewModelFactory(
            getRulesRepository()
        )

}