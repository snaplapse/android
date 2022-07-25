package com.example.snaplapse

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

class ProfileRecyclerViewAdapter(private val data: List<ItemsViewModel2>) : RecyclerView.Adapter<ProfileRecyclerViewAdapter.ViewHolder>() {
    private val placeholders = listOf(
        R.drawable.statue_of_liberty,
        R.drawable.starbucks_waterloo,
        R.drawable.tims_waterloo,
        R.drawable.williams_waterloo,
        R.drawable.cn_tower,
        R.drawable.statue_of_liberty2,
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.profile_item, parent, false)
        view.layoutParams.height = parent.measuredWidth / 3
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < placeholders.size) {
            val photo = placeholders[position]
            holder.itemView.setOnClickListener {
                val transaction = (holder.itemView.context as FragmentActivity).supportFragmentManager.beginTransaction()
                transaction.replace(
                    R.id.fragmentContainerView,
                    ImageDetailsFragment(ItemsViewModel(photo, ""), listOf())
                )
                transaction.addToBackStack(null)
                transaction.commit()
            }
            holder.imageView.setImageResource(photo)
        } else {
            val photo = data[position - placeholders.size]
            holder.itemView.setOnClickListener {
                val transaction = (holder.itemView.context as FragmentActivity).supportFragmentManager.beginTransaction()
                transaction.replace(
                    R.id.fragmentContainerView,
                    ImageDetailsFragment(ItemsViewModel(0, ""), listOf(), ItemsViewModel2(photo.image, photo.text))
                )
                transaction.addToBackStack(null)
                transaction.commit()
            }
            holder.imageView.setImageBitmap(photo.image)
        }
    }

    override fun getItemCount(): Int {
        return data.size + placeholders.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}