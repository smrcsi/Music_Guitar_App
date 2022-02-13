package com.example.guitar_music_app.firebase

import com.example.guitar_music_app.general.awaitTaskCompletable
import com.example.guitar_music_app.general.GeneralResult
import com.example.guitar_music_app.general.User
import com.example.guitar_music_app.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseUserRepo(val auth: FirebaseAuth = FirebaseAuth.getInstance()) : UserRepository {

    override suspend fun getCurrentUser(): GeneralResult<Exception, User?> {
        val firebaseUser = auth.currentUser


        return if (firebaseUser == null) {
            GeneralResult.build { null }
        } else {
                GeneralResult.build {

                    println("TOHLE JE UID A DISPLAYNAME" + firebaseUser.displayName)
                    User(
                        firebaseUser.uid,
                        firebaseUser.displayName ?: ""
                    )

                }
            }
        }

    override suspend fun signOutCurrentUser(): GeneralResult<Exception, Unit> {
        return GeneralResult.build {
            auth.signOut()
        }
    }

    override suspend fun signInGoogleUser(idToken: String): GeneralResult<Exception, Unit> =
        withContext(Dispatchers.IO) {
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                awaitTaskCompletable(auth.signInWithCredential(credential))
                GeneralResult.build { Unit }
            } catch (exception: Exception) {
                GeneralResult.build { throw exception }
            }
        }
}