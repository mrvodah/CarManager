package com.example.carmanager.view.ui.detail


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.carmanager.R
import com.example.carmanager.databinding.FragmentDetailBinding
import com.example.carmanager.model.Parking
import com.example.carmanager.model.Time
import com.example.carmanager.util.PrefManager
import com.example.carmanager.view.adapter.TimeAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    private val database: DatabaseReference by lazy {
        Firebase.database.reference.child("Time")
    }

    private lateinit var adapter: TimeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailBinding.inflate(inflater, container, false)

        adapter = TimeAdapter()
        binding.rvTime.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val time = DetailFragmentArgs.fromBundle(arguments!!).time
        val slot = DetailFragmentArgs.fromBundle(arguments!!).slot

        val parking = PrefManager.get<Parking>(PrefManager.PARKING)

        database.child(time).child(parking!!.id.toString()).child(slot.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists()) {
                        if(p0.hasChildren()) {
                            val times = arrayListOf<Time>()
                            p0.children.forEach {
                                val time = it.getValue(Time::class.java)
                                time?.let {
                                    times.add(it)
                                }
                            }
                            adapter.submitList(times)
                        } else {
                            tv_empty.visibility = View.VISIBLE
                            Log.d(TAG, "not has child")
                        }
                    } else {
                        tv_empty.visibility = View.VISIBLE
                        Log.d(TAG, "not exists")
                    }
                }
            })
    }

    companion object {
        const val TAG = "DetailFragment"
    }

}
