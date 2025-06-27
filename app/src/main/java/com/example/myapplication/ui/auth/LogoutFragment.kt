package com.example.myapplication.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.R

class LogoutFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_logout, container, false)
        val logoutButton = view.findViewById<Button>(R.id.logout)

        logoutButton.setOnClickListener {
            val sharedPref = requireActivity().getSharedPreferences("MyPrefs", AppCompatActivity.MODE_PRIVATE)
            sharedPref.edit().clear().apply()

            Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }
}
