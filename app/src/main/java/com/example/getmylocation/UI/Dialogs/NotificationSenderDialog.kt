package com.example.getmylocation.UI.Dialogs

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.getmylocation.R
import kotlinx.android.synthetic.main.notifications_sender_dialog.*

class NotificationSenderDialog (val toWhom : (Int)->(Unit)) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.notifications_sender_dialog, container, false)

        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }

        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = false

       senderRelativeLayout.setOnTouchListener { view, motionEvent ->
           countDownTimer.cancel()
           isCancelable = true
           true
       }
        countDownTimer.start()

        val toCandidates = arrayOf("To Everyone", "To Friends")

        SendToPicker.apply {
            minValue = 0
            maxValue = toCandidates.size - 1
            displayedValues = toCandidates


        }

        SendNotificationToSelected.setOnClickListener {
            countDownTimer.cancel()
            toWhom(SendToPicker.value)
            dismiss()
        }


    }

    private val countDownTimer = object : CountDownTimer( 10*1000 , 1000) {
        override fun onTick(p0: Long) {
            CountDownTimerTextView.text = ((p0/1000)+1).toString()
        }

        override fun onFinish() {
            toWhom(SendToPicker.value)
            dismiss()
        }
    }

}