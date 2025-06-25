package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.FragmentDetailsBinding
import java.io.File

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var dataDao: DataClassDao
    private var itemId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataDao = AppDatabase.getDatabase(requireContext()).dataClassDao()

        itemId = arguments?.getInt("id") ?: -1
        if (itemId == -1) {
            Toast.makeText(requireContext(), "ID invalid", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
            return
        }

        val item = dataDao.getById(itemId)
        if (item == null) {
            Toast.makeText(requireContext(), "Item not found", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
            return
        }

        try {
            binding.detailTitle.text = item.title
            binding.detailDesc.text = item.dataDesc
            binding.detailBudg.text = item.dataBudg
            binding.detailDate.text = item.dataDate

            val imageFile = File(item.dataImage)
            if (imageFile.exists()) {
                Glide.with(this)
                    .load(imageFile)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(binding.detailImage)
            } else {
                binding.detailImage.setImageResource(R.drawable.placeholder_image)
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Eroare afișare date: ${e.message}", Toast.LENGTH_LONG).show()
        }

        binding.deleteButton.setOnClickListener {
            dataDao.delete(item)
            Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.editButton.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("id", item.id)
            }
            findNavController().navigate(R.id.updateFragment, bundle)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
