package com.example.getmylocation.UI.CurrentUser.CurrentUserRecViewsAdapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.getmylocation.R
import com.example.getmylocation.Domain.Models.NewUser
import kotlinx.android.synthetic.main.search_inflated.view.*


class SearchRecyclerViewAdapter(val Users : List<NewUser>, val Friends : List<String>, val pendingFriends : List<String>, val sendOrCancelFriendRequest : (String, Boolean)->(Unit)) : RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder>() {

     class SearchViewHolder(val view : View) : RecyclerView.ViewHolder(view){
        val nameTextView = view.NameOfSearchedUser
        val imageView = view.ImageOfSearchedUser
        val addFriend = view.SearchAddFriendButton

        fun bind(uid : String ,alreadyFriend : Boolean ,alreadyPended : Boolean, sendOrCancel : (String , Boolean)->(Unit)){

            addFriend.isEnabled = alreadyFriend.not()
            addFriend.isChecked = alreadyPended

            addFriend.setOnClickListener {
                sendOrCancel(uid , addFriend.isChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
       return SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_inflated,parent,false))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

        val alreadyFriend = Friends.contains(Users[position].uid)
        val alreadyPended = pendingFriends.contains(Users[position].uid)

        holder.nameTextView.text = Users[position].run { "$firstName $lastName" }
        Glide.with(holder.view.context).load(Uri.parse(Users[position].profilePicture)).into(holder.imageView)

        holder.bind(Users[position].uid!! , alreadyFriend , alreadyPended , sendOrCancelFriendRequest)


    }

    override fun getItemCount(): Int {
        return Users.size
    }
}