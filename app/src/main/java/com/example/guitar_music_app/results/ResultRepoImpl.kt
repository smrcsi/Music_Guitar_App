package com.example.guitar_music_app.results

import com.example.guitar_music_app.general.GeneralResult
import com.example.guitar_music_app.results.room.ResultDao
import com.example.guitar_music_app.general.User
import com.example.guitar_music_app.firebase.FirebaseResult
import com.example.guitar_music_app.general.*
import com.example.guitar_music_app.general.awaitTaskCompletable
import com.example.guitar_music_app.general.awaitTaskResult
import com.example.guitar_music_app.general.toResult
import com.example.guitar_music_app.general.toUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

private const val COLLECTION_NAME = "results"

/**
 * If this wasn't a demo project, I would apply more abstraction to this repository (i.e. local and remote would be
 * separate interfaces which this class would depend on). I wanted to keep it the back end simple since this app is
 * a demo on MVVM, which is a front end architecture pattern.
 */
class ResultRepoImpl(
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    val remote: FirebaseFirestore = FirebaseFirestore.getInstance(),
    val local: ResultDao
) : ResultRepository {


    override suspend fun getResultById(resultId: String): GeneralResult<Exception, Result> {
        val user = getActiveUser()
        return if (user != null) getRemoteResult(resultId, user)
        else getLocalResult(resultId)
    }

    override suspend fun deleteResult(result: Result): GeneralResult<Exception, Unit> {
        val user = getActiveUser()
        return if (user != null) deleteRemoteResult(result.copy(creator = user))
        else deleteLocalResult(result)
    }

    override suspend fun updateResult(result: Result): GeneralResult<Exception, Unit> {
        val user = getActiveUser()
        return if (user != null) updateRemoteResult(result.copy(creator = user))
        else updateLocalResult(result)
    }

    override suspend fun getResults(): GeneralResult<Exception, List<Result>> {
        val user = getActiveUser()
        return if (user != null) getRemoteResults(user)
        else getLocalResults()
    }

    /**
     * if currentUser != null, return true
     */
    private fun getActiveUser(): User? {
        return firebaseAuth.currentUser?.toUser
    }


    private fun resultToResultList(result: QuerySnapshot?): GeneralResult<Exception, List<Result>> {
        val resultList = mutableListOf<Result>()

        result?.forEach { documentSnapshot ->
            resultList.add(documentSnapshot.toObject(FirebaseResult::class.java).toResult)
        }

        return GeneralResult.build {
            resultList
        }
    }


    /* Remote Datasource */

    private suspend fun getRemoteResults(user: User): GeneralResult<Exception, List<Result>> {
        return try {
            val task = awaitTaskResult(
                remote.collection(COLLECTION_NAME)
                    .whereEqualTo("creator", user.userid)
                    .get()
            )

            resultToResultList(task)
        } catch (exception: Exception) {
            GeneralResult.build { throw exception }
        }
    }

    private suspend fun getRemoteResult(creationDate: String, user: User): GeneralResult<Exception, Result> {
        return try {
            val task = awaitTaskResult(
                remote.collection(COLLECTION_NAME)
                    .document(creationDate + user.userid)
                    .get()
            )

            GeneralResult.build {
                //Task<DocumentSnapshot!>
                task.toObject(FirebaseResult::class.java)?.toResult ?: throw Exception()
            }
        } catch (exception: Exception) {
            GeneralResult.build { throw exception }
        }
    }

    private suspend fun deleteRemoteResult(result: Result): GeneralResult<Exception, Unit> = GeneralResult.build {
        awaitTaskCompletable(
            remote.collection(COLLECTION_NAME)
                .document(result.creationDate + result.creator!!.userid)
                .delete()
        )
    }

    /**
     * Notes are stored with the following composite document name:
     * note.creationDate + note.creator.uid
     * The reason for this, is that if I just used the creationDate, hypothetically two users
     * creating a note at the same time, would have duplicate entries in the cloud database :(
     */
    private suspend fun updateRemoteResult(result: Result): GeneralResult<Exception, Unit> {
        return try {
            awaitTaskCompletable(
                remote.collection(COLLECTION_NAME)
                    .document(result.creationDate + result.creator!!.userid)
                    .set(result.toFirebaseResult)
            )

            GeneralResult.build { Unit }

        } catch (exception: Exception) {
            GeneralResult.build { throw exception }
        }
    }

    /* Local Datasource */
    private suspend fun getLocalResults(): GeneralResult<Exception, List<Result>> = GeneralResult.build {
        local.getResults().toResultListFromRoomResult()
    }

    private suspend fun getLocalResult(id: String): GeneralResult<Exception, Result> = GeneralResult.build {
        local.getResultById(id).toResult
    }

    private suspend fun deleteLocalResult(result: Result): GeneralResult<Exception, Unit> = GeneralResult.build {
        local.deleteResult(result.toRoomResult)
        Unit
    }

    private suspend fun updateLocalResult(result: Result): GeneralResult<Exception, Unit> = GeneralResult.build {
        local.insertOrUpdateResult(result.toRoomResult)
        Unit
    }



}