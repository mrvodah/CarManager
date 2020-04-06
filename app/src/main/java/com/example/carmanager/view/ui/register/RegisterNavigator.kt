package com.example.carmanager.view.ui.register

import com.google.firebase.auth.FirebaseUser

interface RegisterNavigator {
    fun onErrorName(s: String)
    fun onErrorEmail(s: String)
    fun onErrorPassword(s: String)
    fun onLogin()
    fun onSuccess(user: FirebaseUser?)
    fun onFailure(message: String?)
}