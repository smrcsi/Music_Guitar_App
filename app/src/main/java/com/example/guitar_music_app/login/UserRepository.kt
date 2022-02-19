package com.example.guitar_music_app.login

import com.example.guitar_music_app.general.User
import com.example.guitar_music_app.general.GeneralResult

interface UserRepository {
    //Exception pro opravdove chyby; ? i kdyz neni uzivatel proted registrovan
    suspend fun getCurrentUser(): GeneralResult<Exception, User?>

    suspend fun signOutCurrentUser(): GeneralResult<Exception, Unit>

    suspend fun signInGoogleUser(idToken: String): GeneralResult<Exception, Unit>
}