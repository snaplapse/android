package com.example.snaplapse.image_details

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.snaplapse.R
import com.example.snaplapse.api.RetrofitHelper
import com.example.snaplapse.api.data.photo.PhotoActionRequest
import com.example.snaplapse.api.routes.PhotosApi
import com.example.snaplapse.view_models.ItemsViewModel
import com.example.snaplapse.view_models.ItemsViewModel2

class ImageDetailsFragment(var item: ItemsViewModel, private val mList: List<ItemsViewModel>, val item2: ItemsViewModel2? = null) : Fragment() {

    private var likeButton: ImageButton? = null
    private var flagButton: ImageButton? = null

    private val photosApi = RetrofitHelper.getInstance().create(PhotosApi::class.java)

    private var userID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_image_details, container, false)
        val text = view.findViewById<TextView>(R.id.textView)
        val img: ImageView = view.findViewById(R.id.imageView) as ImageView

        userID = activity?.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)?.getInt("id", 0)!!

        if (item2 == null) {
            text.text = resources.getString(R.string.image_details).format(item.text)
            img.setImageResource(item.image)
            img.setOnTouchListener(OnSwipeTouchListener(activity, item, mList, view))
        } else {
            text.text = resources.getString(R.string.image_details).format(item2.text)
            img.setImageBitmap(item2.image)
        }

        likeButton = view.findViewById(R.id.like_button)
        likeButton!!.setOnClickListener {
            likeImage(userID)
        }

        flagButton = view.findViewById(R.id.flag_button)
        flagButton!!.setOnClickListener {
            flagImage(userID)
        }

        val backButton = view.findViewById<ImageButton>(R.id.image_details_back_button)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        return view
    }

    private fun likeImage(id: Int) {
        lifecycleScope.launchWhenCreated {
            try {
                val requestBody = PhotoActionRequest(user=id, photo=item2?.id?:0)
                val response = photosApi.like(requestBody)
                if (response.isSuccessful) {
                    likeButton?.setImageResource(R.drawable.ic_baseline_thumb_up_24_blue)
                    Toast.makeText(activity, resources.getString(R.string.like_toast), Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(activity, "Error Liking", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("LikePhotoError", e.toString())
            }
        }
    }

    private fun flagImage(id: Int) {
        lifecycleScope.launchWhenCreated {
            try {
                val requestBody = PhotoActionRequest(user=id, photo=item2?.id?:0)
                val response = photosApi.flag(requestBody)
                if (response.isSuccessful) {
                    Toast.makeText(activity, resources.getString(R.string.flag_toast), Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(activity, "Error Flagging", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("FlagPhotoError", e.toString())
            }
        }
//        FlagDialogFragment().show(childFragmentManager, "")
    }
}