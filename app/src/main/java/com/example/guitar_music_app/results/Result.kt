package com.example.guitar_music_app.results

import com.example.guitar_music_app.general.User

data class Result(val creationDate:String,
                val contents:String,
                val upVotes: Int,
                val imageUrl: String,
                val creator: User?)