package com.example.snaplapse.for_you

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snaplapse.R
import com.example.snaplapse.timeline.TimelineFragment
import com.example.snaplapse.view_models.ForYouViewModel

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
        val imageView = ImageView(holder.linearLayout.context)
        imageView.setImageBitmap(forYouViewModel.thumbnail)
        imageView.setOnClickListener{
            val timelineFragment = TimelineFragment(forYouViewModel.locationId)
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

        holder.textView.text = forYouViewModel.name
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
