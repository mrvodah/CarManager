package com.example.carmanager.view.ui.register


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment

import com.example.carmanager.R
import com.example.carmanager.databinding.FragmentRegisterBinding
import com.example.carmanager.util.hideSoftKeyboard
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment(), RegisterNavigator {

    override fun onErrorName(s: String) {
        input_name.error = s
        input_name.requestFocus()
    }

    override fun onErrorEmail(s: String) {
        input_email.error = s
        input_email.requestFocus()
    }

    override fun onErrorPassword(s: String) {
        input_password.error = s
        input_password.requestFocus()
    }

    override fun onSuccess(user: FirebaseUser?) {
        hideSoftKeyboard(activity!!)
//        NavHostFragment.findNavController(this).navigate(R.id.action_registerFragment_to_loginFragment)
    }

    override fun onFailure(s: String?) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

    override fun onLogin() {
        NavHostFragment.findNavController(this).navigate(R.id.action_registerFragment_to_loginFragment)
    }

    private val viewModel: RegisterViewModel by lazy {
        ViewModelProviders.of(this).get(RegisterViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        viewModel.setNavigator(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar!!.hide()
    }

}
