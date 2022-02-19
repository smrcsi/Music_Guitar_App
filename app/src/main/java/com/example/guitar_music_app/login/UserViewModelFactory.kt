package com.example.guitar_music_app.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers

class UserViewModelFactory(
    private val userRepo: UserRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return UserViewModel(userRepo, Dispatchers.Main) as T
    }

}