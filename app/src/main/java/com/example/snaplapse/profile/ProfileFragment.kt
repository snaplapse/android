package com.example.snaplapse.profile

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snaplapse.MainActivity
import com.example.snaplapse.R
import com.example.snaplapse.settings.SettingsFragment
import com.example.snaplapse.view_models.CameraViewModel
import com.example.snaplapse.view_models.ItemsViewModel2


class ProfileFragment : Fragment() {
    private val viewModel: CameraViewModel by activityViewModels()
    private var usernameText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            usernameText = activity?.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)?.getString("session", "")
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

        var data = listOf<ItemsViewModel2>()
        viewModel.profilePhotos.observe(viewLifecycleOwner) { profilePhotos ->
            data = profilePhotos
            val adapter = ProfileRecyclerViewAdapter(data)
            recyclerview.adapter = adapter
            val nPostsTextView = view.findViewById<TextView>(R.id.n_posts)
            nPostsTextView.text = (data.size + 6).toString()
        }

        val username = view.findViewById<TextView>(R.id.profile_username)
        username.text = usernameText
        val adapter = ProfileRecyclerViewAdapter(data)
        recyclerview.adapter = adapter

        val nPostsTextView = view.findViewById<TextView>(R.id.n_posts)
        nPostsTextView.text = (data.size + 6).toString()

        val settingsButton: ImageButton = view.findViewById(R.id.settings_button)
        settingsButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, SettingsFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }
}