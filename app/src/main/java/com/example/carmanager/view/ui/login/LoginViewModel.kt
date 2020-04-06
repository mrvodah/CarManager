package com.example.carmanager.view.ui.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel: ViewModel() {

    private var listener: LoginNavigator? = null

    var email: String? = null
    var password: String? = null

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun setNavigator(navigator: LoginNavigator) {
        this.listener = navigator
    }

    fun onLoginClick() {

        if(email.isNullOrEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            listener?.onErrorEmail("Enter a valid email address")
            return
        }

        if(password.isNullOrEmpty() || password?.length!! < 6) {
            listener?.onErrorPassword("Password must at least 6 characters")
            return
        }

        login()

    }

    private fun login() {
        auth.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    val user = auth.currentUser
                    listener?.onSuccess(user)
                } else {
                    listener?.onFailure(task.exception?.message)
                }
            }
    }

    fun onRegisterClick() {
        listener?.onRegister()
    }

}