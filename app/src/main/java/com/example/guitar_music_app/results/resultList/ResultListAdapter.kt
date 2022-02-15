package com.example.guitar_music_app.results.resultList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.guitar_music_app.R
import com.example.guitar_music_app.results.Result
import kotlinx.android.synthetic.main.result_item.view.*

class ResultListAdapter(val event: MutableLiveData<ResultListEvent> = MutableLiveData()): ListAdapter<Result, ResultListAdapter.ResultViewHolder>(ResultDiffUtilCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ResultViewHolder(
            inflater.inflate(R.layout.result_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        getItem(position).let { result ->
            holder.content.text = result.score
            holder.date.text = result.creationDate

            holder.itemView.setOnClickListener {
                event.value = ResultListEvent.OnResultItemClick(position)
            }
        }
    }


    class ResultViewHolder(root: View): RecyclerView.ViewHolder(root){
        var content: TextView = root.lbl_message
        var date: TextView = root.lbl_date_and_time
    }
}


