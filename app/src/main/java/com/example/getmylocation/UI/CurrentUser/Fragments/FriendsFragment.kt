package com.example.getmylocation.UI.CurrentUser.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.getmylocation.CloudMessagingService.NewUserPushNotification
import com.example.getmylocation.CloudMessagingService.sendNotificationOb
import com.example.getmylocation.UI.CurrentUser.CurrentUserRecViewsAdapter.FriendsRecyclerViewAdapter
import com.example.getmylocation.UI.CurrentUser.CurrentUserRecViewsAdapter.FriendsRequestRecViewAdapter
import com.example.getmylocation.Data.Remote.FirebaseGlobals
import com.example.getmylocation.R
import com.example.getmylocation.Domain.Models.NewUser

import kotlinx.android.synthetic.main.fragment_friends.*
import com.example.getmylocation.UI.CurrentUser.ViewModels.FriendsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class FriendsFragment : Fragment() {

    lateinit var friendRequests : ArrayList<NewUser>
    lateinit var friends : ArrayList<NewUser>
    lateinit var userUID : String
    lateinit var coroScope : CoroutineScope
    val friendsViewModel : FriendsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friends , container , false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coroScope = CoroutineScope(Job()+Dispatchers.IO)
        userUID = FirebaseGlobals.auth.currentUser!!.uid

        ShowHideFriends.setOnCheckedChangeListener { compoundButton, b ->
            FriendsRecView.visibility = if (b) View.GONE else View.VISIBLE
        }

        ShowHideFriendsRequests.setOnCheckedChangeListener { compoundButton, b ->
            FriendsRequestsRecView.visibility = if (b) View.GONE else View.VISIBLE
        }

        friends = ArrayList()
        friendsViewModel.friends.observe(this){friendsRes->
            friendsRes.forEach { friends.add(it) }
            with(FriendsRecView){
                layoutManager = LinearLayoutManager(requireContext())
                itemAnimator = DefaultItemAnimator()
                adapter = FriendsRecyclerViewAdapter(friends)

            }
        }

        var indexOfObserver = 0
        var tokenOfObserver = ""
        friendRequests = ArrayList()
        friendsViewModel.friendRequests.observe(this){friendsRes->
            friendsRes.forEach { friendRequests.add(it) }
            with(FriendsRequestsRecView){
                layoutManager = LinearLayoutManager(requireContext())
                itemAnimator = DefaultItemAnimator()
                adapter = FriendsRequestRecViewAdapter(friendRequests){choice , uid , index->
                    FirebaseGlobals.fireStoreCollection.document(uid).get()
                        .addOnSuccessListener {
                            indexOfObserver = index
                            val token = it["token"] as String
                            if (choice){
                                tokenOfObserver = token
                                acceptFriend(uid)
                            }else{
                                declineFriend(uid)
                            }
                        }
                }
            }
        }
        friendsViewModel.apply {
            this.getFriends()
            this.getFriendRequests()
        }

        friendsViewModel.declinedSelfFriendLiveDataRes.observe(this){
            if (it){
                friendRequests.removeAt(indexOfObserver)
                FriendsRequestsRecView.adapter!!.notifyItemRemoved(indexOfObserver)
                FirebaseGlobals().apply {
                    updateFriendsRequest()
                }
            }
        }

        friendsViewModel.acceptedSelfFriendLiveDataRes.observe(this){
            if (it){
                friendRequests.removeAt(indexOfObserver)
                FriendsRequestsRecView.adapter!!.notifyItemRemoved(indexOfObserver)

                FirebaseGlobals().apply {
                    updateFriendsRequest()
                }
                coroScope.launch {
                    if (tokenOfObserver.isNotBlank()){
                        sendFriendAcceptedNotification(FirebaseGlobals.userInfo!! , tokenOfObserver)
                    }
                }
            }
        }
    }

    private fun acceptFriend(uid : String){
        with(friendsViewModel){
            updateAcceptedFriend(uid)
            updateSelfAcceptFriend(uid)
        }
    }

    private fun declineFriend(uid : String){
        with(friendsViewModel){
            updateDeclinedFriend(uid)
            updateSelfDeclinedFriend(uid)
        }
    }

    private suspend fun sendFriendAcceptedNotification(fromUser : NewUser, toToken : String){
        sendNotificationOb.sendNotification.pushNotification(
            NewUserPushNotification(fromUser.apply { topic="FRIEND_ACCEPTED" }, toToken))
    }

    override fun onDestroy() {
        super.onDestroy()
        coroScope.cancel()
    }
}
