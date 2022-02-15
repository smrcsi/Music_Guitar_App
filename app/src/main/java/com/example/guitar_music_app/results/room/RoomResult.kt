package com.example.guitar_music_app.results.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.guitar_music_app.general.User

//Room can autogenerate keys
@Entity(
    tableName = "results",
    indices = [Index("lecture_date")]
)
data class RoomResult(
    @PrimaryKey
    @ColumnInfo(name = "lecture_date")
    val lectureDate: String,


    @ColumnInfo(name = "score")
    val score: String,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "creator_id")
    val creatorId: String


)