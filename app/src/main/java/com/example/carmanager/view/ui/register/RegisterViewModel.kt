package com.example.carmanager.view.ui.register

import androidx.lifecycle.ViewModel
import com.example.carmanager.view.ui.login.LoginNavigator
import com.google.firebase.auth.FirebaseAuth

class RegisterViewModel: ViewModel() {

    private var listener: RegisterNavigator? = null

    var name: String? = null
    var email: String? = null
    var password: String? = null

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun setNavigator(navigator: RegisterNavigator) {
        this.listener = navigator
    }

    fun onRegisterClick() {
        if(name.isNullOrEmpty()) {
            listener?.onErrorName("Name mustn't empty")
            return
        }

        if(email.isNullOrEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            listener?.onErrorEmail("Enter a valid email address")
            return
        }

        if(password.isNullOrEmpty() || password?.length!! < 6) {
            listener?.onErrorPassword("Password must at least 6 characters")
            return
        }

        register()
    }

    private fun register() {
        auth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    val user = auth.currentUser
                    listener?.onSuccess(user)
                } else {
                    listener?.onFailure(task.exception?.message)
                }
            }
    }

    fun onLoginClick() {
        listener?.onLogin()
    }
}