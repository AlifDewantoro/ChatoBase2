package com.gamatechno.chato.sdk.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateFormatUtilChat {
    fun stringToLong(date: String, dateFormat: String): Long? {
        var stdF: Long? = null
        val df = SimpleDateFormat(dateFormat, Locale.getDefault())
        Log.d("KENALOG", "date $date")
        try {
            stdF = df.parse(date)!!.time
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.d("KENALOG", "error nya " + e.message)
        }

        return stdF
    }

    fun longToString(milis: Long, dateFormat: String): String? {
        val date = Date()
        date.time = milis

        var dateS = ""

        try {
            val df = SimpleDateFormat(dateFormat, Locale.getDefault())
            dateS = df.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return dateS
    }

    fun formatDisplay(date: String, oldFormat: String, newFormat: String): String {
        var display = ""
        val d: Date
        var df = SimpleDateFormat(oldFormat, Locale.getDefault())
        Log.e("LOG", "date input = " +date)
        try {
            d = df.parse(date)!!
            df = SimpleDateFormat(newFormat, Locale.getDefault())
            display = df.format(d)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        Log.e("LOG", "date output = " +display)

        return display
    }

    fun setupDateFormUtc(date:String): String{
        var result = date.replace("T", " ")
        result = result.substring(0, result.length-6)
        result = formatDisplay(result, "yyyy-MM-dd HH:mm:ss", "dd MMMM yyyy HH:mm")
        return result
    }

    fun getDaysPass(dateData: String): Int{
        val cal = Calendar.getInstance()
        val nowMilis = cal.timeInMillis
        val dataMilis = stringToLong(dateData, "dd/MM/yyyy")

        val diff = nowMilis - dataMilis!!
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
    }

    fun getHourPass(hourData: String): Int{
        val cal = Calendar.getInstance()
        val nowMilis = cal.timeInMillis
        val justDateNow = longToString(nowMilis, "dd/MM/yyyy")!! + " " + hourData

        val dataMilis = stringToLong(justDateNow, "dd/MM/yyyy HH:mm")

        val diff = nowMilis - dataMilis!!
        return TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS).toInt()
    }

    fun getMinutePass(minuteData: String): Int{
        val cal = Calendar.getInstance()
        val nowMilis = cal.timeInMillis
        val justDateNow = longToString(nowMilis, "dd/MM/yyyy")!! + " " + minuteData

        val dataMilis = stringToLong(justDateNow, "dd/MM/yyyy HH:mm")

        val diff = nowMilis - dataMilis!!
        return TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS).toInt()
    }
}