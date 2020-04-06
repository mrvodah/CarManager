package com.example.carmanager.view.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {

    var email: MutableLiveData<String>? = null

    init {
        email = MutableLiveData()
    }

}