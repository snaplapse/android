package com.example.snaplapse.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snaplapse.MainActivity
import com.example.snaplapse.R
import com.example.snaplapse.view_models.ItemsViewModel

class TimelineFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_timeline, container, false)

        // getting the recyclerview by its id
        val recyclerview = view.findViewById<RecyclerView>(R.id.timeline_recycler_view)
        val backButton = view.findViewById<ImageButton>(R.id.timeline_back_button)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(MainActivity())

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemsViewModel>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (i in 0..19) {
            var image: Int = if (i<10) {
                R.drawable.statue_of_liberty
            } else {
                R.drawable.statue_of_liberty2
            }
            var card = ItemsViewModel(i, 1, image, (2022 - i).toString())
            data.add(card)
        }

        // This will pass the ArrayList to our Adapter
        val adapter = CustomAdapter(data, parentFragmentManager)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
