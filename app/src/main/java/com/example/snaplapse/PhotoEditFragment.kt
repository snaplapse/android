package com.example.snaplapse

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.snaplapse.databinding.FragmentPhotoEditBinding

class PhotoEditFragment : Fragment() {
    private lateinit var safeContext: Context

    private lateinit var _binding: FragmentPhotoEditBinding
    private val binding get() = _binding

    private val viewModel: CameraViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(R.id.fragmentContainerView, CameraFragment())
                transaction.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        _binding = FragmentPhotoEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.imageBitmap.observe(viewLifecycleOwner) { imageBitmap ->
            binding.imageView.setImageBitmap(imageBitmap)
        }
        binding.uploadButton.setOnClickListener { uploadPhoto() }
    }

    private fun uploadPhoto() {
        val description = binding.textInput.text.toString()
        if (description.isBlank()) {
            Toast.makeText(safeContext, "Please provide a description.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                safeContext,
                "Uploaded photo: " + binding.textInput.text.toString(),
                Toast.LENGTH_SHORT
            ).show()
            viewModel.setPhotoDescription(binding.textInput.text.toString())
            val imm = requireActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentContainerView, CameraFragment())
            transaction.commit()
        }
    }
}