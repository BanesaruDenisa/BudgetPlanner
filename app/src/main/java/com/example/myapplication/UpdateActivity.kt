package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateActivity : AppCompatActivity() {

    private lateinit var updateImage: ImageView
    private lateinit var updateButton: Button
    private lateinit var updateDesc: EditText
    private lateinit var updateTitle: EditText
    private lateinit var updateBudg: EditText
    private lateinit var updateDate: TextView
    private lateinit var buttonDate: Button

    private lateinit var dataDao: DataClassDao
    private var itemId: Int = -1
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        updateButton = findViewById(R.id.updateButton)
        updateDesc = findViewById(R.id.updateDesc)
        updateImage = findViewById(R.id.updateImage)
        updateBudg = findViewById(R.id.updateBudg)
        updateDate = findViewById(R.id.updateDate)
        updateTitle = findViewById(R.id.updateTitle)
        buttonDate = findViewById(R.id.buttonDate)

        dataDao = AppDatabase.getDatabase(this).dataClassDao()

        itemId = intent.getIntExtra("id", -1)
        val item = dataDao.getById(itemId)
        if (item == null) {
            Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        updateTitle.setText(item.title)
        updateDesc.setText(item.dataDesc)
        updateBudg.setText(item.dataBudg)
        updateDate.setText(item.dataDate)

        val originalImageUri = item.dataImage
        uri = Uri.parse(originalImageUri)

        val file = File(originalImageUri)
        if (file.exists()) {
            updateImage.setImageURI(Uri.fromFile(file))
        } else {
            Glide.with(this)
                .load(originalImageUri)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(updateImage)
        }

        val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { newUri ->
            newUri?.let {
                uri = it
                updateImage.setImageURI(it)
            }
        }

        updateImage.setOnClickListener {
            getImage.launch("image/*")
        }

        val calendarBox = Calendar.getInstance()
        val dateBox = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendarBox.set(Calendar.YEAR, year)
            calendarBox.set(Calendar.MONTH, month)
            calendarBox.set(Calendar.DAY_OF_MONTH, day)
            updateDateText(calendarBox)
        }

        buttonDate.setOnClickListener {
            DatePickerDialog(
                this, dateBox,
                calendarBox.get(Calendar.YEAR),
                calendarBox.get(Calendar.MONTH),
                calendarBox.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        updateButton.setOnClickListener {
            val title = updateTitle.text.toString()
            val desc = updateDesc.text.toString()
            val budg = updateBudg.text.toString()
            val date = updateDate.text.toString()
            val imageUri = uri?.toString() ?: originalImageUri

            val updatedItem = DataClass(
                id = itemId,
                title = title,
                dataDesc = desc,
                dataBudg = budg,
                dataImage = imageUri,
                dataDate = date
            )

            dataDao.update(updatedItem)
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun updateDateText(calendar: Calendar) {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.UK)
        updateDate.text = sdf.format(calendar.time)
    }
}
