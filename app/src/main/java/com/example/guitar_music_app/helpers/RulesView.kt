package com.example.guitar_music_app.helpers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.guitar_music_app.R
import kotlinx.android.synthetic.main.rules_fragment.*

class RulesView : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rules_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        setUpClickListeners()

        ArrayAdapter.createFromResource(
            activity?.applicationContext!!,
            R.array.lectures_array,
            R.layout.simple_spinner_item_top
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            rules_spinner.adapter = adapter
        }
        rules_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position) {
                    0-> text_rules.text = context?.getString(R.string.notes_text)
                    1-> text_rules.text = context?.getString(R.string.chords_text)
                    2-> text_rules.text = context?.getString(R.string.rhythm_text)
                }
            }
        }

    }

    private fun setUpClickListeners() {
        btn_back.setOnClickListener {
            findNavController().navigate(R.id.lecturesView)
        }
    }

}