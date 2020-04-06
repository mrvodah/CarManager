package com.example.carmanager.view.ui.login

import com.google.firebase.auth.FirebaseUser

interface LoginNavigator {
    fun onErrorEmail(s: String)
    fun onErrorPassword(s: String)
    fun onSuccess(user: FirebaseUser?)
    fun onFailure(s: String?)
    fun onRegister()

}