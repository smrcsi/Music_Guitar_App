package com.example.guitar_music_app.lecture.notesLecture

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Html
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.guitar_music_app.R
import com.example.guitar_music_app.general.toEditable
import com.example.guitar_music_app.lecture.LectureEvent
import com.example.guitar_music_app.lecture.Note
import kotlinx.android.synthetic.main.chords_fragment.img_guitar
import kotlinx.android.synthetic.main.chords_fragment.noteText
import kotlinx.android.synthetic.main.chords_fragment.viewA
import kotlinx.android.synthetic.main.chords_fragment.viewA_SHARP
import kotlinx.android.synthetic.main.chords_fragment.viewA_SHARP1
import kotlinx.android.synthetic.main.chords_fragment.viewB
import kotlinx.android.synthetic.main.chords_fragment.viewB1
import kotlinx.android.synthetic.main.chords_fragment.viewC
import kotlinx.android.synthetic.main.chords_fragment.viewC1
import kotlinx.android.synthetic.main.chords_fragment.viewC_SHARP
import kotlinx.android.synthetic.main.chords_fragment.viewC_SHARP1
import kotlinx.android.synthetic.main.chords_fragment.viewD
import kotlinx.android.synthetic.main.chords_fragment.viewD_SHARP
import kotlinx.android.synthetic.main.chords_fragment.viewD_SHARP1
import kotlinx.android.synthetic.main.chords_fragment.viewE
import kotlinx.android.synthetic.main.chords_fragment.viewF
import kotlinx.android.synthetic.main.chords_fragment.viewF1
import kotlinx.android.synthetic.main.chords_fragment.viewF2
import kotlinx.android.synthetic.main.chords_fragment.viewF_SHARP
import kotlinx.android.synthetic.main.chords_fragment.viewF_SHARP1
import kotlinx.android.synthetic.main.chords_fragment.viewF_SHARP2
import kotlinx.android.synthetic.main.chords_fragment.viewG
import kotlinx.android.synthetic.main.chords_fragment.viewG2
import kotlinx.android.synthetic.main.chords_fragment.viewG_SHARP
import kotlinx.android.synthetic.main.chords_fragment.viewG_SHARP1
import kotlinx.android.synthetic.main.chords_fragment.viewG_SHARP2
import kotlinx.android.synthetic.main.notes_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class NotesView : Fragment() {
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

    private val tones = mapOf(
        Note.F to R.raw.f, Note.C to R.raw.c,
        Note.G_SHARP1 to R.raw.g_sharp_1, Note.D_SHARP1 to R.raw.d_sharp_1,
        Note.A_SHARP1 to R.raw.a_sharp_1, Note.F2 to R.raw.f2,

        Note.F_SHARP to R.raw.f_sharp, Note.C_SHARP to R.raw.c_sharp, Note.A to R.raw.a,
        Note.E to R.raw.e, Note.B1 to R.raw.b1, Note.F_SHARP2 to R.raw.f_sharp_2,

        Note.G to R.raw.g, Note.D to R.raw.d, Note.A_SHARP to R.raw.a_sharp,
        Note.F1 to R.raw.f1, Note.C1 to R.raw.c1, Note.G2 to R.raw.g1,

        Note.G_SHARP to R.raw.g_sharp, Note.D_SHARP to R.raw.d_sharp, Note.B to R.raw.b,
        Note.F_SHARP1 to R.raw.f_sharp_1, Note.C_SHARP1 to R.raw.c_sharp_1,
        Note.G_SHARP2 to R.raw.g_sharp_2
    )

    private lateinit var viewModel: NotesViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.notes_fragment, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
            this,
            NotesInjector(requireActivity().application).provideNotesViewModelFactory()
        )[NotesViewModel::class.java]

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

        setUpClickListeners()


        viewModel.noteState.observe(viewLifecycleOwner, { noteState ->
            views.forEach { (viewId, note) ->
                tones.forEach { (note1, tone) ->
                    if (noteState.notesTouched.any { it.note == note }) {
                        if (noteState.notesTouched.any { it.note == note1 }) {
                            lifecycleScope.launch { playSound(tone) }
                        }
                        if (!noteState.isNoteValid && !noteState.notePlayed) {
                            view?.findViewById<View>(viewId)?.setBackgroundColor(Color.YELLOW)
                            println("dala se zluta")

                        }
                    } else {
                        view?.findViewById<View>(viewId)?.setBackgroundColor(Color.TRANSPARENT)
                    }
                    if ((noteState.note == note)) {
                        if (noteState.notePlayed) {
                            view?.findViewById<View>(viewId)?.setBackgroundColor(Color.GREEN)
                            noteText.setTextColor(Color.GREEN)
                            println("Dala se zelena")

                            displayToast()
                            lifecycleScope.launch { vibrate(millisecond = 5) }
                            noteText.setTextColor(Color.GREEN)
                        } else {
                            noteText.setTextColor(Color.RED)
                        }
                    }
                    if (noteState.targetNote == note) {
                        view?.findViewById<View>(viewId)?.setBackgroundColor(Color.GRAY)
                    }
                }
            }
        }
        )

        viewModel.handleEvent(LectureEvent.OnStart)

        img_guitar.setOnClickListener {
            viewModel.handleEvent(
                LectureEvent.OnDoneClick(
                    notes_result_text.text.toString()
                )
            )
        }

        viewModel.result.observe(
            viewLifecycleOwner,
            { result ->
                notes_result_text.text = result.score.toEditable()
            }
        )

        viewModel.updated.observe(
            viewLifecycleOwner,
            {
                findNavController().navigate(R.id.lectureResultView)
            }
        )

        viewModel.notesNumber.observe(
            viewLifecycleOwner,
            { result ->
                notes_result_text_view.text = viewModel.notesNumber.value.toString()
            }
        )

    }

    private fun displayToast() {
//Toast nejde v backgroundu
        val toast = Toast.makeText(
            context,
            Html.fromHtml("<font color='#FFFFFF' ><b>" + "Správně" + "</b></font>"),
            Toast.LENGTH_SHORT
        )
        toast.setGravity(Gravity.TOP, 0, 0)

        val toastView = toast.view
        toastView!!.setBackgroundResource(R.drawable.toast_drawable)
        toast.show()
    }

    private suspend fun playSound(tone: Int) {
        withContext(Dispatchers.IO) {
            val mediaPlayer = MediaPlayer.create(activity, tone)
            mediaPlayer.start()
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

    private suspend fun vibrate(millisecond: Long) {
        withContext(Dispatchers.IO) {
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
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//TODO-jakmile odslidujeme, tak by se melo tlacitko prestat drzet
        viewF.setOnTouchListener { v, event ->
            lifecycleScope.launch {
                val note = views[viewF.id]
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }

            v.performClick()
            true
        }
        viewC.setOnTouchListener { v, event ->
            val note = views[viewC.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }

        viewG_SHARP1.setOnTouchListener { v, event ->
            val note = views[viewG_SHARP1.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }

        viewD_SHARP1.setOnTouchListener { v, event ->

            val note = views[viewD_SHARP1.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }

        viewA_SHARP1.setOnTouchListener { v, event ->
            val note = views[viewA_SHARP1.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }

        viewF2.setOnTouchListener { v, event ->
            val note = views[viewF2.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }

        viewF_SHARP.setOnTouchListener { v, event ->
            val note = views[viewF_SHARP.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }

        viewC_SHARP.setOnTouchListener { v, event ->
            val note = views[viewC_SHARP.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }



        viewA.setOnTouchListener { v, event ->
            val note = views[viewA.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }


        viewE.setOnTouchListener { v, event ->
            val note = views[viewE.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }


        viewB1.setOnTouchListener { v, event ->
            val note = views[viewB1.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }


        viewF_SHARP2.setOnTouchListener { v, event ->
            val note = views[viewF_SHARP2.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }

        viewG.setOnTouchListener { v, event ->
            val note = views[viewG.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }

        viewD.setOnTouchListener { v, event ->
            val note = views[viewD.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }

        viewA_SHARP.setOnTouchListener { v, event ->
            val note = views[viewA_SHARP.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }



        viewF1.setOnTouchListener { v, event ->
            val note = views[viewF1.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }


        viewC1.setOnTouchListener { v, event ->
            val note = views[viewC1.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }


        viewG2.setOnTouchListener { v, event ->
            val note = views[viewG2.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }

        viewG_SHARP.setOnTouchListener { v, event ->
            val note = views[viewG_SHARP.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }


        viewD_SHARP.setOnTouchListener { v, event ->
            val note = views[viewD_SHARP.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }


        viewB.setOnTouchListener { v, event ->
            val note = views[viewB.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }


        viewF_SHARP1.setOnTouchListener { v, event ->
            val note = views[viewF_SHARP1.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }


        viewC_SHARP1.setOnTouchListener { v, event ->
            val note = views[viewC_SHARP1.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
            }
            v.performClick()
            true
        }


        viewG_SHARP2.setOnTouchListener { v, event ->
            val note = views[viewG_SHARP2.id]
            lifecycleScope.launch {
                viewModel.noteTouched(
                    note!!,
                    touched = event.action != MotionEvent.ACTION_UP
                )
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
            findNavController().navigate(R.id.lectureResultView)
        }
    }
}