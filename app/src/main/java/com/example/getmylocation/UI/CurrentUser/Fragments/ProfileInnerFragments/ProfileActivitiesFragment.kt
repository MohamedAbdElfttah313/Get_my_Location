package com.example.getmylocation.UI.CurrentUser.Fragments.ProfileInnerFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.getmylocation.UI.CurrentUser.CurrentUserRecViewsAdapter.ActivitiesAndHealthRecyclerViewAdapter
import com.example.getmylocation.Data.Remote.FirebaseGlobals
import com.example.getmylocation.R
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_activities_profile.*


class ProfileActivitiesFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activities_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val activities = ArrayList<String>()
        FirebaseGlobals.fireStoreCollection.document(FirebaseGlobals.auth.currentUser!!.uid).collection("Activities")
            .orderBy("Timestamp" , Query.Direction.DESCENDING)
            .get().addOnSuccessListener {
            for (activitySnapShot in it){
                activities.add(
                    "${activitySnapShot["Time"]}_${activitySnapShot["Content"]}"
                )
            }
                //Error may be here
                with(ActivitiesRecView){
                    layoutManager = LinearLayoutManager(requireContext())
                    itemAnimator = DefaultItemAnimator()
                    adapter = ActivitiesAndHealthRecyclerViewAdapter(activities)
                }
        }
    }

}