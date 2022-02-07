package com.example.guitar_music_app.lecture


import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.media.MediaPlayer
import android.os.*
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.guitar_music_app.R
import kotlinx.android.synthetic.main.chords1_fragment.*
import java.lang.Exception
import java.util.*
import kotlin.concurrent.timerTask


class LectureView : Fragment() {
    private val views = mapOf(
        R.id.viewF to Note.F, R.id.viewC to Note.C,
        R.id.viewG_SHARP1 to Note.G_SHARP1, R.id.viewD_SHARP1 to Note.D_SHARP1,
        R.id.viewA_SHARP1 to Note.A_SHARP1, R.id.viewF2 to Note.F2,
        R.id.viewF_SHARP to Note.F_SHARP, R.id.viewC_SHARP to Note.C_SHARP, R.id.viewA to Note.A,
        R.id.viewE to Note.E, R.id.viewB1 to Note.B1, R.id.viewF_SHARP2 to Note.F_SHARP2,
        R.id.viewG to Note.G, R.id.viewD to Note.D, R.id.viewA_SHARP to Note.A_SHARP,
        R.id.viewF1 to Note.F1, R.id.viewC1 to Note.C1, R.id.viewG2 to Note.G2,
        R.id.viewG_SHARP to Note.G_SHARP, R.id.viewD_SHARP to Note.D_SHARP, R.id.viewB to Note.B,
        R.id.viewF_SHARP1 to Note.F_SHARP1, R.id.viewC_SHARP1 to Note.C_SHARP1,
        R.id.viewG_SHARP2 to Note.G_SHARP2
    )

    private lateinit var viewModel: LectureViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.chords1_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
            this,
            LectureInjector(requireActivity().application).provideLectureViewModelFactory()
        )
            .get(LectureViewModel::class.java)

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;


        setUpClickListeners()

        viewModel.state.observe(this, { state ->
            views.forEach { (viewId, note) ->
                if (state.buttonsTouched.any { it.note == note }) {
                    view?.findViewById<View>(viewId)?.setBackgroundColor(Color.YELLOW)

                    if (state.isChordValid) {
                        view?.findViewById<View>(viewId)?.setBackgroundColor(Color.GREEN)
                        chordText.setTextColor(Color.GREEN)
                        vibrate(millisecond = 1)
                        //TODO-TOAST JE MOC DLOUHEJ A TRVA NEZ ZMIZI + Zmenit aby neprekazel
                        Toast.makeText(
                            context, "Správně",
                            Toast.LENGTH_SHORT
                        ).show()
                        playSound()
                    }

                } else if (state.assistant && state.chord.notes.contains(note)) {
                    if (state.chord.notes.contains(note)) {
                        view?.findViewById<View>(viewId)?.setBackgroundColor(Color.LTGRAY)
                    }
                } else {
                    view?.findViewById<View>(viewId)?.setBackgroundColor(Color.TRANSPARENT)
                    chordText.setTextColor(Color.RED)
                }

            }
        })
        //Na zacatku posle prvni akord
        viewModel.chordTextChange.observe(this, {
            chordText.text = viewModel.chordTextChange.value

        })

        viewModel.handleEvent(LectureEvent.OnStart)
    }

    private fun playSound() {
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

    private fun vibrate(millisecond: Long) {
        val vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Start without a delay (0ms)
        // Vibrate duration (100ms)
        // Sleep between vibrations (1000ms)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(millisecond, 255))
        } else {
            vibrator.vibrate(millisecond)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swtch_asistant.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.assistantSet()
            } else {
                viewModel.assistantSet()
            }
        }
