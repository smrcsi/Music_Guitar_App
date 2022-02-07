package com.example.guitar_music_app.results

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.guitar_music_app.R

class ResultActivity : AppCompatActivity() {
    private lateinit var nav: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity)
        nav = Navigation.findNavController(this, R.id.fragment_navigation)
    }
}