package com.example.guitar_music_app.results.resultList

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.guitar_music_app.makeToast
import com.example.guitar_music_app.startWithFade
import androidx.navigation.fragment.findNavController
import com.example.guitar_music_app.R
import kotlinx.android.synthetic.main.result_list_fragment.*

class ResultListView : Fragment() {

    private lateinit var viewModel: ResultListViewModel
    private lateinit var adapter: ResultListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.result_list_fragment, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //THIS IS IMPORTANT!!!
        rec_list_fragment.adapter = null
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
            this,
            ResultListInjector(requireActivity().application).provideResultListViewModelFactory()
        ).get(
            ResultListViewModel::class.java
        )

        (imv_space_background.drawable as AnimationDrawable).startWithFade()

        showLoadingState()
        setUpAdapter()
        observeViewModel()

        fab_create_new_item.setOnClickListener {
//            val direction = ResultListViewDirections.actionResultListViewToResultDetailView("")
//            findNavController().navigate(direction)
        }

        imv_toolbar_auth.setOnClickListener {
            findNavController().navigate(R.id.loginView)
        }

        homepageButton.setOnClickListener{findNavController().navigate(R.id.homepageView)}

        viewModel.handleEvent(
            ResultListEvent.OnStart
        )
    }

    private fun setUpAdapter() {
        adapter = ResultListAdapter()
        adapter.event.observe(
            viewLifecycleOwner,
            Observer {
                viewModel.handleEvent(it)
            }
        )

        rec_list_fragment.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.error.observe(
            viewLifecycleOwner,
            Observer { errorMessage ->
                showErrorState(errorMessage)
            }
        )

        viewModel.resultList.observe(
            viewLifecycleOwner,
            Observer { resultList ->
                adapter.submitList(resultList)

                if (resultList.isNotEmpty()) {
                    (imv_satellite_animation.drawable as AnimationDrawable).stop()
                    imv_satellite_animation.visibility = View.INVISIBLE
                    rec_list_fragment.visibility = View.VISIBLE
                }
            }
        )

//        viewModel.editResult.observe(
//            viewLifecycleOwner,
//            Observer { resultId ->
//                startResultDetailWithArgs(resultId)
//            }
//        )
    }

//    private fun startResultDetailWithArgs(resultId: String) = findNavController().navigate(
////        ResultListViewDirections.actionResultListViewToResultDetailView(resultId)
//    )


    private fun showErrorState(errorMessage: String?) = makeToast(errorMessage!!)


    private fun showLoadingState() = (imv_satellite_animation.drawable as AnimationDrawable).start()

}