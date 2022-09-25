package com.example.getmylocation.UI.Dialogs

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.getmylocation.Data.Remote.FirebaseGlobals
import com.example.getmylocation.R
import com.example.getmylocation.Domain.Models.NewUser
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.pick_profile_pic.*

class PickProfilePictureDialog(val selectedImage : (String?)->(Unit)) : DialogFragment(){


    lateinit var firebaseStorage : FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.pick_profile_pic, container , false)
        if (dialog!=null && dialog!!.window!=null){
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseStorage = FirebaseStorage.getInstance()

        setProfilePic.setOnClickListener {
            setProfilePic.isEnabled = false
            startActivityForResult(Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" },123)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == Activity.RESULT_OK && data != null) {
            val pathURI = data.data

            isCancelable = false
            progressBarForPic.visibility = View.VISIBLE

            val picStorageName = System.currentTimeMillis().toString()+".${getMimeType(pathURI!!)}"
            val childRef = firebaseStorage.getReference(FirebaseGlobals.auth.currentUser!!.uid)
                .child(picStorageName)

            Toast.makeText(requireContext() , "Uploading .." , Toast.LENGTH_SHORT).show()

            childRef.putFile(pathURI).addOnSuccessListener { snapshot ->
                Toast.makeText(requireContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show()
                snapshot.storage.downloadUrl.addOnSuccessListener {DownloadUri->

                    val profilePicRelated = hashMapOf(
                        "Uri" to DownloadUri.toString()
                    )
                  FirebaseGlobals.fireStoreCollection.document(FirebaseGlobals.auth.currentUser!!.uid).also { DocRef->
                      DocRef.collection("Profile_Pictures").document(picStorageName).set(profilePicRelated)
                      DocRef.get().addOnSuccessListener {
                          val forMerge = it.toObject(NewUser::class.java)
                          forMerge!!.profilePicture = DownloadUri.toString()
                          DocRef.set(forMerge , SetOptions.merge())

                          selectedImage(DownloadUri.toString())
                      }
                  }
                    dismiss()
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext() , "Failed while uploading" , Toast.LENGTH_SHORT).show()
                dismiss()
            }.addOnProgressListener {
                progressBarForPic.progress = ((100*it.bytesTransferred/it.totalByteCount).toInt())
            }
        }
    }


    private fun getMimeType(uri : Uri?) : String? = MimeTypeMap.getSingleton().getExtensionFromMimeType(requireContext().contentResolver.getType(uri!!))
}