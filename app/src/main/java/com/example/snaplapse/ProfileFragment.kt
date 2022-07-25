package com.example.snaplapse

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ProfileFragment : Fragment() {
    private val viewModel: CameraViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        val recyclerview = view.findViewById<RecyclerView>(R.id.profile_recycler_view)
        recyclerview.layoutManager = GridLayoutManager(MainActivity(), 3)

        var data = listOf<ItemsViewModel2>()
        viewModel.profilePhotos.observe(viewLifecycleOwner) { profilePhotos ->
            data = profilePhotos
            val adapter = ProfileRecyclerViewAdapter(data)
            recyclerview.adapter = adapter
            val nPostsTextView = view.findViewById<TextView>(R.id.n_posts)
            nPostsTextView.text = (data.size + 6).toString()
        }

        val adapter = ProfileRecyclerViewAdapter(data)
        recyclerview.adapter = adapter

        val nPostsTextView = view.findViewById<TextView>(R.id.n_posts)
        nPostsTextView.text = (data.size + 6).toString()

        return view
    }
}