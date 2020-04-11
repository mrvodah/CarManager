package com.example.carmanager.util

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:status")
fun TextView.bindStatus(value: Boolean) {
    text = if(value) {
        "Đấy"
    } else {
        "Trống"
    }
}

@BindingAdapter("app:formatTime")
fun TextView.bindFormatTime(value: Long) {
    text = fmTimeStamp(value)
}