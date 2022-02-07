package com.example.guitar_music_app.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.example.guitar_music_app.R
import com.google.firebase.FirebaseApp

private const val LOGIN_VIEW = "LOGIN_VIEW"

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        FirebaseApp.initializeApp(applicationContext)

        val view = supportFragmentManager.findFragmentByTag(LOGIN_VIEW) as LoginView?
            ?: LoginView()

        supportFragmentManager.beginTransaction()
            .replace(R.id.root_activity_login, view, LOGIN_VIEW)
            .commitNowAllowingStateLoss()
    }
}