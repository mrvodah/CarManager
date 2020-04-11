package com.example.carmanager.view.ui.history


import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.example.carmanager.R
import com.example.carmanager.`interface`.OnClickListener
import com.example.carmanager.databinding.FragmentHistoryBinding
import com.example.carmanager.model.Parking
import com.example.carmanager.model.Slot
import com.example.carmanager.util.PrefManager
import com.example.carmanager.util.fmNormalDay
import com.example.carmanager.view.adapter.SlotAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_history.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class HistoryFragment : Fragment() {

    private lateinit var time: String
    private val database: DatabaseReference by lazy {
        Firebase.database.reference
    }

    private lateinit var adapter: SlotAdapter

    private var parking: Parking? = null

    private var day = 0
    private var month = 0
    private var year = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHistoryBinding.inflate(inflater, container, false)

        parking = PrefManager.get<Parking>(PrefManager.PARKING)

        bindSlot()

        adapter = SlotAdapter(true, object : OnClickListener {
            override fun onClick(id: Int) {
                findNavController().navigate(HistoryFragmentDirections.actionHistoryFragmentToDetailFragment(time, id))
            }
        })
        binding.rvCategory.adapter = adapter

        return binding.root
    }

    private fun initTime() {
        val c = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH)
        day = c.get(Calendar.DAY_OF_MONTH)

        time = fmNormalDay(c.timeInMillis)
        tv_time.text = time
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTime()

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.nav_history, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_pick_date -> showPickDateDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showPickDateDialog() {
        val dpd = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            val c = Calendar.getInstance()
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            c.set(Calendar.MONTH, monthOfYear)
            c.set(Calendar.YEAR, year)
            time = fmNormalDay(c.timeInMillis)
            tv_time.text = time
        }, year, month, day)

        dpd.show()
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
        const val TAG = "HistoryFragment"
    }

}
