package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private lateinit var searchView: SearchView
    private lateinit var broadcastReceiver: BroadcastReceiver
    private lateinit var dataDao: DataClassDao
    private var dataList = mutableListOf<DataClass>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        fab = view.findViewById(R.id.fab)
        searchView = view.findViewById(R.id.search)
        searchView.clearFocus()

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        dataDao = AppDatabase.getDatabase(requireContext()).dataClassDao()
        dataList = dataDao.getAll().toMutableList()
        adapter = MyAdapter(requireContext(), dataList) { dataItem ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("id", dataItem.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchList(it) }
                return true
            }
        })

        fab.setOnClickListener {
            val intent = Intent(requireContext(), UploadActivity::class.java)
            startActivity(intent)
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

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterNetwork()
    }
}