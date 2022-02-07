package com.example.guitar_music_app.lecture

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.guitar_music_app.R
import kotlinx.android.synthetic.main.chords_fragment.*

class NotesView : Fragment() {
    private lateinit var viewModel: LectureViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.notes_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
            this,
            LectureInjector(requireActivity().application).provideLectureViewModelFactory()
        )[LectureViewModel::class.java]

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

        setUpClickListeners()


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