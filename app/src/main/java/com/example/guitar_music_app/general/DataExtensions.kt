package com.example.guitar_music_app.general

import android.text.Editable
import com.example.guitar_music_app.results.room.RoomResult
import com.example.guitar_music_app.firebase.FirebaseResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import com.example.guitar_music_app.results.Result

internal suspend fun <T> awaitTaskResult(task: Task<T>): T = suspendCoroutine { continuation ->
    task.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(task.result!!)
        } else {
            continuation.resumeWithException(task.exception!!)
        }
    }
}

//Wraps Firebase/GMS calls
internal suspend fun <T> awaitTaskCompletable(task: Task<T>): Unit = suspendCoroutine { continuation ->
    task.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(Unit)
        } else {
            continuation.resumeWithException(task.exception!!)
        }
    }
}

internal val FirebaseUser.toUser: User
    get() = User(
        userid = this.uid,
        name = this.displayName ?: ""
    )

internal val FirebaseResult.toResult: Result
    get() = Result(
        this.creationDate ?: "",
        this.score,
        this.type,
        User(this.creator ?: "")
    )

internal val Result.toFirebaseResult: FirebaseResult
    get() = FirebaseResult(
        this.creationDate,
        this.score,
        this.type,
        this.safeGetUid
    )

internal val RoomResult.toResult: Result
    get() = Result(
        this.lectureDate,
        this.score,
        this.type,
        User(this.creatorId)
    )

internal val Result.toRoomResult: RoomResult
    get() = RoomResult(
        this.creationDate,
        this.score,
        this.type,
        this.safeGetUid
    )

internal fun List<RoomResult>.toResultListFromRoomResult(): List<Result> = this.flatMap {
    listOf(it.toResult)
}

internal fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

internal val Result.safeGetUid: String
    get() = this.creator?.userid ?: ""