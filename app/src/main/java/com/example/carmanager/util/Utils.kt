package com.example.carmanager.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*

fun hideSoftKeyboard(activity: Activity) {
    val view = activity.currentFocus
    if (view != null) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        try {
            view.clearFocus()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}

fun fmTimeStamp(time: Long): String {
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    val date = Date(time)
    return simpleDateFormat.format(date)
}

fun fmToDay(): String {
    val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
    val date = Date()
    return simpleDateFormat.format(date)
}

fun fmNormalDay(time: Long): String {
    val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
    val date = Date(time)
    return simpleDateFormat.format(date)
}