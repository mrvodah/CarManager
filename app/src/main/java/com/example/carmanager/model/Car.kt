package com.example.carmanager.model

data class Car(
    var time_in: Long = 0L,
    var time_out: Long = 0L,
    var parking: Int = 0,
    var slot: Int = 0,
    var time_id: String = ""
)