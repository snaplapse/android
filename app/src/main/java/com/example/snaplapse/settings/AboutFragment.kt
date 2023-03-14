package com.example.snaplapse.settings

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.lifecycleScope
import com.example.snaplapse.R
import com.example.snaplapse.api.RetrofitHelper
import com.example.snaplapse.api.data.photo.PhotoPatchRequest
import com.example.snaplapse.api.data.photo.PhotoVisibilityRequest
import com.example.snaplapse.api.routes.PhotosApi
import com.example.snaplapse.timeline.CustomAdapter
import com.example.snaplapse.view_models.ItemsViewModel2
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AboutFragment : Fragment() {
    private val photosApi = RetrofitHelper.getInstance().create(PhotosApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_about, container, false)

        val backButton: ImageButton = view.findViewById(R.id.about_back_button)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        lifecycleScope.launchWhenCreated {
            try {
                var page = 1

                while (true) {
                    val response = photosApi.getPhotos(page)
                    if (response.isSuccessful) {
                        for (photo in response.body()!!.results) {
                            if (photo.bitmap.isNotEmpty()) {
                                val decodedBitmap = Base64.decode(photo.bitmap, Base64.DEFAULT)
                                val bitmap =
                                    BitmapFactory.decodeByteArray(decodedBitmap, 0, decodedBitmap.size)
                                val byteArrayOutputStream = ByteArrayOutputStream()
                                bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 0, byteArrayOutputStream)
                                val byteArray = byteArrayOutputStream.toByteArray()
                                val encodedBitmap: String = Base64.encodeToString(byteArray, Base64.NO_WRAP)
                                photosApi.patchPhoto(photo.id, PhotoPatchRequest(encodedBitmap))
                            }
                        }
                    } else {
                        break
                    }
                    page += 1
                }
            } catch (e: Exception) {
                Log.e("GetPhotoError", e.toString())
            }
        }

        return view
    }
}
