package com.example.carmanager.view.ui.category


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.carmanager.`interface`.OnClickListener

import com.example.carmanager.databinding.FragmentCategoryBinding
import com.example.carmanager.model.Parking
import com.example.carmanager.model.Slot
import com.example.carmanager.util.PrefManager
import com.example.carmanager.view.adapter.SlotAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass.
 */
class CategoryFragment : Fragment() {

    private val database: DatabaseReference by lazy {
        Firebase.database.reference
    }

    private lateinit var adapter: SlotAdapter

    private var parking: Parking? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentCategoryBinding.inflate(inflater, container, false)

        parking = PrefManager.get<Parking>(PrefManager.PARKING)

        bindSlot()

        adapter = SlotAdapter(false, object : OnClickListener {
            override fun onClick(id: Int) {

            }
        })
        binding.rvCategory.adapter = adapter

        return binding.root
    }

    private fun bindSlot() {
        database.child("Slot").child(parking!!.id.toString()).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d(TAG, "${p0.key} - ${p0.value}")
                val slots = arrayListOf<Slot>()
                p0.children.forEach {
                    val slot = it.getValue(Slot::class.java)
                    slot?.let {
                        slots.add(it)
                    }
                }
                adapter.submitList(slots)
            }
        })
    }

    companion object {
        const val TAG = "CategoryFragment"
    }

}
