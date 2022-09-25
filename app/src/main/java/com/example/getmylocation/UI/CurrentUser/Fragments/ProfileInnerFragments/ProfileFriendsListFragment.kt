package com.example.getmylocation.UI.CurrentUser.Fragments.ProfileInnerFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.getmylocation.UI.CurrentUser.CurrentUserRecViewsAdapter.FriendsRecyclerViewAdapter
import com.example.getmylocation.Data.Remote.FirebaseGlobals
import com.example.getmylocation.R
import com.example.getmylocation.Domain.Models.NewUser
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot

import kotlinx.android.synthetic.main.fragment_friends_list_profile.*


class ProfileFriendsListFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends_list_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Trying With Tasks
        * */
        val listOfTasks = ArrayList<Task<DocumentSnapshot>>()
        val friends = ArrayList<NewUser>()
        for (uid in FirebaseGlobals.friendsId){
            listOfTasks.add(FirebaseGlobals.fireStoreCollection.document(uid).get().addOnSuccessListener {
                friends.add(it.toObject(NewUser::class.java)!!)
            })
        }

        val tasks = Tasks.whenAllSuccess<Unit>(listOfTasks).addOnSuccessListener {
            with(FriendsListRecView){
                //and here
                layoutManager = LinearLayoutManager(requireContext())
                itemAnimator = DefaultItemAnimator()
                adapter = FriendsRecyclerViewAdapter(friends)
            }
        }



    }


}