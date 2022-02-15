package com.example.guitar_music_app.statistics

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.guitar_music_app.R
import com.example.guitar_music_app.menus.HomepageInjector
import com.example.guitar_music_app.menus.HomepageViewModel
import kotlinx.android.synthetic.main.result_fragment.*

class LectureResultView : Fragment() {
    private lateinit var viewModel: HomepageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.result_fragment, container, false)
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

        btn_homepage.setOnClickListener {
            findNavController().navigate(R.id.homepageView)
        }

    }
}