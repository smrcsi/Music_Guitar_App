package com.example.guitar_music_app.lecture.rhythmLecture

import android.content.pm.ActivityInfo
import android.graphics.Color

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.guitar_music_app.R
import kotlinx.android.synthetic.main.chords_fragment.endPicture
import kotlinx.android.synthetic.main.rhythm_fragment.*
import kotlinx.coroutines.launch
import androidx.core.view.GestureDetectorCompat
import java.lang.Exception
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast

import androidx.annotation.RequiresApi
import com.example.guitar_music_app.general.toEditable
import com.example.guitar_music_app.lecture.LectureEvent
import kotlinx.android.synthetic.main.rhythm_fragment.btn_back
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RhythmView : Fragment() {

    private lateinit var mDetector: GestureDetectorCompat
    private lateinit var viewModel: RhythmViewModel

    private var intSlidesNumber = 0

    private var arrow1state = false
    private var arrow2state = false
    private var arrow3state = false
    private var arrow4state = false
    private var arrow5state = false
    private var arrow6state = false
    private var arrow7state = false
    private var arrow8state = false
    private var arrow9state = false
    private var arrow10state = false
    private var arrow11state = false
    private var arrow12state = false
    private var arrow13state = false
    private var arrow14state = false
    private var arrow15state = false
    private var arrow16state = false
    private var arrow17state = false
    private var arrow18state = false
    private var arrow19state = false
    private var arrow20state = false
    private var firstStep = false
    private var arrowPosition = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rhythm_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() {
        super.onStart()

        viewModel = ViewModelProvider(
            this,
            RhythmInjector(requireActivity().application).provideRhythmViewModelFactory()
        )[RhythmViewModel::class.java]

        stringField.setBackgroundColor(Color.LTGRAY)
        mDetector = GestureDetectorCompat(context, MyGestureListener(viewModel))
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setUpClickListeners()
        observeViewModel()


        viewModel.rhythmState.observe(viewLifecycleOwner, { rhythmState ->
            if (!firstStep) {
                firstStep = true
            } else {
                intSlidesNumber += 1
                viewModel.slidesNumber.value = intSlidesNumber
                when (arrowPosition) {
                    0 -> {
                        if (rhythmState.isFlingUpValid) {
                            stringField.setBackgroundColor(Color.GREEN)
                            lifecycleScope.launch { playSound() }
                        } else {
                            stringField.setBackgroundColor(Color.BLUE)
                            lifecycleScope.launch { playSecond() }
                        }
                    }
                    1 -> {
                        if (firstStep && !arrow1state && !arrow2state && !arrow3state && !arrow4state && !arrow5state) {
                            if (rhythmState.isFlingUpValid) {
                                arrow1state = false
                                arrow1.setBackgroundColor(Color.RED)
                                stringField.setBackgroundColor(Color.GREEN)
                                lifecycleScope.launch { playSound() }
                            } else {
                                stringField.setBackgroundColor(Color.BLUE)
                                lifecycleScope.launch { playSecond() }
                                arrow1state = true
                                arrow1.setBackgroundColor(Color.GREEN)
                            }
                        } else if (arrow1state && !arrow2state && !arrow3state && !arrow4state && !arrow5state) {
                            if (rhythmState.isFlingUpValid) {
                                arrow1state = false
                                arrow2state = false
                                arrow1.setBackgroundColor(Color.RED)
                                arrow2.setBackgroundColor(Color.RED)
                                stringField.setBackgroundColor(Color.GREEN)
                                lifecycleScope.launch { playSound() }
                            } else {
                                stringField.setBackgroundColor(Color.BLUE)
                                lifecycleScope.launch { playSecond() }
                                arrow2state = true
                                arrow2.setBackgroundColor(Color.GREEN)
                            }
                        } else if (arrow1state && arrow2state && !arrow3state && !arrow4state && !arrow5state) {
                            if (rhythmState.isFlingUpValid) {
                                arrow1state = false
                                arrow2state = false
                                arrow3state = false
                                arrow1.setBackgroundColor(Color.RED)
                                arrow2.setBackgroundColor(Color.RED)
                                arrow3.setBackgroundColor(Color.RED)
                                stringField.setBackgroundColor(Color.GREEN)
                                lifecycleScope.launch { playSound() }
                            } else {
                                stringField.setBackgroundColor(Color.BLUE)
                                lifecycleScope.launch { playSecond() }

                                arrow3state = true
                                arrow3.setBackgroundColor(Color.GREEN)
                            }
                        } else if (arrow1state && arrow2state && arrow3state && !arrow4state && !arrow5state) {
                            if (rhythmState.isFlingUpValid) {

                                arrow4state = true
                                arrow4.setBackgroundColor(Color.GREEN)
                                stringField.setBackgroundColor(Color.GREEN)
                                lifecycleScope.launch { playSound() }
                            } else {
                                stringField.setBackgroundColor(Color.BLUE)
                                lifecycleScope.launch { playSecond() }
                                arrow1state = false
                                arrow2state = false
                                arrow3state = false
                                arrow4state = false
                                arrow1.setBackgroundColor(Color.RED)
                                arrow2.setBackgroundColor(Color.RED)
                                arrow3.setBackgroundColor(Color.RED)
                                arrow4.setBackgroundColor(Color.RED)
                            }
                        } else if (arrow1state && arrow2state && arrow3state && arrow4state && !arrow5state) {
                            if (rhythmState.isFlingUpValid) {
                                arrow1state = false
                                arrow2state = false
                                arrow3state = false
                                arrow4state = false
                                arrow5state = false
                                arrow1.setBackgroundColor(Color.RED)
                                arrow2.setBackgroundColor(Color.RED)
                                arrow3.setBackgroundColor(Color.RED)
                                arrow4.setBackgroundColor(Color.RED)
                                arrow5.setBackgroundColor(Color.RED)
                                stringField.setBackgroundColor(Color.GREEN)
                                lifecycleScope.launch { playSound() }
                            } else {
                                stringField.setBackgroundColor(Color.BLUE)
                                lifecycleScope.launch { playSecond() }
                                arrow5state = true
                                arrow5.setBackgroundColor(Color.GREEN)
                            }
                        } else if (arrow1state && arrow2state && arrow3state && arrow4state && arrow5state) {
                            arrow1state = false
                            arrow2state = false
                            arrow3state = false
                            arrow4state = false
                            arrow5state = false
                            viewModel.addToResult()

                            arrow1.setBackgroundColor(Color.TRANSPARENT)
                            arrow2.setBackgroundColor(Color.TRANSPARENT)
                            arrow3.setBackgroundColor(Color.TRANSPARENT)
                            arrow4.setBackgroundColor(Color.TRANSPARENT)
                            arrow5.setBackgroundColor(Color.TRANSPARENT)
                            stringField.setBackgroundColor(Color.TRANSPARENT)
                            Toast.makeText(context, "SPRAVNE", Toast.LENGTH_SHORT).show()
                        }
                    }
                    2 -> {
                        if (firstStep && !arrow6state && !arrow7state && !arrow8state && !arrow9state && !arrow10state) {
                            if (rhythmState.isFlingUpValid) {
                                arrow6state = false
                                arrow6.setBackgroundColor(Color.RED)
                                stringField.setBackgroundColor(Color.GREEN)
                                lifecycleScope.launch { playSound() }
                            } else {
                                stringField.setBackgroundColor(Color.BLUE)
                                lifecycleScope.launch { playSecond() }
                                arrow6state = true
                                arrow6.setBackgroundColor(Color.GREEN)
                            }
                        } else if (arrow6state && !arrow7state && !arrow8state && !arrow9state && !arrow10state) {
                            if (rhythmState.isFlingUpValid) {
                                arrow6state = false
                                arrow7state = false
                                arrow6.setBackgroundColor(Color.RED)
                                arrow7.setBackgroundColor(Color.RED)
                                stringField.setBackgroundColor(Color.GREEN)
                                lifecycleScope.launch { playSound() }
                            } else {
                                stringField.setBackgroundColor(Color.BLUE)
                                lifecycleScope.launch { playSecond() }
                                arrow7state = true
                                arrow7.setBackgroundColor(Color.GREEN)
                            }
                        } else if (arrow6state && arrow7state && !arrow8state && !arrow9state && !arrow10state) {
                            if (rhythmState.isFlingUpValid) {
                                stringField.setBackgroundColor(Color.GREEN)
                                lifecycleScope.launch { playSecond() }
                                arrow8state = true
                                arrow8.setBackgroundColor(Color.GREEN)
                            } else {
                                arrow6state = false
                                arrow7state = false
                                arrow8state = false
                                arrow6.setBackgroundColor(Color.RED)
                                arrow7.setBackgroundColor(Color.RED)
                                arrow8.setBackgroundColor(Color.RED)
                                stringField.setBackgroundColor(Color.BLUE)
                                lifecycleScope.launch { playSound() }
                            }
                        } else if (arrow6state && arrow7state && arrow8state && !arrow9state && !arrow10state) {
                            if (rhythmState.isFlingUpValid) {
                                stringField.setBackgroundColor(Color.GREEN)
                                lifecycleScope.launch { playSecond() }
                                arrow6state = false
                                arrow7state = false
                                arrow8state = false
                                arrow9state = false
                                arrow6.setBackgroundColor(Color.RED)
                                arrow7.setBackgroundColor(Color.RED)
                                arrow8.setBackgroundColor(Color.RED)
                                arrow9.setBackgroundColor(Color.RED)
                            } else {
                                arrow9state = true
                                arrow9.setBackgroundColor(Color.GREEN)
                                stringField.setBackgroundColor(Color.BLUE)
                                lifecycleScope.launch { playSound() }
                            }
                        } else if (arrow6state && arrow7state && arrow8state && arrow9state && !arrow10state) {
                            if (rhythmState.isFlingUpValid) {
                                stringField.setBackgroundColor(Color.GREEN)
                                lifecycleScope.launch { playSecond() }
                                arrow10state = true
                                arrow10.setBackgroundColor(Color.GREEN)
                            } else {
                                arrow6state = false
                                arrow7state = false
                                arrow8state = false
                                arrow9state = false
                                arrow10state = false
                                arrow6.setBackgroundColor(Color.RED)
                                arrow7.setBackgroundColor(Color.RED)
                                arrow8.setBackgroundColor(Color.RED)
                                arrow9.setBackgroundColor(Color.RED)
                                arrow10.setBackgroundColor(Color.RED)
                                stringField.setBackgroundColor(Color.BLUE)
                                lifecycleScope.launch { playSound() }
                            }
                        } else if (arrow6state && arrow7state && arrow8state && arrow9state && arrow10state) {
                            arrow6state = false
                            arrow7state = false
                            arrow8state = false
                            arrow9state = false
                            arrow10state = false

                            viewModel.addToResult()

                            arrow6.setBackgroundColor(Color.TRANSPARENT)
                            arrow7.setBackgroundColor(Color.TRANSPARENT)
                            arrow8.setBackgroundColor(Color.TRANSPARENT)
                            arrow9.setBackgroundColor(Color.TRANSPARENT)
                            arrow10.setBackgroundColor(Color.TRANSPARENT)
                            stringField.setBackgroundColor(Color.TRANSPARENT)
                            Toast.makeText(context, "SPRAVNE", Toast.LENGTH_SHORT).show()
                        }
                    }
                    3 -> {
                        if (firstStep && !arrow11state && !arrow12state && !arrow13state && !arrow14state && !arrow15state) {
                            if (rhythmState.isFlingUpValid) {
                                arrow11state = false
                                arrow11.setBackgroundColor(Color.RED)
                                stringField.setBackgroundColor(Color.GREEN)
                                lifecycleScope.launch { playSound() }
                            } else {
                                stringField.setBackgroundColor(Color.BLUE)
                                lifecycleScope.launch { playSecond() }
                                arrow11state = true
                                arrow11.setBackgroundColor(Color.GREEN)
                            }
                        } else if (arrow11state && !arrow12state && !arrow13state && !arrow14state && !arrow15state) {
                            if (rhythmState.isFlingUpValid) {
                                stringField.setBackgroundColor(Color.BLUE)
                                lifecycleScope.launch { playSecond() }
                                arrow12state = true
                                arrow12.setBackgroundColor(Color.GREEN)
                            } else {
                                arrow11state = false
                                arrow12state = false
                                arrow11.setBackgroundColor(Color.RED)
                                arrow12.setBackgroundColor(Color.RED)
                                stringField.setBackgroundColor(Color.GREEN)
                                lifecycleScope.launch { playSound() }
                            }
                        } else if (arrow11state && arrow12state && !arrow13state && !arrow14state && !arrow15state) {
                            if (rhythmState.isFlingUpValid) {
                                arrow11state = false
                                arrow12state = false
                                arrow13state = false
                                arrow11.setBackgroundColor(Color.RED)
                                arrow12.setBackgroundColor(Color.RED)
                                arrow13.setBackgroundColor(Color.RED)
                                stringField.setBackgroundColor(Color.GREEN)
                                lifecycleScope.launch { playSound() }

                            } else {
                                stringField.setBackgroundColor(Color.BLUE)
                                lifecycleScope.launch { playSecond() }
                                arrow13state = true
                                arrow13.setBackgroundColor(Color.GREEN)

                            }
                        } else if (arrow11state && arrow12state && arrow13state && !arrow14state && !arrow15state) {
                            if (rhythmState.isFlingUpValid) {
                                arrow14state = true
                                arrow14.setBackgroundColor(Color.GREEN)
                                stringField.setBackgroundColor(Color.GREEN)
                                lifecycleScope.launch { playSound() }
                            } else {
                                stringField.setBackgroundColor(Color.BLUE)
                                lifecycleScope.launch { playSecond() }
                                arrow11state = false
                                arrow12state = false
                                arrow13state = false
                                arrow14state = false
                                arrow11.setBackgroundColor(Color.RED)
                                arrow12.setBackgroundColor(Color.RED)
                                arrow13.setBackgroundColor(Color.RED)
                                arrow14.setBackgroundColor(Color.RED)
                            }
                        } else if (arrow11state && arrow12state && arrow13state && arrow14state && !arrow15state) {
                            if (rhythmState.isFlingUpValid) {
                                arrow11state = false
                                arrow12state = false
                                arrow13state = false
                                arrow14state = false
                                arrow15state = false
                                arrow11.setBackgroundColor(Color.RED)
                                arrow12.setBackgroundColor(Color.RED)
                                arrow13.setBackgroundColor(Color.RED)
                                arrow14.setBackgroundColor(Color.RED)
                                arrow15.setBackgroundColor(Color.RED)
                                stringField.setBackgroundColor(Color.GREEN)
                                lifecycleScope.launch { playSound() }
                            } else {
                                stringField.setBackgroundColor(Color.BLUE)
                                lifecycleScope.launch { playSecond() }
                                arrow15state = true
                                arrow15.setBackgroundColor(Color.GREEN)
                            }
                        } else if (arrow11state && arrow12state && arrow13state && arrow14state && arrow15state) {
                            arrow11state = false
                            arrow12state = false
                            arrow13state = false
                            arrow14state = false
                            arrow15state = false

                            viewModel.addToResult()

                            arrow11.setBackgroundColor(Color.TRANSPARENT)
                            arrow12.setBackgroundColor(Color.TRANSPARENT)
                            arrow13.setBackgroundColor(Color.TRANSPARENT)
                            arrow14.setBackgroundColor(Color.TRANSPARENT)
                            arrow15.setBackgroundColor(Color.TRANSPARENT)
                            stringField.setBackgroundColor(Color.TRANSPARENT)
                            Toast.makeText(context, "SPRAVNE", Toast.LENGTH_SHORT).show()
                        }
                    }
                    4 -> {
                        if (firstStep && !arrow16state && !arrow17state && !arrow18state && !arrow19state && !arrow20state) {
                            if (rhythmState.isFlingUpValid) {
                                stringField.setBackgroundColor(Color.RED)
                                lifecycleScope.launch { playSecond() }
                                arrow16state = true
                                arrow16.setBackgroundColor(Color.GREEN)
                            } else {
                                arrow16state = false
                                arrow16.setBackgroundColor(Color.BLUE)
                                stringField.setBackgroundColor(Color.GREEN)
                                lifecycleScope.launch { playSound() }
                            }
                        } else if (arrow16state && !arrow17state && !arrow18state && !arrow19state && !arrow20state) {
                            if (rhythmState.isFlingUpValid) {
                                stringField.setBackgroundColor(Color.BLUE)
                                lifecycleScope.launch { playSecond() }
                                arrow17state = true
                                arrow17.setBackgroundColor(Color.GREEN)
                            } else {
                                arrow16state = false
                                arrow17state = false
                                arrow16.setBackgroundColor(Color.RED)
                                arrow17.setBackgroundColor(Color.RED)
                                stringField.setBackgroundColor(Color.GREEN)
                                lifecycleScope.launch { playSound() }
                            }
                        } else if (arrow16state && arrow17state && !arrow18state && !arrow19state && !arrow20state) {
                            if (rhythmState.isFlingUpValid) {
                                arrow16state = false
                                arrow17state = false
                                arrow18state = false
                                arrow16.setBackgroundColor(Color.RED)
                                arrow17.setBackgroundColor(Color.RED)
                                arrow18.setBackgroundColor(Color.RED)
                                stringField.setBackgroundColor(Color.GREEN)
                                lifecycleScope.launch { playSound() }
                            } else {
                                stringField.setBackgroundColor(Color.BLUE)
                                lifecycleScope.launch { playSecond() }
                                arrow18state = true
                                arrow18.setBackgroundColor(Color.GREEN)
                            }
                        } else if (arrow16state && arrow17state && arrow18state && !arrow19state && !arrow20state) {
                            if (rhythmState.isFlingUpValid) {
                                arrow19state = true
                                arrow19.setBackgroundColor(Color.GREEN)
                                stringField.setBackgroundColor(Color.GREEN)
                                lifecycleScope.launch { playSound() }
                            } else {
                                stringField.setBackgroundColor(Color.BLUE)
                                lifecycleScope.launch { playSecond() }
                                arrow16state = false
                                arrow17state = false
                                arrow18state = false
                                arrow19state = false
                                arrow16.setBackgroundColor(Color.RED)
                                arrow17.setBackgroundColor(Color.RED)
                                arrow18.setBackgroundColor(Color.RED)
                                arrow19.setBackgroundColor(Color.RED)
                            }
                        } else if (arrow16state && arrow17state && arrow18state && arrow19state && !arrow20state) {
                            if (rhythmState.isFlingUpValid) {
                                arrow16state = false
                                arrow17state = false
                                arrow18state = false
                                arrow19state = false
                                arrow20state = false
                                arrow16.setBackgroundColor(Color.RED)
                                arrow17.setBackgroundColor(Color.RED)
                                arrow18.setBackgroundColor(Color.RED)
                                arrow19.setBackgroundColor(Color.RED)
                                arrow20.setBackgroundColor(Color.RED)
                                stringField.setBackgroundColor(Color.GREEN)
                                lifecycleScope.launch { playSound() }
                            } else {
                                stringField.setBackgroundColor(Color.BLUE)
                                lifecycleScope.launch { playSecond() }
                                arrow20state = true
                                arrow20.setBackgroundColor(Color.GREEN)
                            }
                        } else if (arrow16state && arrow17state && arrow18state && arrow19state && arrow20state) {
                            arrow16state = false
                            arrow17state = false
                            arrow18state = false
                            arrow19state = false
                            arrow20state = false


                            viewModel.addToResult()

                            arrow16.setBackgroundColor(Color.TRANSPARENT)
                            arrow17.setBackgroundColor(Color.TRANSPARENT)
                            arrow18.setBackgroundColor(Color.TRANSPARENT)
                            arrow19.setBackgroundColor(Color.TRANSPARENT)
                            arrow20.setBackgroundColor(Color.TRANSPARENT)
                            stringField.setBackgroundColor(Color.TRANSPARENT)
                            Toast.makeText(context, "SPRÁVNĚ", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.N)
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
            viewLifecycleOwner,
            { result ->
                rhythm_result_text.text = result.score.toEditable()
            }
        )

        viewModel.updated.observe(
            viewLifecycleOwner,
            {
                findNavController().navigate(R.id.lectureResultView)
            }
        )

        viewModel.slidesNumber.observe(
            viewLifecycleOwner,
            { result ->
                rhythm_result_text_view.text = viewModel.slidesNumber.value.toString()
            }
        )
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
                when (position) {
                    0 -> {
                        arrow1.visibility = View.GONE
                        arrow2.visibility = View.GONE
                        arrow3.visibility = View.GONE
                        arrow4.visibility = View.GONE
                        arrow5.visibility = View.GONE
                        arrow6.visibility = View.GONE
                        arrow7.visibility = View.GONE
                        arrow8.visibility = View.GONE
                        arrow9.visibility = View.GONE
                        arrow10.visibility = View.GONE
                        arrow11.visibility = View.GONE
                        arrow12.visibility = View.GONE
                        arrow13.visibility = View.GONE
                        arrow14.visibility = View.GONE
                        arrow15.visibility = View.GONE
                        arrow16.visibility = View.GONE
                        arrow17.visibility = View.GONE
                        arrow18.visibility = View.GONE
                        arrow19.visibility = View.GONE
                        arrow20.visibility = View.GONE
                        arrowPosition = 0
                    }
                    1 -> {
                        arrow1.visibility = View.VISIBLE
                        arrow2.visibility = View.VISIBLE
                        arrow3.visibility = View.VISIBLE
                        arrow4.visibility = View.VISIBLE
                        arrow5.visibility = View.VISIBLE
                        arrow6.visibility = View.INVISIBLE
                        arrow7.visibility = View.INVISIBLE
                        arrow8.visibility = View.INVISIBLE
                        arrow9.visibility = View.INVISIBLE
                        arrow10.visibility = View.INVISIBLE
                        arrow11.visibility = View.INVISIBLE
                        arrow12.visibility = View.INVISIBLE
                        arrow13.visibility = View.INVISIBLE
                        arrow14.visibility = View.INVISIBLE
                        arrow15.visibility = View.INVISIBLE
                        arrow16.visibility = View.INVISIBLE
                        arrow17.visibility = View.INVISIBLE
                        arrow18.visibility = View.INVISIBLE
                        arrow19.visibility = View.INVISIBLE
                        arrow20.visibility = View.INVISIBLE
                        arrowPosition = 1
                    }
                    2 -> {
                        arrow1.visibility = View.INVISIBLE
                        arrow2.visibility = View.INVISIBLE
                        arrow3.visibility = View.INVISIBLE
                        arrow4.visibility = View.INVISIBLE
                        arrow5.visibility = View.INVISIBLE
                        arrow6.visibility = View.VISIBLE
                        arrow7.visibility = View.VISIBLE
                        arrow8.visibility = View.VISIBLE
                        arrow9.visibility = View.VISIBLE
                        arrow10.visibility = View.VISIBLE
                        arrow11.visibility = View.INVISIBLE
                        arrow12.visibility = View.INVISIBLE
                        arrow13.visibility = View.INVISIBLE
                        arrow14.visibility = View.INVISIBLE
                        arrow15.visibility = View.INVISIBLE
                        arrow16.visibility = View.INVISIBLE
                        arrow17.visibility = View.INVISIBLE
                        arrow18.visibility = View.INVISIBLE
                        arrow19.visibility = View.INVISIBLE
                        arrow20.visibility = View.INVISIBLE
                        arrowPosition = 2
                    }
                    3 -> {
                        arrow1.visibility = View.INVISIBLE
                        arrow2.visibility = View.INVISIBLE
                        arrow3.visibility = View.INVISIBLE
                        arrow4.visibility = View.INVISIBLE
                        arrow5.visibility = View.INVISIBLE
                        arrow6.visibility = View.INVISIBLE
                        arrow7.visibility = View.INVISIBLE
                        arrow8.visibility = View.INVISIBLE
                        arrow9.visibility = View.INVISIBLE
                        arrow10.visibility = View.INVISIBLE
                        arrow11.visibility = View.VISIBLE
                        arrow12.visibility = View.VISIBLE
                        arrow13.visibility = View.VISIBLE
                        arrow14.visibility = View.VISIBLE
                        arrow15.visibility = View.VISIBLE
                        arrow16.visibility = View.INVISIBLE
                        arrow17.visibility = View.INVISIBLE
                        arrow18.visibility = View.INVISIBLE
                        arrow19.visibility = View.INVISIBLE
                        arrow20.visibility = View.INVISIBLE
                        arrowPosition = 3
                    }
                    4 -> {
                        arrow1.visibility = View.INVISIBLE
                        arrow2.visibility = View.INVISIBLE
                        arrow3.visibility = View.INVISIBLE
                        arrow4.visibility = View.INVISIBLE
                        arrow5.visibility = View.INVISIBLE
                        arrow6.visibility = View.INVISIBLE
                        arrow7.visibility = View.INVISIBLE
                        arrow8.visibility = View.INVISIBLE
                        arrow9.visibility = View.INVISIBLE
                        arrow10.visibility = View.INVISIBLE
                        arrow11.visibility = View.INVISIBLE
                        arrow12.visibility = View.INVISIBLE
                        arrow13.visibility = View.INVISIBLE
                        arrow14.visibility = View.INVISIBLE
                        arrow15.visibility = View.INVISIBLE
                        arrow16.visibility = View.VISIBLE
                        arrow17.visibility = View.VISIBLE
                        arrow18.visibility = View.VISIBLE
                        arrow19.visibility = View.VISIBLE
                        arrow20.visibility = View.VISIBLE
                        arrowPosition = 4
                    }
                }
            }
        }
    }


    private fun setUpClickListeners() {
        btn_back.setOnClickListener {
            findNavController().navigate(R.id.lecturesView)
        }
        endPicture.setOnClickListener {
            findNavController().navigate(R.id.lectureResultView)
        }
    }

}