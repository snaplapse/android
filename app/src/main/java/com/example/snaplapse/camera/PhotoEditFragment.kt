package com.example.snaplapse.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.snaplapse.R
import com.example.snaplapse.api.RetrofitHelper
import com.example.snaplapse.api.data.photo.PhotoRequest
import com.example.snaplapse.api.routes.PhotosApi
import com.example.snaplapse.databinding.FragmentPhotoEditBinding
import com.example.snaplapse.view_models.CameraViewModel
import com.example.snaplapse.view_models.ItemsViewModel2
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PhotoEditFragment : Fragment() {
    private lateinit var safeContext: Context

    private lateinit var _binding: FragmentPhotoEditBinding
    private val binding get() = _binding

    private val viewModel: CameraViewModel by activityViewModels()

    private lateinit var imageBitmap: Bitmap

    private var userID: Int = 0
    private val photosApi = RetrofitHelper.getInstance().create(PhotosApi::class.java)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
        userID = activity?.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)?.getInt("id", 0)!!
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

    private fun uploadPhoto() {
        val description = binding.textInput.text.toString()
        if (description.isBlank()) {
            Toast.makeText(safeContext, "Please provide a description.", Toast.LENGTH_SHORT).show()
        } else {
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val encodedBitmap: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
            uploadToServer(description, encodedBitmap)
        }
    }

    private fun deletePhoto() {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContainerView, CameraFragment())
        transaction.commit()
    }

    @SuppressLint("NewApi")
    private fun uploadToServer(description: String, encodedBitmap: String) {
        lifecycleScope.launchWhenCreated {
            try {
                val requestBody = PhotoRequest(user=userID, location=1, description=description, bitmap=encodedBitmap)
                val response = photosApi.upload(requestBody)
                if (response.isSuccessful) {
                    Toast.makeText(
                        safeContext,
                        "Uploaded photo: " + binding.textInput.text.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    val current = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    val text = current.toString() + "\n" + binding.textInput.text.toString()
                    viewModel.appendProfilePhotos(ItemsViewModel2(response.body()?.id ?: 0, userID, imageBitmap, text))
                    val imm = requireActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE
                    ) as InputMethodManager
                    imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.add(R.id.fragmentContainerView, CameraFragment())
                    transaction.commit()
                }
                else {
                    // TODO: Error handling
                }
            } catch (e: Exception) {
                Log.e("UploadPhotoError", e.toString())
            }
        }
    }
}
