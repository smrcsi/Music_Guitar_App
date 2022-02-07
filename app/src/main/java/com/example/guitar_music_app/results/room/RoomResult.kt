package com.example.guitar_music_app.results.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

//Room can autogenerate keys
@Entity(
    tableName = "results",
    indices = [Index("creation_date")]
)
data class RoomResult(
    @PrimaryKey
    @ColumnInfo(name = "creation_date")
    val creationDate: String,

    @ColumnInfo(name = "contents")
    val contents: String,

    @ColumnInfo(name = "up_votes")
    val upVotes: Int,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @ColumnInfo(name = "creator_id")
    val creatorId: String
)