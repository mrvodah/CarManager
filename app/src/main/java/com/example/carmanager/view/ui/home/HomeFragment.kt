package com.example.carmanager.view.ui.home


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController

import kotlinx.android.synthetic.main.fragment_home.*
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.example.carmanager.R
import com.google.firebase.auth.FirebaseAuth


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(activity!!).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar!!.show()

        initClick()

        initUser()

        setHasOptionsMenu(true)

    }

    private fun initUser() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            viewModel.email?.value = it.email
        }
    }

    private fun initClick() {
        fab_add.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_scanFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.nav_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_select -> showSelectDialog()
            R.id.nav_log_out -> showLogoutDialog()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure want to logout?")
        builder.setPositiveButton("OK") { dialog, which ->
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showSelectDialog() {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Select your CarManager")
        val customLayout =
            layoutInflater.inflate(R.layout.dialog_select_option, null)
        val spinner = customLayout.findViewById<Spinner>(R.id.spinner)
        builder.setView(customLayout)
        builder.setPositiveButton("OK") { dialog, which ->
            Toast.makeText(context, spinner.selectedItemPosition, Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

}
