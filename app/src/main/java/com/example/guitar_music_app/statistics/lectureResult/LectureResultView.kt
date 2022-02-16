package com.example.guitar_music_app.statistics.lectureResult

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.guitar_music_app.R
import com.example.guitar_music_app.statistics.StatisticsEvent
import com.example.guitar_music_app.statistics.statistics.StatisticsInjector
import kotlinx.android.synthetic.main.result_fragment.*

class LectureResultView : Fragment() {
    private lateinit var viewModel: LectureResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.result_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
            this,
            LectureResultInjector(requireActivity().application).provideLectureResultViewModelFactory()
        )
            .get(LectureResultViewModel::class.java)

        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setUpClickListeners()
        observeViewModel()

        viewModel.handleEvent(StatisticsEvent.OnStart)


    }

    private fun observeViewModel() {

        viewModel.bestResult.observe(
            viewLifecycleOwner,
            { bestResult ->
                highest_score_text.text = viewModel.bestResult.value.toString()
                println(highest_score_text.text)
            }
        )

        viewModel.lastResult.observe(
            viewLifecycleOwner,
            { lastResult ->
                last_result_text.text = viewModel.lastResult.value.toString()
                println(last_result_text.text)
            }
        )

        viewModel.lecturePlayed.observe(
            viewLifecycleOwner,
            { lecturePlayed ->
                if (viewModel.lecturePlayed.value == "notes") {
                    lbl_highest.text = "Nejvyšší výsledek not"
                } else if (viewModel.lecturePlayed.value == "chords") {
                    lbl_highest.text = "Nejvyšší výsledek akordů"
                } else if (viewModel.lecturePlayed.value == "rhythm") {
                    lbl_highest.text = "Nejvyšší výsledek rytmu"
                }
            }
        )
    }

    private fun setUpClickListeners() {

        btn_homepage.setOnClickListener {
            findNavController().navigate(R.id.homepageView)
        }

    }
}