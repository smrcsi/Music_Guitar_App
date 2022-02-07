package com.example.guitar_music_app.results.resultDetail

import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.guitar_music_app.R
import com.example.guitar_music_app.general.toEditable
import com.example.guitar_music_app.makeToast
import com.example.guitar_music_app.startWithFade
import kotlinx.android.synthetic.main.result_detail_fragment.*
import androidx.navigation.fragment.findNavController

class ResultDetailView : Fragment() {

    private lateinit var viewModel: ResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.result_detail_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() {
        super.onStart()

        viewModel = ViewModelProvider(
            this,
            ResultDetailInjector(requireActivity().application).provideResultViewModelFactory()
        ).get(
            ResultViewModel::class.java
        )

        showLoadingState()

        imb_toolbar_done.setOnClickListener {
            viewModel.handleEvent(
                ResultDetailEvent.OnDoneClick(
                    edt_result_detail_text.text.toString()
                )
            )
        }

        imb_toolbar_delete.setOnClickListener { viewModel.handleEvent(ResultDetailEvent.OnDeleteClick) }

        observeViewModel()

        (result_detail_fragment.background as AnimationDrawable).startWithFade()

        viewModel.handleEvent(
            ResultDetailEvent.OnStart(
                //note NoteDetailViewArgs is genereted via Navigation component
                ResultDetailViewArgs.fromBundle(requireArguments()).resultId
            )
        )
    }

    private fun observeViewModel() {
        viewModel.error.observe(
            viewLifecycleOwner,
            Observer { errorMessage ->
                showErrorState(errorMessage)
            }
        )

        viewModel.result.observe(
            viewLifecycleOwner,
            Observer { result ->
                edt_result_detail_text.text = result.contents.toEditable()
            }
        )

        viewModel.updated.observe(
            viewLifecycleOwner,
            Observer {
                findNavController().navigate(R.id.resultListView)
            }
        )

        viewModel.deleted.observe(
            viewLifecycleOwner,
            Observer {
                findNavController().navigate(R.id.resultListView)
            }
        )

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.resultListView)
        }
    }

    private fun showErrorState(errorMessage: String?) {
        makeToast(errorMessage!!)
        findNavController().navigate(R.id.resultListView)
    }

    private fun showLoadingState() {
        (imv_result_detail_satellite.drawable as AnimationDrawable).start()
    }
}