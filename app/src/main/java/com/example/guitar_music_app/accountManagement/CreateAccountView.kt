package com.example.guitar_music_app.accountManagement

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
import com.example.guitar_music_app.ANTENNA_LOOP
import com.example.guitar_music_app.R
import com.example.guitar_music_app.RC_SIGN_IN
import com.example.guitar_music_app.login.LoginEvent
import com.example.guitar_music_app.login.LoginInjector
import com.example.guitar_music_app.login.LoginResult
import com.example.guitar_music_app.results.ResultActivity
import com.example.guitar_music_app.viewModels.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.login_fragment.*

class CreateAccountView : Fragment() {
//    private lateinit var viewModel : UserViewModel
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.create_account_fragment, container, false)
//    }
//
//    override fun onStart() {
//        super.onStart()
//        viewModel = ViewModelProvider(
//            this,
//            LoginInjector(requireActivity().application).provideUserViewModelFactory())
//            .get(UserViewModel::class.java)
//
//
//        setUpClickListeners()
//        observeViewModel()
//
//        viewModel.handleEvent(LoginEvent.OnStart)
//    }
//
//    private fun observeViewModel() {
//        viewModel.signInStatusText.observe(
//            viewLifecycleOwner,
//            Observer {
//                //"it" is the value of the MutableLiveData object, which is inferred to be a String automatically
//                loginStatusTextView.text = it
//            }
//        )
//
//        viewModel.authButtonText.observe(
//            viewLifecycleOwner,
//            Observer {
//                authButton.text = it
//            }
//        )
//
//        viewModel.startAnimation.observe(
//            viewLifecycleOwner,
//            Observer {
//                loginAnimation.setImageResource(
//                    resources.getIdentifier(ANTENNA_LOOP, "drawable", activity?.packageName)
//                )
//                (loginAnimation.drawable as AnimationDrawable).start()
//            }
//        )
//
//        viewModel.authAttempt.observe(
//            viewLifecycleOwner,
//            Observer { startSignInFlow() }
//        )
//
//        viewModel.satelliteDrawable.observe(
//            viewLifecycleOwner,
//            Observer {
//                loginAnimation.setImageResource(
//                    resources.getIdentifier(it, "drawable", activity?.packageName)
//                )
//            }
//        )
//    }
//
//    private fun startSignInFlow() {
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            //Might be problematic
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .build()
//
//        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
//
//        val signInIntent = googleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//        var userToken: String? = null
//
//        try {
//            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
//
//            if (account != null) userToken = account.idToken
//        } catch (exception: Exception) {
//            Log.d("LOGIN", exception.toString())
//        }
//
//        viewModel.handleEvent(
//            LoginEvent.OnGoogleSignInResult(
//                LoginResult(
//                    requestCode,
//                    userToken
//                )
//            )
//        )
//    }
//
//    private fun setUpClickListeners() {
//        authButton.setOnClickListener{ viewModel.handleEvent(LoginEvent.OnAuthButtonClick)}
//
//        backButton.setOnClickListener{ startListActivity() }
//        requireActivity().onBackPressedDispatcher.addCallback(this) {startListActivity()}
//
//
//    }
//    // Navigation not used because it doesn't work well with onActivityResult
//    private fun startListActivity() = requireActivity().startActivity(
//        Intent(
//            activity,
//            ResultActivity::class.java
//        )
//    )
}