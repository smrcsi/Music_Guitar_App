package com.example.guitar_music_app.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.guitar_music_app.firebase.FirebaseUserRepo
import com.google.firebase.FirebaseApp

class LoginInjector(application: Application): AndroidViewModel(application) {
    init {
        FirebaseApp.initializeApp(application)
    }
    private fun getUserRepository(): UserRepository {
        return FirebaseUserRepo()
    }
    fun provideUserViewModelFactory(): UserViewModelFactory =
        UserViewModelFactory(
            getUserRepository()
        )

}