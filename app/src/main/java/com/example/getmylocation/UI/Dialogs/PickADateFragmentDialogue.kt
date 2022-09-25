package com.example.getmylocation.UI.Dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.getmylocation.R
import kotlinx.android.synthetic.main.date_picker_dialog.*

class PickADateFragmentDialogue(val arrayOfResult : (kotlin.Array<Int>)->(Unit)): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.date_picker_dialog , container , false)
        if (dialog!=null && dialog!!.window!=null){
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CancelDOB.setOnClickListener {
            dismiss()
        }
        ApplyDOB.setOnClickListener {
            arrayOfResult(arrayOf(datePicker.dayOfMonth,datePicker.month+1,datePicker.year))
            dismiss()
        }
    }
}