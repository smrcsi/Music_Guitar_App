package com.example.guitar_music_app.lecture.notesLecture

import android.app.AlertDialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Html
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.guitar_music_app.R
import com.example.guitar_music_app.general.toEditable
import com.example.guitar_music_app.lecture.LectureEvent
import com.example.guitar_music_app.lecture.Note
import kotlinx.android.synthetic.main.chords_fragment.endPicture
import kotlinx.android.synthetic.main.chords_fragment.noteText
import kotlinx.android.synthetic.main.notes_fragment.*

class NotesView : Fragment() {
    private val views = mapOf(
        R.id.viewF to Note.F,
        R.id.viewC to Note.C,
        R.id.viewG_SHARP1 to Note.G_SHARP1,
        R.id.viewD_SHARP1 to Note.D_SHARP1,
        R.id.viewA_SHARP1 to Note.A_SHARP1,
        R.id.viewF2 to Note.F2,
        R.id.viewF_SHARP to Note.F_SHARP,
        R.id.viewC_SHARP to Note.C_SHARP,
        R.id.viewA to Note.A,
        R.id.viewE to Note.E,
        R.id.viewB1 to Note.B1,
        R.id.viewF_SHARP2 to Note.F_SHARP2,
        R.id.viewG to Note.G,
        R.id.viewD to Note.D,
        R.id.viewA_SHARP to Note.A_SHARP,
        R.id.viewF1 to Note.F1,
        R.id.viewC1 to Note.C1,
        R.id.viewG2 to Note.G1,
        R.id.viewG_SHARP to Note.G_SHARP,
        R.id.viewD_SHARP to Note.D_SHARP,
        R.id.viewB to Note.B,
        R.id.viewF_SHARP1 to Note.F_SHARP1,
        R.id.viewC_SHARP1 to Note.C_SHARP1,
        R.id.viewG_SHARP2 to Note.G_SHARP2
    )

    private val sounds = mutableMapOf<Note, Int>()
    private lateinit var soundPool: SoundPool

    private lateinit var viewModel: NotesViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.notes_fragment, container, false)
    }


    private fun displayToast() {
        val toast = Toast.makeText(
            context,
            Html.fromHtml("<font color='#FFFFFF' ><b>" + "Správně" + "</b></font>"),
            Toast.LENGTH_SHORT
        )
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }

    private fun playSound(tone: Note) {
        soundPool.play(
            sounds[tone]!!, 1F, 1F, 0, 0, 1F
        );
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


        views.forEach { (viewId, note) ->
            view.findViewById<View>(viewId)?.setOnTouchListener { view, event ->
                viewModel.noteTouched(
                    note,
                    touched = event.action != MotionEvent.ACTION_UP
                )
                view.performClick()
                true
            }
        }

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

        for (note in Note.values()) {
            sounds[note] = soundPool
                .load(
                    requireContext(),
                    note.tone,
                    1
                )
        }
        for (note in Note.values()) {
            sounds[note] = soundPool
                .load(
                    requireContext(),
                    note.tone,
                    1
                )
        }
        viewModel = ViewModelProvider(
            this,
            NotesInjector(requireActivity().application).provideNotesViewModelFactory()
        )[NotesViewModel::class.java]

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

        setUpClickListeners()

        viewModel.noteState.observe(
            viewLifecycleOwner
        ) { noteState ->
            views.forEach { (viewId, note) ->
                if (noteState.notesTouched.any { it.note == note }) {
                    playSound(note)
                    if (note != noteState.targetNote) {
                        view.findViewById<View>(viewId)?.setBackgroundColor(Color.YELLOW)
                    } else {
                        view.findViewById<View>(viewId)?.setBackgroundColor(Color.GREEN)
                        noteText.setTextColor(Color.GREEN)
                    }
                } else if (noteState.targetNote == note) {
                    view.findViewById<View>(viewId)?.setBackgroundColor(Color.GRAY)
                } else {
                    view.findViewById<View>(viewId)?.setBackgroundColor(Color.TRANSPARENT)
                }
            }
            if (noteState.targetAccomplished) {
                noteText.setTextColor(Color.GREEN)
                displayToast()
                vibrate(millisecond = 5)
            } else {
                noteText.setTextColor(Color.RED)
                noteText.text = noteState.targetNote.toString()
            }
        }

        viewModel.handleEvent(LectureEvent.OnStart)

        endPicture.setOnClickListener {
            viewModel.handleEvent(
                LectureEvent.OnDoneClick(
                    notes_result_text.text.toString()
                )
            )
        }

        viewModel.result.observe(
            viewLifecycleOwner
        ) { result ->
            notes_result_text.text = result.score.toEditable()
        }

        viewModel.updated.observe(
            viewLifecycleOwner
        ) {
            findNavController().navigate(R.id.lectureResultView)
        }

        viewModel.notesNumber.observe(
            viewLifecycleOwner
        ) { result ->
            notes_result_text_view.text = viewModel.notesNumber.value.toString()
        }

    }

    private fun setUpClickListeners() {
        btn_back.setOnClickListener {
            val builder = AlertDialog.Builder(this.activity)
            builder.setMessage((R.string.exit_confirmation))
                .setCancelable(false)
                .setPositiveButton(R.string.yes) { dialog, id ->
                    findNavController().navigate(R.id.lecturesView)
                }.setNegativeButton(R.string.no) { dialog, id ->
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
}
