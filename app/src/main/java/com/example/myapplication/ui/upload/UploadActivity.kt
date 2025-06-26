package com.example.myapplication.ui.upload

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.R
import com.example.myapplication.data.dao.DataClassDao
import com.example.myapplication.data.model.DataClass
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class UploadActivity : AppCompatActivity() {

    private lateinit var uploadImage: ImageView
    private lateinit var saveButton: Button
    private lateinit var uploadTitle: EditText
    private lateinit var uploadDesc: EditText
    private lateinit var uploadBudg: EditText
    private lateinit var uploadDate: TextView
    private lateinit var buttonDate: Button

    private var uri: Uri? = null

    private lateinit var database: AppDatabase
    private lateinit var dataDao: DataClassDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        uploadImage = findViewById(R.id.uploadImage)
        uploadDesc = findViewById(R.id.uploadDesc)
        uploadTitle = findViewById(R.id.uploadTitle)
        uploadBudg = findViewById(R.id.uploadBudg)
        uploadDate = findViewById(R.id.uploadDate)
        saveButton = findViewById(R.id.saveButton)
        buttonDate = findViewById(R.id.buttonDate)

        database = AppDatabase.getDatabase(this)
        dataDao = database.dataClassDao()

        val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { selectedUri ->
            selectedUri?.let {
                uri = it
                uploadImage.setImageURI(it)
            }
        }

        uploadImage.setOnClickListener {
            getImage.launch("image/*")
        }

        val calendarBox = Calendar.getInstance()
        val dateBox = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendarBox.set(Calendar.YEAR, year)
            calendarBox.set(Calendar.MONTH, month)
            calendarBox.set(Calendar.DAY_OF_MONTH, day)
            updateDate(calendarBox)
        }

        buttonDate.setOnClickListener {
            DatePickerDialog(
                this, dateBox,
                calendarBox.get(Calendar.YEAR),
                calendarBox.get(Calendar.MONTH),
                calendarBox.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        saveButton.setOnClickListener {
            val title = uploadTitle.text.toString()
            val desc = uploadDesc.text.toString()
            val budg = uploadBudg.text.toString()
            val date = uploadDate.text.toString()
            val imageUri = uri?.let { saveImageLocally(it) } ?: ""

            if (title.isEmpty() || desc.isEmpty() || budg.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Completează toate câmpurile", Toast.LENGTH_SHORT).show()
            } else {
                val item = DataClass(title = title, dataDesc = desc, dataBudg = budg, dataImage = imageUri, dataDate = date)
                dataDao.insert(item)
                Toast.makeText(this, "Salvat cu succes", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun updateDate(calendar: Calendar) {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.UK)
        uploadDate.text = sdf.format(calendar.time)
    }

    private fun saveImageLocally(uri: Uri): String {
        val inputStream = contentResolver.openInputStream(uri)
        val fileName = "IMG_${System.currentTimeMillis()}.jpg"
        val file = File(filesDir, fileName)

        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return file.absolutePath
    }
}
