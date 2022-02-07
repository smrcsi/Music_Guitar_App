package com.example.guitar_music_app.results.resultList

import androidx.recyclerview.widget.DiffUtil
import com.example.guitar_music_app.results.Result

class ResultDiffUtilCallback : DiffUtil.ItemCallback<Result>(){
    override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem.creationDate == newItem.creationDate
    }

    override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem.creationDate == newItem.creationDate
    }
}