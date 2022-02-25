package com.example.guitar_music_app.lecture.rhythmLecture

import android.app.AlertDialog
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.guitar_music_app.R
import com.example.guitar_music_app.general.toEditable
import com.example.guitar_music_app.lecture.LectureEvent
import com.example.guitar_music_app.lecture.Note
import kotlinx.android.synthetic.main.chords_fragment.endPicture
import kotlinx.android.synthetic.main.rhythm_fragment.*
import kotlinx.coroutines.launch
import java.util.*


class RhythmView : Fragment() {

    private lateinit var mDetector: GestureDetectorCompat
    private lateinit var viewModel: RhythmViewModel
    private lateinit var soundPool: SoundPool

    //    private val sounds  = mutableListOf(R.raw.f, R.raw.g, R.raw.a, R.raw.c, R.raw.d, R.raw.e)
    private val sounds = mutableMapOf<Note, Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rhythm_fragment, container, false)
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

        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setUsage(
                AudioAttributes.USAGE_MEDIA
            )
            .setContentType(
                AudioAttributes.CONTENT_TYPE_MUSIC
            )
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(
                audioAttributes
            )
            .build()

        // This load function takes
        // three parameter context,
        // file_name and priority.

        // This load function takes
        // three parameter context,
        // file_name and priority.
        // >0 >1 >2 >3 >4 >5

        for (note in Note.values()) {
            sounds[note] = soundPool
                .load(
                    requireContext(),
                    note.tone,
                    1
                )
        }


    }

    private fun playSound(tone: Note) {

        soundPool.play(
            sounds[tone]!!, 1F, 1F, 0, 0, 1F
        )
    }

    private fun randomSound(): Note {
        val pick = Random().nextInt(sounds.values.size)
        return Note.values()[pick]
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
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            updateRhythmArrows(uiState.flings)
            val lastOrNull =
                uiState.flings.lastOrNull { it.state != RhythmViewModel.UiState.FlingState.START }
            val stateOfLast =
                lastOrNull?.state
            if (stateOfLast == RhythmViewModel.UiState.FlingState.VALID) {
                if (lastOrNull.direction == RhythmViewModel.UiState.Direction.UP) {
                    stringField.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.GREEN
                        )
                    )
                    playSound(randomSound())
                } else {
                    stringField.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.BLUE
                        )
                    )
                    playSound(randomSound())
                }
            } else if (uiState.errorMessage != null) {
                stringField.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.RED
                    )
                )
            } else {
                stringField.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.transparent
                    )
                )
            }
        }
        viewModel.uiState.observe(viewLifecycleOwner) {
            if (viewModel.uiState.value?.errorMessage != null) {
                displayErrorToast()
            }
            if (viewModel.uiState.value?.successMessage != null) {
                displaySuccessToast()
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
    }

    private fun updateVisibility(
        upView: View,
        downView: View,
        fling: RhythmViewModel.UiState.Fling?
    ) {
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
                val transparent =
                    ContextCompat.getColor(requireContext(), android.R.color.transparent)
                upView.setBackgroundColor(transparent)
                downView.setBackgroundColor(transparent)
            }
            RhythmViewModel.UiState.FlingState.VALID -> {
                upView.setBackgroundColor(green)
                downView.setBackgroundColor(green)
            }
            RhythmViewModel.UiState.FlingState.INVALID -> {
                upView.setBackgroundColor(red)
                downView.setBackgroundColor(red)
                println("This happened which should")
            }
        }
    }

    private fun displayErrorToast() {
//Toast nejde v backgroundu
        val toast = Toast.makeText(
            context,
            Html.fromHtml("<font color='#FFFFFF' ><b>" + viewModel.uiState.value?.errorMessage + "</b></font>"),
            Toast.LENGTH_SHORT
        )
        toast.setGravity(Gravity.TOP, 0, 0)

        // TODO Koukni na snackbar
        toast.show()
    }

    private fun displaySuccessToast() {
//Toast nejde v backgroundu
        val toast = Toast.makeText(
            context,
            Html.fromHtml("<font color='#FFFFFF' ><b>" + viewModel.uiState.value?.successMessage + "</b></font>"),
            Toast.LENGTH_SHORT
        )
        toast.setGravity(Gravity.TOP, 0, 0)

        // TODO Koukni na snackbar
        toast.show()
    }
}

