//package com.example.myapplication
//
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.view.MenuItem
//import android.widget.MediaController
//import android.widget.VideoView
//import androidx.appcompat.app.AppCompatActivity
//import com.google.android.material.bottomnavigation.BottomNavigationView
//
//class AboutUsActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_about_us)
//
//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
//        bottomNavigationView.selectedItemId = R.id.bottom_aboutus
//        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
//            when (item.itemId) {
//                R.id.bottom_home -> {
//                    startActivity(Intent(applicationContext, MainActivity::class.java))
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//                    finish()
//                    true
//                }
//                R.id.bottom_aboutus -> true
//                R.id.bottom_logout -> {
//                    startActivity(Intent(applicationContext, LogoutActivity::class.java))
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//                    finish()
//                    true
//                }
//                else -> false
//            }
//        }
//
//        val videoView = findViewById<VideoView>(R.id.video_view)
//        val videoPath = "android.resource://" + packageName + "/" + R.raw.video
//        val uri = Uri.parse(videoPath)
//        videoView.setVideoURI(uri)
//
//        val mediaController = MediaController(this)
//        videoView.setMediaController(mediaController)
//        mediaController.setAnchorView(videoView)
//    }
//}
