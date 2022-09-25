package com.example.getmylocation.UtilAndSystemServices

import java.text.SimpleDateFormat
import java.util.*

class TimeFormatter{

    fun getTimeFormatted(format : String, locale : Locale = Locale.getDefault()):String{
        return with(SimpleDateFormat(format , locale)){
            format(Calendar.getInstance().time)
        }
    }
}