//TODO-jakmile odslidujeme, tak by se melo tlacitko prestat drzet
        viewF.setOnTouchListener { v, event ->
            val note = views[viewF.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            view.playSoundEffect(android.view.SoundEffectConstants.CLICK);
            v.performClick()
            false
        }
        viewF.setOnTouchListener { v, event ->
            val note = views[viewF.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }
        viewC.setOnTouchListener { v, event ->
            val note = views[viewC.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewC.setOnTouchListener { v, event ->
            val note = views[viewC.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }

        viewG_SHARP1.setOnTouchListener { v, event ->
            val note = views[viewG_SHARP1.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewG_SHARP1.setOnTouchListener { v, event ->
            val note = views[viewG_SHARP1.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }

        viewD_SHARP1.setOnTouchListener { v, event ->
            val note = views[viewD_SHARP1.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewD_SHARP1.setOnTouchListener { v, event ->
            val note = views[viewD_SHARP1.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }
        viewA_SHARP1.setOnTouchListener { v, event ->
            val note = views[viewA_SHARP1.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewA_SHARP1.setOnTouchListener { v, event ->
            val note = views[viewA_SHARP1.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }
        viewF2.setOnTouchListener { v, event ->
            val note = views[viewF2.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewF2.setOnTouchListener { v, event ->
            val note = views[viewF2.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }
        viewF_SHARP.setOnTouchListener { v, event ->
            val note = views[viewF_SHARP.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewF_SHARP.setOnTouchListener { v, event ->
            val note = views[viewF_SHARP.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }
        viewC_SHARP.setOnTouchListener { v, event ->
            val note = views[viewC_SHARP.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewC_SHARP.setOnTouchListener { v, event ->
            val note = views[viewC_SHARP.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }

        viewA.setOnTouchListener { v, event ->
            val note = views[viewA.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewA.setOnTouchListener { v, event ->
            val note = views[viewA.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }
        viewE.setOnTouchListener { v, event ->
            val note = views[viewE.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewE.setOnTouchListener { v, event ->
            val note = views[viewE.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }
        viewB1.setOnTouchListener { v, event ->
            val note = views[viewB1.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewB1.setOnTouchListener { v, event ->
            val note = views[viewB1.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }
        viewF_SHARP2.setOnTouchListener { v, event ->
            val note = views[viewF_SHARP2.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewF_SHARP2.setOnTouchListener { v, event ->
            val note = views[viewF_SHARP2.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }
        viewG.setOnTouchListener { v, event ->
            val note = views[viewG.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewG.setOnTouchListener { v, event ->
            val note = views[viewG.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }
        viewD.setOnTouchListener { v, event ->
            val note = views[viewD.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewD.setOnTouchListener { v, event ->
            val note = views[viewD.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }
        viewA_SHARP.setOnTouchListener { v, event ->
            val note = views[viewA_SHARP.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewA_SHARP.setOnTouchListener { v, event ->
            val note = views[viewA_SHARP.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }

        viewF1.setOnTouchListener { v, event ->
            val note = views[viewF1.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewF1.setOnTouchListener { v, event ->
            val note = views[viewF1.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }
        viewC1.setOnTouchListener { v, event ->
            val note = views[viewC1.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewC1.setOnTouchListener { v, event ->
            val note = views[viewC1.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }
        viewG2.setOnTouchListener { v, event ->
            val note = views[viewG2.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewG2.setOnTouchListener { v, event ->
            val note = views[viewG2.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }
        viewG_SHARP.setOnTouchListener { v, event ->
            val note = views[viewG_SHARP.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewG_SHARP.setOnTouchListener { v, event ->
            val note = views[viewG_SHARP.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }
        viewD_SHARP.setOnTouchListener { v, event ->
            val note = views[viewD_SHARP.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewD_SHARP.setOnTouchListener { v, event ->
            val note = views[viewD_SHARP.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }
        viewB.setOnTouchListener { v, event ->
            val note = views[viewB.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewB.setOnTouchListener { v, event ->
            val note = views[viewB.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }
        viewF_SHARP1.setOnTouchListener { v, event ->
            val note = views[viewF_SHARP1.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewF_SHARP1.setOnTouchListener { v, event ->
            val note = views[viewF_SHARP1.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }
        viewC_SHARP1.setOnTouchListener { v, event ->
            val note = views[viewC_SHARP1.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewC_SHARP1.setOnTouchListener { v, event ->
            val note = views[viewC_SHARP1.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

            v.performClick()
            true
        }
        viewG_SHARP2.setOnTouchListener { v, event ->
            val note = views[viewG_SHARP2.id]
            viewModel.buttonTouched(note!!, touched = event.action == MotionEvent.ACTION_DOWN)
            v.performClick()
            true
        }

        viewG_SHARP2.setOnTouchListener { v, event ->
            val note = views[viewG_SHARP2.id]
            viewModel.buttonTouched(note!!, touched = event.action != MotionEvent.ACTION_UP)

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