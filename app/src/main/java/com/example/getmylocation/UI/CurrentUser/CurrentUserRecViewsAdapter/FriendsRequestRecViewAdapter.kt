package com.example.getmylocation.UI.CurrentUser.CurrentUserRecViewsAdapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.getmylocation.R
import com.example.getmylocation.Domain.Models.NewUser
import kotlinx.android.synthetic.main.friends_requests_list.view.*

class FriendsRequestRecViewAdapter(private val friendRequests : ArrayList<NewUser>, val acceptOrDecline : (Boolean, String, Int)->(Unit)) : RecyclerView.Adapter<FriendsRequestRecViewAdapter.FriendRequestViewHolder>(){

    inner class FriendRequestViewHolder(val view : View) : RecyclerView.ViewHolder(view){

        val senderNameView = view.FriendRequestName
        val senderPicView = view.FriendRequestPic
        val acceptFriend = view.AcceptFriend
        val declineFriend = view.DeclineFriend

        fun bind(uid : String , pos : Int , acceptDeclind : (Boolean , String , Int)->(Unit)){

            acceptFriend.setOnClickListener {
                acceptDeclind(true , uid , this.adapterPosition)
            }
            declineFriend.setOnClickListener {
                acceptDeclind(false , uid ,  this.adapterPosition)
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        return FriendRequestViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.friends_requests_list,parent,false))
    }

    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        holder.senderNameView.text = friendRequests[position].run { "$firstName $lastName" }
        Glide.with(holder.view.context).load(Uri.parse(friendRequests[position].profilePicture)).into(holder.senderPicView)

        holder.bind(friendRequests[position].uid!! ,position,acceptOrDecline)
    }

    override fun getItemCount(): Int {
        return friendRequests.size
    }
}