package com.example.snaplapse.for_you

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snaplapse.MainActivity
import com.example.snaplapse.R
import com.example.snaplapse.view_models.ForYouViewModel

class ForYouFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_for_you, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.for_you_view)

        recyclerView.layoutManager = LinearLayoutManager(MainActivity())

        val data = ArrayList<ForYouViewModel>()
        val recommendedSpots = ArrayList<Int>()
        recommendedSpots.add(R.drawable.starbucks_waterloo)
        recommendedSpots.add(R.drawable.tims_waterloo)
        recommendedSpots.add(R.drawable.williams_waterloo)
        val card = ForYouViewModel(recommendedSpots, "Coffee Shop")
        data.add(card)

        val recommendedSpots2 = ArrayList<Int>()
        recommendedSpots2.add(R.drawable.statue_of_liberty)
        recommendedSpots2.add(R.drawable.cn_tower)
        val card2 = ForYouViewModel(recommendedSpots2, "Tourist Attraction")
        data.add(card2)

        val adapter = ForYouAdapter(data, parentFragmentManager)
        recyclerView.adapter = adapter

        return view
    }
}