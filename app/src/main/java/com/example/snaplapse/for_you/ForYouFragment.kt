package com.example.snaplapse.for_you

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snaplapse.MainActivity
import com.example.snaplapse.R
import com.example.snaplapse.api.RetrofitHelper
import com.example.snaplapse.api.routes.LocationsApi
import com.example.snaplapse.api.routes.PhotosApi
import com.example.snaplapse.view_models.ForYouViewModel

class ForYouFragment : Fragment() {
    private val locationsApi = RetrofitHelper.getInstance().create(LocationsApi::class.java)
    private val photosApi = RetrofitHelper.getInstance().create(PhotosApi::class.java)
    private var userID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userID = activity?.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)?.getInt("id", 0)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_for_you, container, false)
        recommendations(view)
        return view
    }

    private fun recommendations(view: View) {
        lifecycleScope.launchWhenCreated {
            try {
                val recyclerView = view.findViewById<RecyclerView>(R.id.for_you_view)
                recyclerView.layoutManager = LinearLayoutManager(MainActivity())

                val coordinates = "43.4723,-80.5449" // replace with real location
                val recResponse = locationsApi.getRecommendations(userID, coordinates, 5000.0, 10)
                val recommendations = recResponse.body()?.results

                val cards = ArrayList<ForYouViewModel>()
                if (recommendations != null) {
                    for (location in recommendations) {
                        val photosResponse = photosApi.getPhotosByLocation(location.id, "-created")
                        val mostRecentPhoto = photosResponse.body()?.results?.get(0)
                        val bmpRaw = mostRecentPhoto?.bitmap
                        val imageBytes = Base64.decode(bmpRaw, 0)
                        var image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                        val card = ForYouViewModel(image, location.name)
                        cards.add(card)
                    }
                }

                val adapter = ForYouAdapter(cards, parentFragmentManager)
                recyclerView.adapter = adapter

            } catch (e: Exception) {
                Log.e("ForYouPageError", e.toString())
            }
        }
    }
}
