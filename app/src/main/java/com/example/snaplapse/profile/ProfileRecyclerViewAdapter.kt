package com.example.snaplapse.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.snaplapse.image_details.ImageDetailsFragment
import com.example.snaplapse.R
import com.example.snaplapse.view_models.ItemsViewModel
import com.example.snaplapse.view_models.ItemsViewModel2

class ProfileRecyclerViewAdapter(private val data: List<ItemsViewModel2>) : RecyclerView.Adapter<ProfileRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.profile_item, parent, false)
        view.layoutParams.height = parent.measuredWidth / 3
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = data[position]
        holder.itemView.setOnClickListener {
            val transaction = (holder.itemView.context as FragmentActivity).supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.fragmentContainerView,
                ImageDetailsFragment(ItemsViewModel(position, 1, 0, ""), listOf(), ItemsViewModel2(photo.id, photo.user, photo.image, photo.text))
            )
            transaction.addToBackStack(null)
            transaction.commit()
        }
        holder.imageView.setImageBitmap(photo.image)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}
