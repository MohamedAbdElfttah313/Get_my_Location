package com.example.getmylocation.Data.Repositories

import androidx.lifecycle.MutableLiveData
import com.example.getmylocation.Data.Remote.FirebaseGlobals
import com.example.getmylocation.Domain.Repositories.FriendsRepository
import com.example.getmylocation.UtilAndSystemServices.TimeFormatter
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject


class FriendsRepositoryImp @Inject constructor() : FriendsRepository {

    override fun getCurrentUserInfo(): Task<DocumentSnapshot> = FirebaseGlobals.fireStoreCollection.document(FirebaseGlobals.auth.currentUser!!.uid).get()

    override fun getFriends(): List<Task<DocumentSnapshot>> {
        val listOfTasks = ArrayList<Task<DocumentSnapshot>>()
        for (i in FirebaseGlobals.friendsId){
            listOfTasks.add(
                FirebaseGlobals.fireStoreCollection.document(i).get()
            )
        }
        return listOfTasks
    }

    override fun getFriendRequests(): List<Task<DocumentSnapshot>>{
        val listOfTasks = ArrayList<Task<DocumentSnapshot>>()
        for (i in FirebaseGlobals.friendRequests){
            listOfTasks.add(
                FirebaseGlobals.fireStoreCollection.document(i).get()
            )
        }
        return listOfTasks
    }

    override fun sendOrUnsend(uid : String , send: Boolean): Task<Void>{
        val userUID = FirebaseGlobals.userInfo!!.uid!!

        val requestReceiver = FirebaseGlobals.fireStoreCollection.document(uid).collection(
            FirebaseGlobals.Constants.PENDED_REQUESTS.const)
            .document(userUID)

        val updated = FirebaseGlobals.fireStoreCollection.document(userUID)
            .collection(FirebaseGlobals.Constants.PENDING_FRIENDS.const)
            .document(uid)
            .run {
                if (send) {
                    set(hashMapOf("Ref" to uid)).addOnSuccessListener {
                        requestReceiver.set(hashMapOf("Ref" to userUID))
                    }
                } else {
                    delete().addOnSuccessListener {
                        requestReceiver.delete()
                    }
                }
            }

        return updated
    }

    override fun getSearchResultBy(field: String): Task<QuerySnapshot> {
        return  FirebaseGlobals.fireStoreCollection.orderBy("nameForSearch").startAt(field).get()
    }

    override fun updateFriendAccepted(uid : String): MutableLiveData<Boolean> {
        val authUID = FirebaseGlobals.auth.currentUser!!.uid
        val resultMessage = MutableLiveData(false)
        FirebaseGlobals.fireStoreCollection.document(uid).run {
            collection(FirebaseGlobals.Constants.FRIENDS.const).document(authUID).set(hashMapOf("Ref" to authUID))
                .addOnSuccessListener {
                    collection(FirebaseGlobals.Constants.PENDING_FRIENDS.const).document(authUID).delete()
                    collection("Activities").document().set(
                        hashMapOf(
                            "Timestamp" to Timestamp.now(),
                            "Content" to "${FirebaseGlobals.userInfo!!.run { firstName+" "+lastName }} has accepted your friend request ",
                            "Time" to "${TimeFormatter().getTimeFormatted("dd/MM/yy hh:mm:ss aa")} "
                        )
                    )
                    resultMessage.value = true
                }
        }
        return resultMessage

    }

    override fun updateSelfAccFriend(uid: String): MutableLiveData<Boolean> {
        val userUID = FirebaseGlobals.auth.currentUser!!.uid
        val resultMessage = MutableLiveData(false)
        FirebaseGlobals.fireStoreCollection.document(userUID).run {
            collection(FirebaseGlobals.Constants.FRIENDS.const).document(uid).set(hashMapOf("Ref" to uid))
                .addOnSuccessListener{
                    collection(FirebaseGlobals.Constants.PENDED_REQUESTS.const).document(uid).delete().addOnSuccessListener {
                        resultMessage.value = true
                    }
                }
        }
        return resultMessage
    }

    override fun updateFriendDeclined(uid: String): MutableLiveData<Boolean> {
        val userUID = FirebaseGlobals.auth.currentUser!!.uid
        val resultMessage = MutableLiveData(false)

        FirebaseGlobals.fireStoreCollection.document(uid).run {
            collection(FirebaseGlobals.Constants.PENDING_FRIENDS.const).document(userUID).delete()
                .addOnSuccessListener {
                    resultMessage.value = true
                }
        }
        return resultMessage
    }

    override fun updateSelfDecFriend(uid: String): MutableLiveData<Boolean> {
        val userUID = FirebaseGlobals.auth.currentUser!!.uid
        val resultMessage = MutableLiveData(false)

        FirebaseGlobals.fireStoreCollection.document(userUID).run {
            collection(FirebaseGlobals.Constants.PENDED_REQUESTS.const).document(uid).delete()
                .addOnSuccessListener {
                    resultMessage.value = true
                }
        }

        return resultMessage
    }
}
