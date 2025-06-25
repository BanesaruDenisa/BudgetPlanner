//package com.example.myapplication
//
//import android.annotation.SuppressLint
//import android.content.Intent
//import android.os.Bundle
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.bumptech.glide.Glide
//import com.github.clans.fab.FloatingActionButton
//import java.io.File
//
//class DetailActivity : AppCompatActivity() {
//    private lateinit var detailDesc: TextView
//    private lateinit var detailTitle: TextView
//    private lateinit var detailBudg: TextView
//    private lateinit var detailDate: TextView
//    private lateinit var detailImage: ImageView
//    private lateinit var deleteButton: FloatingActionButton
//    private lateinit var editButton: FloatingActionButton
//
//    private lateinit var dataDao: DataClassDao
//    private var itemId: Int = -1
//
//    @SuppressLint("MissingInflatedId")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_detail)
//
//        detailDesc = findViewById(R.id.detailDesc)
//        detailImage = findViewById(R.id.detailImage)
//        detailTitle = findViewById(R.id.detailTitle)
//        detailDate = findViewById(R.id.detailDate)
//        deleteButton = findViewById(R.id.deleteButton)
//        editButton = findViewById(R.id.editButton)
//        detailBudg = findViewById(R.id.detailBudg)
//
//        dataDao = AppDatabase.getDatabase(this).dataClassDao()
//
//        itemId = intent.getIntExtra("id", -1)
//        if (itemId != -1) {
//            val item = dataDao.getById(itemId)
//            if (item != null) {
//                detailTitle.text = item.title
//                detailDesc.text = item.dataDesc
//                detailBudg.text = item.dataBudg
//                detailDate.text = item.dataDate
//
//                val imageFile = File(item.dataImage)
//                if (imageFile.exists()) {
//                    Glide.with(this)
//                        .load(imageFile)
//                        .placeholder(R.drawable.placeholder_image)
//                        .error(R.drawable.placeholder_image)
//                        .into(detailImage)
//                } else {
//                    detailImage.setImageResource(R.drawable.placeholder_image)
//                }
//
//                deleteButton.setOnClickListener {
//                    dataDao.delete(item)
//                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
//                }
//
//                editButton.setOnClickListener {
//                    val intent = Intent(this@DetailActivity, UpdateFragment::class.java)
//                        .putExtra("id", item.id)
//                    startActivity(intent)
//                }
//            }
//        }
//    }
//}
