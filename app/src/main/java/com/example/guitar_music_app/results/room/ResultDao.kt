package com.example.guitar_music_app.results.room

import androidx.room.*

//Funkce na upravu dat v tabulce
//Dao = data access object
@Dao
interface ResultDao {
    @Query("SELECT * FROM results")
    suspend fun getResults(): List<RoomResult>

    @Query("SELECT * FROM results WHERE creation_date= :creationDate")
    suspend fun getResultById(creationDate: String): RoomResult

    @Delete
    suspend fun  deleteResult(result: RoomResult)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insertOrUpdateResult(result: RoomResult): Long
}