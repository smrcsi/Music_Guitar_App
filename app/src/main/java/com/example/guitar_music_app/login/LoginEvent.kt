package com.example.guitar_music_app.login

sealed class LoginEvent<out T> {
    //Reprezentuje různé eventy, které mohou přijít z určité View nebo setu View
    object OnAuthButtonClick : LoginEvent<Nothing>()
    object OnStart : LoginEvent<Nothing>()
    data class OnGoogleSignInResult<out LoginResult>(val result:
    LoginResult) : LoginEvent<LoginResult>()

}