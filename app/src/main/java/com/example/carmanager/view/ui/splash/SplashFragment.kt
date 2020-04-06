package com.example.carmanager.view.ui.splash


import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController

import com.example.carmanager.R
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 */
class SplashFragment : Fragment() {

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar!!.hide()

        Handler().postDelayed({
            val user = auth.currentUser
            user?.let {
                Log.d("TAG", it.email)
            }
            if(user != null) {
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }, 2000)
    }

}
