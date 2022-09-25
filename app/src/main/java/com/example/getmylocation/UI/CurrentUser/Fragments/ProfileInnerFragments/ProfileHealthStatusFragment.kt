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
import com.example.getmylocation.UtilAndSystemServices.TimeFormatter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_health_status_profile.*


class ProfileHealthStatusFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_health_status_profile, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val healthStatusContents = ArrayList<String>()
        FirebaseGlobals.fireStoreCollection.document(FirebaseGlobals.auth.currentUser!!.uid).collection("HealthStatus")
            .orderBy("Timestamp",Query.Direction.DESCENDING)
            .get().addOnSuccessListener {
                for (healthState in it){
                    healthStatusContents.add(
                        "${healthState["Time"]}_${healthState["Content"]}"
                    )
                }
                //and here
                with(HealthStatusRecView){
                    layoutManager = LinearLayoutManager(requireContext())
                    itemAnimator = DefaultItemAnimator()
                    adapter = ActivitiesAndHealthRecyclerViewAdapter(healthStatusContents)
                }
            }

        AddHealthState.setOnClickListener{
            with(ContentHealthStatusTextView.text.toString().trim()){
                if (isNotEmpty()){
                    AddHealthState.isEnabled=false
                    FirebaseGlobals.fireStoreCollection.document(FirebaseGlobals.auth.currentUser!!.uid).collection("HealthStatus")
                        .document().set(
                            hashMapOf(
                                "Timestamp" to Timestamp.now(),
                                "Content" to this,
                                "Time" to "${TimeFormatter().getTimeFormatted("dd/MM/yy hh:mm:ss aa")} "
                            )
                        ).addOnSuccessListener {
                            healthStatusContents.add("${TimeFormatter().getTimeFormatted("dd/MM/yy hh:mm:ss aa")}_$this")
                            HealthStatusRecView.adapter!!.notifyDataSetChanged()
                            ContentHealthStatusTextView.setText("")
                        }.addOnCompleteListener {
                            AddHealthState.isEnabled=true
                        }
                }
            }

        }

    }
}