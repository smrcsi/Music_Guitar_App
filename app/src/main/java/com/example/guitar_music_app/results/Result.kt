package com.example.guitar_music_app.results

import com.example.guitar_music_app.general.User

data class Result(
    val creationDate:String,
    val score: String,
    val type: String,
    val creator: User?
)