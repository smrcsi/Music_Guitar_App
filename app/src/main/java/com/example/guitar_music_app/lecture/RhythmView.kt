package com.example.guitar_music_app.lecture

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.guitar_music_app.R
import kotlinx.android.synthetic.main.chords_fragment.*
import kotlinx.android.synthetic.main.chords_fragment.btn_back
import kotlinx.android.synthetic.main.chords_fragment.img_guitar
import kotlinx.android.synthetic.main.rhythm_fragment.*
import kotlinx.coroutines.launch
import androidx.core.view.GestureDetectorCompat
import android.view.animation.Animation
import java.lang.Exception
import kotlin.math.abs
import android.view.MotionEvent
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RhythmView : Fragment() {

    private lateinit var mDetector: GestureDetectorCompat
    private lateinit var viewModel: LectureViewModel

    private var arrow1state = false
    private var arrow2state = false
    private var arrow3state = false
    private var arrow4state = false
    private var arrow5state = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rhythm_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
            this,
            LectureInjector(requireActivity().application).provideLectureViewModelFactory()
        )[LectureViewModel::class.java]

        mDetector = GestureDetectorCompat(context, MyGestureListener(viewModel))

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        setUpClickListeners()


        viewModel.rhythmState.observe(viewLifecycleOwner, { rhythmState ->
            if (!arrow1state && !arrow2state && !arrow3state && !arrow4state && !arrow5state) {
                if (rhythmState.isFlingValid) {
                    arrow1state = false
                    arrow1.setBackgroundColor(Color.RED)
                    stringField.setBackgroundColor(Color.GREEN)
                    lifecycleScope.launch { playSound() }
                } else {
                    stringField.setBackgroundColor(Color.RED)
                    lifecycleScope.launch { playSecond() }
                    arrow1state = true
                    arrow1.setBackgroundColor(Color.GREEN)
                }
            } else if (arrow1state && !arrow2state && !arrow3state && !arrow4state && !arrow5state) {
                if (rhythmState.isFlingValid) {
                    arrow1state = false
                    arrow2state = false
                    arrow1.setBackgroundColor(Color.RED)
                    arrow2.setBackgroundColor(Color.RED)
                    stringField.setBackgroundColor(Color.GREEN)
                    lifecycleScope.launch { playSound() }
                } else {
                    stringField.setBackgroundColor(Color.RED)
                    lifecycleScope.launch { playSecond() }
                    arrow2state = true
                    arrow2.setBackgroundColor(Color.GREEN)
                }
            } else if (arrow1state && arrow2state && !arrow3state && !arrow4state && !arrow5state) {
                if (rhythmState.isFlingValid) {
                    arrow1state = false
                    arrow2state = false
                    arrow3state = false
                    arrow1.setBackgroundColor(Color.RED)
                    arrow2.setBackgroundColor(Color.RED)
                    arrow3.setBackgroundColor(Color.RED)
                    stringField.setBackgroundColor(Color.GREEN)
                    lifecycleScope.launch { playSound() }
                } else {
                    stringField.setBackgroundColor(Color.RED)
                    lifecycleScope.launch { playSecond() }
                    arrow3state = true
                    arrow3.setBackgroundColor(Color.GREEN)
                }
            }
            else if (arrow1state && arrow2state && arrow3state && !arrow4state && !arrow5state) {
                if (rhythmState.isFlingValid) {
                    arrow4state = true
                    arrow4.setBackgroundColor(Color.GREEN)
                    stringField.setBackgroundColor(Color.GREEN)
                    lifecycleScope.launch { playSound() }
                } else {
                    stringField.setBackgroundColor(Color.RED)
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
            }
            else if (arrow1state && arrow2state && arrow3state && arrow4state && !arrow5state) {
                if (rhythmState.isFlingValid) {
                    arrow1state = false
                    arrow2state = false
                    arrow3state = false
                    arrow4state = false

                    arrow1.setBackgroundColor(Color.RED)
                    arrow2.setBackgroundColor(Color.RED)
                    arrow3.setBackgroundColor(Color.RED)
                    arrow4.setBackgroundColor(Color.RED)
                    arrow5.setBackgroundColor(Color.RED)
                    stringField.setBackgroundColor(Color.GREEN)
                    lifecycleScope.launch { playSound() }
                } else {
                    stringField.setBackgroundColor(Color.RED)
                    lifecycleScope.launch { playSecond() }
                    arrow5state = true
                    arrow5.setBackgroundColor(Color.GREEN)
                }
            }
            else if (arrow1state && arrow2state && arrow3state && arrow4state && arrow5state) {
                arrow1state = false
                arrow2state = false
                arrow3state = false
                arrow4state = false
                arrow5state = false

                arrow1.setBackgroundColor(Color.TRANSPARENT)
                arrow2.setBackgroundColor(Color.TRANSPARENT)
                arrow3.setBackgroundColor(Color.TRANSPARENT)
                arrow4.setBackgroundColor(Color.TRANSPARENT)
                arrow5.setBackgroundColor(Color.TRANSPARENT)
                stringField.setBackgroundColor(Color.TRANSPARENT)
                Toast.makeText(context, "SPRAVNE", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private suspend fun playSound() {
        withContext(Dispatchers.IO) {
            val mediaPlayer = MediaPlayer.create(activity, R.raw.a_sharp1)
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
            val mediaPlayer = MediaPlayer.create(activity, R.raw.a_sharp1)
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


    }


    private fun setUpClickListeners() {
        btn_back.setOnClickListener {
            findNavController().navigate(R.id.lecturesView)
        }
        img_guitar.setOnClickListener {
            findNavController().navigate(R.id.lectureResult)
        }
    }

}