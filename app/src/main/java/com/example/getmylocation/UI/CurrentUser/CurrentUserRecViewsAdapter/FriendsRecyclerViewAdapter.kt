package com.example.getmylocation.UI.CurrentUser.CurrentUserRecViewsAdapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.getmylocation.R
import com.example.getmylocation.Domain.Models.NewUser
import kotlinx.android.synthetic.main.friends_list.view.*

class FriendsRecyclerViewAdapter(val Friends : List<NewUser>) : RecyclerView.Adapter<FriendsRecyclerViewAdapter.FriendsViewHolder>() {

    class FriendsViewHolder(val view : View) : RecyclerView.ViewHolder(view){

        val nameTextView = view.NameOfFriend
        val imageView = view.ImageOfFriend
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        return FriendsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.friends_list,parent,false))
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {

        holder.nameTextView.text = Friends[position].run { "$firstName $lastName" }
        Glide.with(holder.view.context).load(Uri.parse(Friends[position].profilePicture)).into(holder.imageView)

    }

    override fun getItemCount(): Int {
        return Friends.size
    }
}