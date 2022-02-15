package com.example.guitar_music_app.viewModels

import androidx.lifecycle.MutableLiveData
import com.example.guitar_music_app.general.*
import com.example.guitar_music_app.general.ANTENNA_EMPTY
import com.example.guitar_music_app.general.ANTENNA_FULL
import com.example.guitar_music_app.general.ANTENNA_LOOP
import com.example.guitar_music_app.general.LOADING
import com.example.guitar_music_app.general.LOGIN_ERROR
import com.example.guitar_music_app.general.RC_SIGN_IN
import com.example.guitar_music_app.general.SIGNED_IN
import com.example.guitar_music_app.general.SIGNED_OUT
import com.example.guitar_music_app.general.SIGN_IN
import com.example.guitar_music_app.general.SIGN_OUT
import com.example.guitar_music_app.login.LoginEvent
import com.example.guitar_music_app.login.LoginResult
import com.example.guitar_music_app.repositories.UserRepository
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UserViewModel(
    val repo: UserRepository,
    uiContext: CoroutineContext
) : BaseViewModel<LoginEvent<LoginResult>>(uiContext) {

    //MutableLiveObject musi obsahovat vsechny objekty z View
    //The actual data model is kept private to avoid unwanted tampering
    private val userState = MutableLiveData<User>()

    //Control Logic
    //Internal = Viditelne jen v modulu, lepsi encapsulation
    //Unit => Jako Void v jave, trigger eventu ale nezajima nas value
    //Neprimy callback na view
    internal val authAttempt = MutableLiveData<Unit>()
    internal val startAnimation = MutableLiveData<Unit>()

    //UI Binding
    internal val signInStatusText = MutableLiveData<String>()
    internal val authButtonText = MutableLiveData<String>()
    internal val satelliteDrawable = MutableLiveData<String>()

    internal val signedIn = MutableLiveData<Boolean>(false)

    private fun showErrorState() {
        signInStatusText.value = LOGIN_ERROR
        authButtonText.value = SIGN_IN
        satelliteDrawable.value = ANTENNA_EMPTY
    }

    private fun showLoadingState() {
        signInStatusText.value = LOADING
        satelliteDrawable.value = ANTENNA_LOOP
        startAnimation.value = Unit
    }

    private fun showSignedInState() {
        signInStatusText.value = SIGNED_IN
        authButtonText.value = SIGN_OUT
        satelliteDrawable.value = ANTENNA_FULL
    }

    private fun showSignedOutState() {
        signInStatusText.value = SIGNED_OUT
        authButtonText.value = SIGN_IN
        satelliteDrawable.value = ANTENNA_EMPTY
    }

    override fun handleEvent(event: LoginEvent<LoginResult>) {
        //Trigger loading screen first
        showLoadingState()
        //Switch statement
        when (event) {
            is LoginEvent.OnStart -> {getUser()
            }
            is LoginEvent.OnAuthButtonClick -> onAuthButtonClick()
            is LoginEvent.OnGoogleSignInResult -> onSignInResult(event.result)
        }
    }

    private fun getUser() = launch {
        val result = repo.getCurrentUser()
        println("not bad" + result)
        when (result) {
            is GeneralResult.Value -> {
                userState.value = result.value
                if (result.value == null) showSignedOutState()
                else showSignedInState()
            }
            is GeneralResult.Error -> showErrorState()
        }
    }
     fun signOutUser() = launch {
        val result = repo.signOutCurrentUser()
        when (result) {
            is GeneralResult.Value -> {
                userState.value = null
                signedIn.value = false
                showSignedOutState()
            }
            is GeneralResult.Error -> showErrorState()
        }
    }

    /**
     * If user is null, tell the View to begin the authAttempt. Else, attempt to sign the user out
     */
    private fun onAuthButtonClick() {
        //Triggruje authAttempt observable v LoginView, ktery zavola startSignInFlow funkci a ta pripravi aplikaci na prihlaseni
        if (userState.value == null) {
            authAttempt.value = Unit
        }
        else {
            signOutUser()
//            signedIn.value = false
        }
        }

    //Firebase and google singIn
    private fun onSignInResult(result: LoginResult) = launch {
        if (result.requestCode == RC_SIGN_IN && result.userToken != null) {

            val createGoogleUserResult = repo.signInGoogleUser(
                result.userToken
            )

            //Result.Value means it was successful
            if (createGoogleUserResult is GeneralResult.Value) getUser()
            else showErrorState()

            signedIn.value = true
        } else {
            showErrorState()
        }
    }




}