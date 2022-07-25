package com.example.snaplapse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ProfileFragment : Fragment() {
    private var usernameText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            usernameText = it.getString(resources.getString(R.string.username_key))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        val recyclerview = view.findViewById<RecyclerView>(R.id.profile_recycler_view)
        recyclerview.layoutManager = GridLayoutManager(MainActivity(), 3)

        val username = view.findViewById<TextView>(R.id.profile_username)
        username.text = usernameText

        val data = arrayOf(
            R.drawable.statue_of_liberty,
            R.drawable.starbucks_waterloo,
            R.drawable.tims_waterloo,
            R.drawable.williams_waterloo,
            R.drawable.cn_tower,
            R.drawable.statue_of_liberty2,
        )
        val adapter = ProfileRecyclerViewAdapter(data)
        recyclerview.adapter = adapter

        val nPostsTextView = view.findViewById<TextView>(R.id.n_posts)
        nPostsTextView.text = data.size.toString()

        return view
    }
}