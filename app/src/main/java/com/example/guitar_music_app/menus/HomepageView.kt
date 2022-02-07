package com.example.guitar_music_app.menus


import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.guitar_music_app.R
import com.example.guitar_music_app.login.LoginActivity
import com.example.guitar_music_app.login.LoginEvent
import com.example.guitar_music_app.login.LoginInjector
import com.example.guitar_music_app.results.ResultActivity
import com.example.guitar_music_app.viewModels.UserViewModel
import kotlinx.android.synthetic.main.homepage_fragment.*
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.result_list_fragment.*


class HomepageView : Fragment() {
    private lateinit var viewModel: HomepageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
        return inflater.inflate(R.layout.homepage_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
            this,
            HomepageInjector(requireActivity().application).provideHomepageViewModelFactory()
        )
            .get(HomepageViewModel::class.java)

        setUpClickListeners()
    }
    private fun setUpClickListeners() {
        //TODO-HODIT ALERT DO VLASTNI TRIDY + ZAJISTIT ABY BYL PRI LOGOUT UZIVATEL DOOPRAVDY PRYC
        imb_toolbar_logout.setOnClickListener{
            val builder = AlertDialog.Builder(this.activity)
            builder.setMessage("Are you sure you want to sign out?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    startLoginActivity()
                    val userViewModel: UserViewModel = ViewModelProvider(
                        this,
                        LoginInjector(requireActivity().application).provideUserViewModelFactory()
                    )[UserViewModel::class.java]
                    userViewModel.signOutUser()
                }.setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }

            val alert = builder.create()
            alert.show()
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {startLoginActivity() }


        btn_lectures.setOnClickListener {
            findNavController().navigate(R.id.lecturesView)
        }
        btn_stats.setOnClickListener {
            findNavController().navigate(R.id.resultDetailView)
        }
        btn_personal_info.setOnClickListener {
            findNavController().navigate(R.id.personalInformationView)
        }
    }

    private fun startLoginActivity() = requireActivity().startActivity(
        Intent(
            activity,
            LoginActivity::class.java
        )
    )
}