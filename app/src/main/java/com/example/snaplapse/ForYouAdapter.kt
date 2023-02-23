package com.example.snaplapse

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView

class ForYouAdapter(private val mList: List<ForYouViewModel>, private val fragmentManager: FragmentManager) : RecyclerView.Adapter<ForYouAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.for_you_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val forYouViewModel = mList[position]
        for (i in 0 until forYouViewModel.thumbnail.size) {
            val imageView = ImageView(holder.linearLayout.context)
            imageView.setImageResource(forYouViewModel.thumbnail[i])
            imageView.setOnClickListener{
                val timelineFragment = TimelineFragment()
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentContainerView, timelineFragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
            imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
            imageView.adjustViewBounds = true
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            ).apply {
                weight = 1.0f
                gravity = Gravity.LEFT
            }
            params.height = 1000
            imageView.layoutParams = params

            holder.linearLayout.addView(imageView)
        }

        // sets the text to the textview from our itemHolder class
        holder.textView.text = forYouViewModel.tag
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val linearLayout: LinearLayout = itemView.findViewById(R.id.timelines_row)
        val textView: TextView = itemView.findViewById(R.id.tag_label)
    }
}
