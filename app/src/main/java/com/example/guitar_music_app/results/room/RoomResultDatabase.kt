package com.example.guitar_music_app.results.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DATABASE = "results"

@Database(
    entities = [RoomResult::class],
    version = 1,
    exportSchema = false
)
abstract class RoomResultDatabase : RoomDatabase() {

    abstract fun roomResultDao(): ResultDao

    //code below courtesy of https://github.com/googlesamples/android-sunflower; it     is open
    //source just like this application.
    companion object {

        // For Singleton instantiation
        @Volatile //Vzdy current instance
        private var instance: RoomResultDatabase? = null

        fun getInstance(context: Context): RoomResultDatabase {
            return instance
                ?: synchronized(this) { //Jestli vlakno vstoupi, zamkne se a ostatni nemuzou, takze databaze neni vytvorena vicekrat nez 1
                    instance
                        ?: buildDatabase(context).also { instance = it }
                }
        }

        private fun buildDatabase(context: Context): RoomResultDatabase {
            return Room.databaseBuilder(context, RoomResultDatabase::class.java, DATABASE)
                .build()
        }
    }
}