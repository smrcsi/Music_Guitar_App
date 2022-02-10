package com.example.guitar_music_app.menus

import android.content.Intent
import android.content.pm.ActivityInfo
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
import kotlinx.android.synthetic.main.homepage_fragment.*
import kotlinx.android.synthetic.main.lectures_fragment.*
import kotlinx.android.synthetic.main.login_fragment.*

class LecturesView : Fragment () {
    private lateinit var viewModel: HomepageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.lectures_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
            this,
            HomepageInjector(requireActivity().application).provideHomepageViewModelFactory()
        )
            .get(HomepageViewModel::class.java)

        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setUpClickListeners()
    }
    private fun setUpClickListeners() {

        btn_back.setOnClickListener {
            findNavController().navigate(R.id.homepageView)
        }
        btn_chords.setOnClickListener{
            findNavController().navigate(R.id.chordsView)
        }

        btn_notes.setOnClickListener{
            findNavController().navigate(R.id.notesView)
        }

        btn_rhythm.setOnClickListener{
            findNavController().navigate(R.id.rhythmView)
        }
    }
}
