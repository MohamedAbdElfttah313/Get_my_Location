package com.example.getmylocation.UI.CurrentUser.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.getmylocation.CloudMessagingService.NewUserPushNotification
import com.example.getmylocation.CloudMessagingService.sendNotificationOb
import com.example.getmylocation.UI.CurrentUser.CurrentUserRecViewsAdapter.SearchRecyclerViewAdapter
import com.example.getmylocation.Data.Remote.FirebaseGlobals
import com.example.getmylocation.Data.Remote.FirebaseGlobals.Constants.*
import com.example.getmylocation.R
import com.example.getmylocation.Domain.Models.NewUser
import com.example.getmylocation.UI.CurrentUser.ViewModels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.*


@AndroidEntryPoint
class SearchFragment : Fragment() {

    lateinit var userUID : String
    lateinit var coroScope : CoroutineScope
    val searchViewModel : SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search , container , false)
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coroScope = CoroutineScope(Job() + Dispatchers.IO)
        userUID = FirebaseGlobals.auth.currentUser!!.uid

        var tokenOfObserver = ""
        searchViewModel.searchRes.observe(this){
            if (it.isNotEmpty()){
                with(SearchRecView) {
                    layoutManager = LinearLayoutManager(requireContext())
                    itemAnimator = DefaultItemAnimator()
                    adapter = SearchRecyclerViewAdapter(it, FirebaseGlobals.friendsId,
                        FirebaseGlobals.pendingFriendsId) { friendUid, sendOrCancel ->
                            FirebaseGlobals.fireStoreCollection.document(friendUid).get()
                            .addOnSuccessListener{
                                val toToken = it["token"] as String
                                tokenOfObserver = toToken
                                sendOrCancelFriendRequest(friendUid, sendOrCancel)
                            }
                    }
                }
            }
        }

        searchViewModel.sendSuccessfully.observe(this){
            if (it){
                Toast.makeText(requireContext(), "Request was send", Toast.LENGTH_SHORT).show()
                coroScope.launch {
                    if (tokenOfObserver.isNotBlank()){
                        sendRequestNotification(FirebaseGlobals.userInfo!!,tokenOfObserver)
                    }
                }
            }
        }

        searchViewModel.unsendSuccessfully.observe(this){
            Toast.makeText(requireContext(), "Canceled Request", Toast.LENGTH_SHORT).show()
        }

        SearchNameButton.setOnClickListener {
            val nameForSearch = SearchNameEditText.text.toString()
            if (nameForSearch.isNotBlank()) {
               searchViewModel.searchBy(nameForSearch)
            }
        }
    }

    private fun sendOrCancelFriendRequest(uid: String, pendingRequest: Boolean) {
        searchViewModel.sendOrUnsend(uid , pendingRequest)
    }


    private suspend fun sendRequestNotification(fromUser: NewUser, toToken : String) {
        sendNotificationOb.sendNotification.pushNotification(
            NewUserPushNotification(fromUser.apply { topic="FRIEND_REQUEST" }, toToken))
    }

    override fun onDestroy() {
        super.onDestroy()
        coroScope.cancel()
    }
}

