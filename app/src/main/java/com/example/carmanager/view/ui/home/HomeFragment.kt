package com.example.carmanager.view.ui.home


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController

import kotlinx.android.synthetic.main.fragment_home.*
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.example.carmanager.R
import com.example.carmanager.model.Parking
import com.example.carmanager.model.Slot
import com.example.carmanager.model.User
import com.example.carmanager.util.PrefManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(activity!!).get(HomeViewModel::class.java)
    }

    private val database: DatabaseReference by lazy {
        Firebase.database.reference
    }

    private val parkings = ArrayList<Parking>()
    private lateinit var currentParking: Parking

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

        initFirebase()

        setHasOptionsMenu(true)

        test()

        val parking = PrefManager.get<Parking>(PrefManager.PARKING)
        parking?.let {
            bindParking(it)
        }

    }

    private fun bindParking(it: Parking) {
        tv_name.visibility = View.VISIBLE
        tv_slot.visibility = View.VISIBLE
        tv_slot_value.visibility = View.VISIBLE

        tv_name.text = it.name
        tv_slot_value.text = "${it.remain}/${it.capacity}"
    }

    private fun test() {
        for(i in 1..30) {
            val slot = Slot(i, false)
            database.child("Slot").child("2").child(i.toString()).setValue(slot)
        }

        for(i in 1..50) {
            val slot = Slot(i, false)
            database.child("Slot").child("3").child(i.toString()).setValue(slot)
        }
    }

    val parkingChildListener = object : ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {
        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            val parking = p0.getValue(Parking::class.java)
            Log.d(TAG, parking.toString())
            parking?.id = p0.key?.toInt()!!
            parkings.add(parking!!)
        }

        override fun onChildRemoved(p0: DataSnapshot) {
        }
    }

    private fun initFirebase() {
        parkings.clear()
        database.child("Parking").addChildEventListener(parkingChildListener)
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
        builder.setTitle("Select Parking")
        val customLayout =
            layoutInflater.inflate(R.layout.dialog_select_option, null)
        val spinner = customLayout.findViewById<Spinner>(R.id.spinner)

        val items = ArrayList<String>()
        parkings.forEach {
            items.add(it.name)
        }
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, items)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                currentParking = parkings.get(position)
                Log.d(TAG, "$position")
            }

        }
        builder.setView(customLayout)
        builder.setPositiveButton("OK") { dialog, which ->
            PrefManager.put(PrefManager.PARKING, currentParking)
            bindParking(currentParking)
            dialog.dismiss()
            loadParking()
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun loadParking() {

    }

    companion object {
        const val TAG = "HomeFragment"
    }

}
