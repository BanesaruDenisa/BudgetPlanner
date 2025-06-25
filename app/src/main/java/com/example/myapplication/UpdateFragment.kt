package com.example.myapplication

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
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.FragmentUpdateBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private lateinit var dataDao: DataClassDao
    private var itemId: Int = -1
    private var uri: Uri? = null
    private lateinit var originalImageUri: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataDao = AppDatabase.getDatabase(requireContext()).dataClassDao()

//        itemId = requireActivity().intent.getIntExtra("id", -1)
//        val item = dataDao.getById(itemId)
//        if (item == null) {
//            Toast.makeText(requireContext(), "Item not found", Toast.LENGTH_SHORT).show()
//            requireActivity().onBackPressedDispatcher.onBackPressed()
//            return
//        }

        val itemId = arguments?.getInt("id") ?: -1
        if (itemId == -1) {
            Toast.makeText(requireContext(), "ID invalid", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }
        val dataDao = AppDatabase.getDatabase(requireContext()).dataClassDao()
        val item = dataDao.getById(itemId)
        if (item == null) {
            Toast.makeText(requireContext(), "Item not found", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }



        binding.updateTitle.setText(item.title)
        binding.updateBudg.setText(item.dataBudg)
        binding.updateDate.text = item.dataDate
        originalImageUri = item.dataImage
        uri = Uri.parse(originalImageUri)

        // Spinner
        val descOptions = listOf("Expense", "Income")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, descOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.updateDesc.adapter = adapter
        val selectedIndex = descOptions.indexOf(item.dataDesc)
        if (selectedIndex >= 0) binding.updateDesc.setSelection(selectedIndex)

        // Imagine
        val file = File(originalImageUri)
        if (file.exists()) {
            binding.updateImage.setImageURI(Uri.fromFile(file))
        } else {
            Glide.with(this)
                .load(originalImageUri)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(binding.updateImage)
        }

        val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { newUri ->
            newUri?.let {
                uri = it
                binding.updateImage.setImageURI(it)
            }
        }

        binding.updateImage.setOnClickListener {
            getImage.launch("image/*")
        }

        // Date Picker
        val calendar = Calendar.getInstance()
        val dateBox = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(year, month, day)
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.UK)
            binding.updateDate.text = sdf.format(calendar.time)
        }

        binding.buttonDate.setOnClickListener {
            DatePickerDialog(
                requireContext(), dateBox,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        binding.updateButton.setOnClickListener {
            val title = binding.updateTitle.text.toString().trim()
            val desc = binding.updateDesc.selectedItem.toString()
            val budgStr = binding.updateBudg.text.toString().trim()
            val date = binding.updateDate.text.toString()
            val imageUri = uri?.toString() ?: originalImageUri

            if (title.isEmpty() || budgStr.isEmpty() || date.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val budg = budgStr.toDoubleOrNull()
            if (budg == null || budg <= 0) {
                Toast.makeText(requireContext(), "Amount must be greater than 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedItem = DataClass(
                id = itemId,
                title = title,
                dataDesc = desc,
                dataBudg = budgStr,
                dataImage = imageUri,
                dataDate = date
            )

            dataDao.update(updatedItem)
            Toast.makeText(requireContext(), "Successfully updated!", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
