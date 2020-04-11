package com.example.carmanager.model

data class Time(
    var license_plate: String = "",
    var time_in: Long = 0L,
    var time_out: Long = 0L,
    var fee: Long = 0L
)