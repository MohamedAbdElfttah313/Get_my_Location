package com.example.getmylocation.UI.CurrentUser.CurrentUserRecViewsAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.getmylocation.R
import kotlinx.android.synthetic.main.inflated_activities_list.view.*

class ActivitiesAndHealthRecyclerViewAdapter(val activities : List<String>) : RecyclerView.Adapter<ActivitiesAndHealthRecyclerViewAdapter.ActivitiesViewHolder>() {

    class ActivitiesViewHolder(val view : View) : RecyclerView.ViewHolder(view){
        val content = view.ContentActivitiesTextView
        val time = view.TimestampActivitiesTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivitiesViewHolder {
        return ActivitiesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflated_activities_list, parent , false))
    }

    override fun onBindViewHolder(holder: ActivitiesViewHolder, position: Int) {
        holder.content.text = activities[position].substringAfter('_')
        holder.time.text = activities[position].substringBefore('_')
    }

    override fun getItemCount(): Int {
       return activities.size
    }
}