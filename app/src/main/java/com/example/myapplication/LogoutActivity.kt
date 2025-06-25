//package com.example.myapplication
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//
//class LogoutActivity : AppCompatActivity() {
//    private lateinit var logout: Button
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_logout)
//
//        logout = findViewById(R.id.logout)
//        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
//
//        logout.setOnClickListener {
//            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
//            sharedPref.edit().clear().apply()
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//        }
//    }
//}