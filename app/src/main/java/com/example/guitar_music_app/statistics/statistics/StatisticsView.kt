package com.example.guitar_music_app.statistics.statistics

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
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
            this,
            StatisticsInjector(requireActivity().application).provideStatisticsViewModelFactory()
        )[StatisticsViewModel::class.java]


        viewModel.handleEvent(StatisticsEvent.OnStart)

        setUpClickListeners()
        observeViewModel()


    }

    //TODO - Muze byt rychlejsi?
    //TODO - Na nekterych mobilech nefunguje for some reason
    private fun observeViewModel() {
        viewModel.updateNotes.observe(
            viewLifecycleOwner,
            { updateNotes ->
                notes_last_result_value.text = viewModel.updateNotes.value?.notesLastValue
                notes_best_result_value.text = viewModel.updateNotes.value?.notesBestValue
                notes_complete_result_value.text = viewModel.updateNotes.value?.notesCompleteValue
            }
        )

        viewModel.updateChords.observe(
            viewLifecycleOwner,
            { updateChords ->
                chords_last_result_value.text = viewModel.updateChords.value?.chordsLastValue
                chords_best_result_value.text = viewModel.updateChords.value?.chordsBestValue
                chords_complete_result_value.text = viewModel.updateChords.value?.chordsCompleteValue
            }
        )

        viewModel.updateRhythm.observe(
            viewLifecycleOwner,
            { updateRhythm ->
                rhythm_last_result_value.text = viewModel.updateRhythm.value?.rhythmLastValue
                rhythm_best_result_value.text = viewModel.updateRhythm.value?.rhythmBestValue
                rhythm_complete_result_value.text = viewModel.updateRhythm.value?.rhythmCompleteValue
            }
        )

        viewModel.finalResultState.observe(
            viewLifecycleOwner,
            {
                complete_value_text.text = viewModel.finalResultState.value.toString()
            }
        )

    }

    private fun setUpClickListeners() {
        btn_back.setOnClickListener {
            findNavController().navigate(R.id.homepageView)
        }
    }
}