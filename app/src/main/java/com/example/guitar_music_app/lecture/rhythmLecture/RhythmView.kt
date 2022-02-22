package com.example.guitar_music_app.lecture.rhythmLecture

import android.app.AlertDialog
import android.content.pm.ActivityInfo
import android.graphics.Color

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.guitar_music_app.R
import kotlinx.android.synthetic.main.rhythm_fragment.*
import kotlinx.coroutines.launch
import androidx.core.view.GestureDetectorCompat
import java.lang.Exception
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat

import com.example.guitar_music_app.general.toEditable
import com.example.guitar_music_app.lecture.LectureEvent
import kotlinx.android.synthetic.main.chords_fragment.endPicture
import kotlinx.android.synthetic.main.rhythm_fragment.btn_back
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RhythmView : Fragment() {

    private lateinit var mDetector: GestureDetectorCompat
    private lateinit var viewModel: RhythmViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rhythm_fragment, container, false)
    }

    //TODO - Pridat random akordy meneny po konci
    private suspend fun playSound() {
        withContext(Dispatchers.IO) {
            val mediaPlayer = MediaPlayer.create(activity, R.raw.a_sharp)
            try {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                    mediaPlayer.release()
                } else {
                    mediaPlayer.start()
                }
            } catch (e: Exception) {
                e.printStackTrace(); }
        }

    }

    private suspend fun playSecond() {
        withContext(Dispatchers.IO) {
            val mediaPlayer = MediaPlayer.create(activity, R.raw.c)
            try {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                    mediaPlayer.release()
                } else {
                    mediaPlayer.start()
                }
            } catch (e: Exception) {
                e.printStackTrace(); }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            RhythmInjector(requireActivity().application).provideRhythmViewModelFactory()
        )[RhythmViewModel::class.java]

        view.setOnTouchListener { v, event ->
            lifecycleScope.launch {
                mDetector.onTouchEvent(event)
            }
            v.performClick()
            true
        }

        ArrayAdapter.createFromResource(
            activity?.applicationContext!!,
            R.array.rhythms_array,
            R.layout.simple_spinner_item_top
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            rhythmSpinner.adapter = adapter
        }

        rhythmSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.changeRhythm(RhythmViewModel.RhythmType.values()[position])
            }
        }

        stringField.setBackgroundColor(Color.LTGRAY)
        mDetector = GestureDetectorCompat(context, MyGestureListener(viewModel))
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setUpClickListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.handleEvent(LectureEvent.OnStart)

        endPicture.setOnClickListener {
            viewModel.handleEvent(
                LectureEvent.OnDoneClick(
                    rhythm_result_text.text.toString()
                )
            )
        }

        viewModel.result.observe(
            viewLifecycleOwner
        ) { result ->
            rhythm_result_text.text = result.score.toEditable()
        }

        viewModel.updated.observe(
            viewLifecycleOwner
        ) {
            findNavController().navigate(R.id.lectureResultView)
        }

        viewModel.slidesNumber.observe(
            viewLifecycleOwner
        ) { result ->
            rhythm_result_text_view.text = result.toString()
        }
        viewModel.uiState.observe(viewLifecycleOwner) {
            updateRhythmArrows(it.flings)
            val stateOfLast = it.flings.lastOrNull { it.state != RhythmViewModel.UiState.FlingState.START }?.state
            if (stateOfLast == RhythmViewModel.UiState.FlingState.VALID) {
                stringField.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.GREEN))
            } else if (stateOfLast == RhythmViewModel.UiState.FlingState.INVALID) {
                stringField.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.RED))
            } else {
                stringField.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
            }
        }
    }


    private fun setUpClickListeners() {
        btn_back.setOnClickListener {
            val builder = AlertDialog.Builder(this.activity)
            builder.setMessage("Opuštěním cvičení příjdete o výsledek. Doopravdy chcete cvičení opustit?")
                .setCancelable(false)
                .setPositiveButton("Ano") { dialog, id ->
                    findNavController().navigate(R.id.lecturesView)
                }.setNegativeButton("Ne") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }

            val alert = builder.create()
            alert.show()
        }
        endPicture.setOnClickListener {
            findNavController().navigate(R.id.lectureResultView)
        }
    }

    private fun updateRhythmArrows(
        flings: List<RhythmViewModel.UiState.Fling>
    ) {
        for (index in 0..5) {
            val fling = flings.getOrNull(index)
            when (index) {
                0 -> updateVisibility(arrow1_up, arrow1_down, fling)
                1 -> updateVisibility(arrow2_up, arrow2_down, fling)
                2 -> updateVisibility(arrow3_up, arrow3_down, fling)
                3 -> updateVisibility(arrow4_up, arrow4_down, fling)
                4 -> updateVisibility(arrow5_up, arrow5_down, fling)
            }
        }
        flings.forEachIndexed { index, fling ->

        }
    }

    private fun updateVisibility(upView: View, downView: View, fling: RhythmViewModel.UiState.Fling?) {
        when (fling?.direction) {
            RhythmViewModel.UiState.Direction.UP -> {
                downView.visibility = View.INVISIBLE
                upView.visibility = View.VISIBLE
            }
            RhythmViewModel.UiState.Direction.DOWN -> {
                downView.visibility = View.VISIBLE
                upView.visibility = View.INVISIBLE
            }
            null -> {
                downView.visibility = View.INVISIBLE
                upView.visibility = View.INVISIBLE
            }
        }
        val green = ContextCompat.getColor(requireContext(), R.color.GREEN)
        val red = ContextCompat.getColor(requireContext(), R.color.RED)
        when (fling?.state) {
            null,
            RhythmViewModel.UiState.FlingState.START -> {
                Log.d("vojta", "Setting background to transparent")
                val transparent = ContextCompat.getColor(requireContext(), android.R.color.transparent)
                upView.setBackgroundColor(transparent)
                downView.setBackgroundColor(transparent)
            }
            RhythmViewModel.UiState.FlingState.VALID -> {
                Log.d("vojta", "Setting background to green")
                upView.setBackgroundColor(green)
                downView.setBackgroundColor(green)
            }
            RhythmViewModel.UiState.FlingState.INVALID -> {
                Log.d("vojta", "Setting background to red")
                upView.setBackgroundColor(red)
                downView.setBackgroundColor(red)
            }
        }
    }
}
