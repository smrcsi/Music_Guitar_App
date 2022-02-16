package com.example.guitar_music_app.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.guitar_music_app.R
import com.example.guitar_music_app.lecture.LectureEvent
import kotlinx.android.synthetic.main.chords_fragment.*
import kotlinx.android.synthetic.main.result_fragment.*
import kotlinx.android.synthetic.main.statistics_fragment.*

class StatisticsView : Fragment() {
    private lateinit var viewModel: StatisticsViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.statistics_fragment, container, false)
    }
    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
            this,
            StatisticsInjector(requireActivity().application).provideStatisticsViewModelFactory()
        )[StatisticsViewModel::class.java]

        setUpClickListeners()

        viewModel.handleEvent(StatisticsEvent.OnStart)


        viewModel.lastResult.observe(
            viewLifecycleOwner,
            { lastResult ->
                last_result_text.text = viewModel.lastResult.value.toString()
                println(last_result_text.text)
            }
        )
    }
    private fun setUpClickListeners() {
        btn_back.setOnClickListener {
            findNavController().navigate(R.id.homepageView)
        }
    }
}