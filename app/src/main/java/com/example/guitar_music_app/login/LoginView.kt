package com.example.guitar_music_app.login

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.guitar_music_app.*
import com.example.guitar_music_app.general.awaitTaskCompletable
import com.example.guitar_music_app.general.awaitTaskResult
import com.example.guitar_music_app.menus.HomepageView
import com.example.guitar_music_app.repositories.UserRepository
import com.example.guitar_music_app.results.ResultActivity
import com.example.guitar_music_app.viewModels.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.result_list_fragment.*

class LoginView : Fragment() {
    private lateinit var viewModel : UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
             this,
            LoginInjector(requireActivity().application).provideUserViewModelFactory())
            .get(UserViewModel::class.java)
        setUpClickListeners()
        observeViewModel()

        viewModel.handleEvent(LoginEvent.OnStart)
    }

     private fun observeViewModel() {
        viewModel.signInStatusText.observe(
            viewLifecycleOwner,
            Observer {
                //"it" is the value of the MutableLiveData object, which is inferred to be a String automatically
                loginStatusTextView.text = it
            }
        )
         viewModel.signedIn.observe(
             viewLifecycleOwner,
             {
                 if (it) {
                     startListActivity()
                     //TODO-OnBackPressedDispatcher nahradit, neni to "onBack"
                     requireActivity().onBackPressedDispatcher.addCallback(this) {
                         startListActivity()
                     }
                 }
                 else if (loginStatusTextView.text  == "Signed In") {
                     startSignInFlow()
                 }
             }
         )

         viewModel.signInStatusText.observe(
             viewLifecycleOwner,
             {
                 if (loginStatusTextView.text == SIGNED_IN) {
                     startListActivity()
                 }
             }
         )

        viewModel.authButtonText.observe(
            viewLifecycleOwner,
            Observer {
                authButton.text = it
            }
        )

        viewModel.startAnimation.observe(
            viewLifecycleOwner,
            Observer {
                loginAnimation.setImageResource(
                    resources.getIdentifier(ANTENNA_LOOP, "drawable", activity?.packageName)
                )
                (loginAnimation.drawable as AnimationDrawable).start()
            }
        )

        viewModel.authAttempt.observe(
            viewLifecycleOwner,
            Observer { startSignInFlow() }
        )

        viewModel.satelliteDrawable.observe(
            viewLifecycleOwner,
            Observer {
                loginAnimation.setImageResource(
                    resources.getIdentifier(it, "drawable", activity?.packageName)
                )
            }
        )
    }

    private fun startSignInFlow() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //Might be problematic
            .requestIdToken(getString(R.string.default_web_client_id))
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        var userToken: String? = null

        try {
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)

            if (account != null) userToken = account.idToken
        } catch (exception: Exception) {
            Log.d("LOGIN", exception.toString())
        }

        viewModel.handleEvent(
            LoginEvent.OnGoogleSignInResult(
                LoginResult(
                    requestCode,
                    userToken
                )
            )
        )
    }
    //TODO-Pri prvnim zapnuti aplikace nefunguje prihlaseni (jestlize je uzivatel uz prihlasen a prvni se musi na homepage odhlasit)
    private fun setUpClickListeners() {
        authButton.setOnClickListener{viewModel.handleEvent(LoginEvent.OnAuthButtonClick)
            }

       backButton.setOnClickListener{ startListActivity() }
        requireActivity().onBackPressedDispatcher.addCallback(this) {startListActivity()}

    }
// Navigation not used because it doesn't work well with onActivityResult
    private fun startListActivity() = requireActivity().startActivity(
        Intent(
            activity,
            ResultActivity::class.java
        )
    )


}