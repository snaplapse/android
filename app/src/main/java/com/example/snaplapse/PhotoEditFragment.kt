package com.example.snaplapse

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PhotoEditFragment : Fragment() {
    private lateinit var safeContext: Context

    private lateinit var _binding: FragmentPhotoEditBinding
    private val binding get() = _binding

    private val viewModel: CameraViewModel by activityViewModels()

    private lateinit var imageBitmap: Bitmap

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
            this.imageBitmap = imageBitmap
            binding.imageView.setImageBitmap(imageBitmap)
        }
        binding.uploadButton.setOnClickListener { uploadPhoto() }
        binding.deleteButton.setOnClickListener { deletePhoto() }
    }

    @SuppressLint("NewApi")
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
            val current = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val text = current.toString() + "\n" + binding.textInput.text.toString()
            viewModel.appendProfilePhotos(ItemsViewModel2(imageBitmap, text))
            val imm = requireActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentContainerView, CameraFragment())
            transaction.commit()
        }
    }

    private fun deletePhoto() {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContainerView, CameraFragment())
        transaction.commit()
    }
}
