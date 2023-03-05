package com.example.snaplapse.for_you

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snaplapse.BuildConfig
import com.example.snaplapse.MainActivity
import com.example.snaplapse.R
import com.example.snaplapse.api.RetrofitHelper
import com.example.snaplapse.api.routes.LocationsApi
import com.example.snaplapse.api.routes.PhotosApi
import com.example.snaplapse.view_models.ForYouViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient

class ForYouFragment : Fragment() {
    private lateinit var safeContext: Context
    private lateinit var placesClient: PlacesClient
    private var locationPermissionGranted = false

    private val locationsApi = RetrofitHelper.getInstance().create(LocationsApi::class.java)
    private val photosApi = RetrofitHelper.getInstance().create(PhotosApi::class.java)
    private var userID: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userID = activity?.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)?.getInt("id", 0)!!
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getLocationPermission()

        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_for_you, container, false)
        var currentCoordinates: String
        if (locationPermissionGranted) {
            Places.initialize(safeContext, BuildConfig.MAPS_API_KEY)
            placesClient = Places.createClient(safeContext)
            // Use fields to define the data types to return.
            val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.TYPES, Place.Field.ID)

            // Use the builder to create a FindCurrentPlaceRequest.
            val request = FindCurrentPlaceRequest.newInstance(placeFields)

            val placeResult = placesClient.findCurrentPlace(request)
            placeResult.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val likelyPlace = task.result.placeLikelihoods[0].place
                    currentCoordinates = likelyPlace.latLng.latitude.toString() + "," + likelyPlace.latLng.longitude.toString()

                    recommendations(view, currentCoordinates)
                }
            }
        } else {
            getLocationPermission()
        }

        return view
    }

    private fun recommendations(view: View, currentCoordinates: String) {
        lifecycleScope.launchWhenCreated {
            try {
                val recyclerView = view.findViewById<RecyclerView>(R.id.for_you_view)
                recyclerView.layoutManager = LinearLayoutManager(MainActivity())

//                val coordinates = "43.4723,-80.5449" // UWaterloo coordinates for testing
                val recResponse = locationsApi.getRecommendations(userID, currentCoordinates, 5000.0, 10)
                val recommendations = recResponse.body()?.results

                val cards = ArrayList<ForYouViewModel>()
                if (recommendations != null) {
                    for (location in recommendations) {
                        val photosResponse = photosApi.getPhotosByLocation(location.id, "-created")
                        val photos = photosResponse.body()?.results
                        if (photos?.isNotEmpty() == true) {
                            val mostRecentPhoto = photos[0]
                            val bmpRaw = mostRecentPhoto?.bitmap
                            val imageBytes = Base64.decode(bmpRaw, 0)
                            var image =
                                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                            val card = ForYouViewModel(image, location.name, location.id)
                            cards.add(card)
                        }
                    }
                }

                val adapter = ForYouAdapter(cards, parentFragmentManager)
                recyclerView.adapter = adapter

            } catch (e: Exception) {
                Log.e("ForYouPageError", e.toString())
            }
        }
    }

    @SuppressLint("MissingPermission")
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

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }
}
