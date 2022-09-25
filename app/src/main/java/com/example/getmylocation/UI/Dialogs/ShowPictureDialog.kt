package com.example.getmylocation.UI.Dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.getmylocation.Data.Remote.FirebaseGlobals
import com.example.getmylocation.R
import kotlinx.android.synthetic.main.show_picture_dialog.*


class ShowPictureDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.show_picture_dialog , container , false)

        if (dialog!=null && dialog!!.window!=null){
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseGlobals.userInfo.let {
            Glide.with(requireContext()).load(Uri.parse(it!!.profilePicture)).into(ShowBottomSheetImageView)
        }

    }
}