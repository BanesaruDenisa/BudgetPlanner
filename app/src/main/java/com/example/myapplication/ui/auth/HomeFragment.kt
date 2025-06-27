package com.example.myapplication.ui.auth

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.utils.BrodcastRec
import com.example.myapplication.adapter.MyAdapter
import com.example.myapplication.R
import com.example.myapplication.data.dao.DataClassDao
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.model.DataClass
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private lateinit var searchView: SearchView
    private lateinit var broadcastReceiver: BroadcastReceiver
    private lateinit var dataDao: DataClassDao
    private var dataList = mutableListOf<DataClass>()
    private lateinit var incomeTextView: TextView
    private lateinit var expenseTextView: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        fab = view.findViewById(R.id.fab)
        searchView = view.findViewById(R.id.search)
        searchView.clearFocus()
        incomeTextView = view.findViewById(R.id.incomeTotal)
        expenseTextView = view.findViewById(R.id.expenseTotal)



        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        dataDao = AppDatabase.getDatabase(requireContext()).dataClassDao()
        dataList = dataDao.getAll().toMutableList()
        adapter = MyAdapter(requireContext(), dataList) { dataItem ->
            val bundle = Bundle().apply {
                putInt("id", dataItem.id)
            }
            findNavController().navigate(R.id.detailsFragment, bundle)


        }

        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchList(it) }
                return true
            }
        })

//        fab.setOnClickListener {
//            val intent = Intent(requireContext(), UploadActivity::class.java)
//            startActivity(intent)
//        }

        fab.setOnClickListener {
            findNavController().navigate(R.id.uploadFragment)
        }



        broadcastReceiver = BrodcastRec()
        registerNetworkBroadcast()

        return view
    }

    override fun onResume() {
        super.onResume()
        dataList.clear()
        dataList.addAll(dataDao.getAll())
        adapter.notifyDataSetChanged()
        updateTotals()

    }

    private fun searchList(text: String) {
        val searchList = dataList.filter {
            it.title.lowercase().contains(text.lowercase())
        }
        adapter.searchDataList(ArrayList(searchList))
    }

    private fun registerNetworkBroadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            requireContext().registerReceiver(
                broadcastReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

    private fun unregisterNetwork() {
        try {
            requireContext().unregisterReceiver(broadcastReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    private fun updateTotals() {
        val incomeTotal = dataList
            .filter { it.dataDesc.trim().equals("Income", ignoreCase = true) }
            .sumOf { it.dataBudg.trim().toIntOrNull() ?: 0 }

        val expenseTotal = dataList
            .filter { it.dataDesc.trim().equals("Expense", ignoreCase = true) }
            .sumOf { it.dataBudg.trim().toIntOrNull() ?: 0 }

        incomeTextView.text = "Income: $incomeTotal"
        expenseTextView.text = "Expense: $expenseTotal"
    }



    override fun onDestroyView() {
        super.onDestroyView()
        unregisterNetwork()
    }
}