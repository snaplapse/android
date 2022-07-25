package com.example.snaplapse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageDetailsFragment(var item: ItemsViewModel, private val mList: List<ItemsViewModel>) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image_details, container, false)
        var text = view.findViewById<TextView>(R.id.textView)
        text.text = resources.getString(R.string.image_details).format(item.text)
        val img: ImageView = view.findViewById(R.id.imageView) as ImageView
        img.setImageResource(item.image)
        img.setOnTouchListener(OnSwipeTouchListener(activity, item, mList, view))

        // Inflate the layout for this fragment
        return view
    }

}