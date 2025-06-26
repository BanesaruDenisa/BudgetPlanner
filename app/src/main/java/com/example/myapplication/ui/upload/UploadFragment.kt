package com.example.myapplication.ui.upload

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.dao.DataClassDao
import com.example.myapplication.data.model.DataClass
import com.example.myapplication.databinding.FragmentUploadBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class UploadFragment : Fragment() {

    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!

    private var uri: Uri? = null
    private lateinit var dataDao: DataClassDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataDao = AppDatabase.getDatabase(requireContext()).dataClassDao()

        // Dropdown tip tranzacție
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.transaction_types,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.uploadDesc.adapter = adapter

        // Imagine
        val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { selectedUri ->
            selectedUri?.let {
                uri = it
                binding.uploadImage.setImageURI(it)
            }
        }

        binding.uploadImage.setOnClickListener {
            getImage.launch("image/*")
        }

        // Dată
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, y, m, d ->
            calendar.set(y, m, d)
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.UK)
            binding.uploadDate.text = sdf.format(calendar.time)
        }

        binding.buttonDate.setOnClickListener {
            DatePickerDialog(
                requireContext(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        binding.saveButton.setOnClickListener {
            val title = binding.uploadTitle.text.toString().trim()
            val desc = binding.uploadDesc.selectedItem.toString()
            val budgStr = binding.uploadBudg.text.toString().trim()
            val date = binding.uploadDate.text.toString().trim()
            val imageUri = uri?.let { saveImageLocally(it) } ?: ""


            if (title.isEmpty() || budgStr.isEmpty() || date.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields\"", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val budg = budgStr.toDoubleOrNull()
            if (budg == null ||  budg <= 0) {
                Toast.makeText(requireContext(), "Amount must be greater than 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val item = DataClass(
                title = title,
                dataDesc = desc,
                dataBudg = budgStr,
                dataImage = imageUri,
                dataDate = date
            )

            dataDao.insert(item)
            Toast.makeText(requireContext(), "Transaction saved", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun saveImageLocally(uri: Uri): String {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val fileName = "IMG_${System.currentTimeMillis()}.jpg"
        val file = File(requireContext().filesDir, fileName)

        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return file.absolutePath
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
