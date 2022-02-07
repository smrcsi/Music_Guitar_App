package com.example.guitar_music_app.lecture

import com.example.guitar_music_app.general.GeneralResult
import com.example.guitar_music_app.results.Result

interface LectureRepository {
    suspend fun getResultById(resultId: String): GeneralResult<Exception, Result>
    suspend fun getResults(): GeneralResult<Exception, List<Result>>
    suspend fun deleteResult(result: Result): GeneralResult<Exception, Unit>
    suspend fun updateResult(result: Result): GeneralResult<Exception, Unit>
}