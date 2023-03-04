package com.example.snaplapse.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.snaplapse.BuildConfig
import com.example.snaplapse.R
import com.example.snaplapse.api.MapHelper
import com.example.snaplapse.api.RetrofitHelper
import com.example.snaplapse.api.data.photo.PhotoRequest
import com.example.snaplapse.api.routes.MapsApi
import com.example.snaplapse.api.routes.PhotosApi
import com.example.snaplapse.databinding.FragmentPhotoEditBinding
import com.example.snaplapse.map.MapFragment
import com.example.snaplapse.view_models.CameraViewModel
import com.example.snaplapse.view_models.ItemsViewModel2
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PhotoEditFragment : Fragment() {
    private lateinit var safeContext: Context

    private lateinit var _binding: FragmentPhotoEditBinding
    private val binding get() = _binding

    private val viewModel: CameraViewModel by activityViewModels()

    private var locationPermissionGranted = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lastKnownLocation: Location? = null
    private var coordString: String = ""

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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(safeContext)

        getLocationPermission()
        getDeviceLocation()

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
        var params: MutableMap<String, String> = mutableMapOf<String, String>()
        params["location"] = coordString
        params["radius"] = "1000"
        params["key"] = BuildConfig.MAPS_API_KEY

        lifecycleScope.launchWhenCreated {
            try {
                val response = MapHelper.getInstance().create(MapsApi::class.java).getNearbyPlaces(params)
                if (response.isSuccessful) {
                    Log.i("resp", response.body().toString())
                }
            } catch (e: Exception) {
                Log.e("NearbyPlacesError", e.toString())
            }
        }


        val description = binding.textInput.text.toString()
        if (description.isBlank()) {
            Toast.makeText(safeContext, "Please provide a description.", Toast.LENGTH_SHORT).show()
        } else {
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val encodedBitmap: String = Base64.encodeToString(byteArray, Base64.NO_WRAP)
            uploadToServer(description, encodedBitmap)
        }
    }

    private fun deletePhoto() {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContainerView, CameraFragment())
        transaction.commit()
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(safeContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        if (locationPermissionGranted) {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    lastKnownLocation = task.result
                    if (lastKnownLocation != null) {
                        val sb = StringBuilder()
                        sb.append(lastKnownLocation!!.latitude.toString()).append(",").append(lastKnownLocation!!.longitude.toString())
                        coordString = sb.toString()
                    }
                } else {
                }
            }
        }
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

    companion object {
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
    }
}
