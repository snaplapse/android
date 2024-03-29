package com.example.snaplapse.image_details

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.TextView
import com.example.snaplapse.R
import com.example.snaplapse.view_models.ItemsViewModel
import kotlin.math.abs


class OnSwipeTouchListener(var ctx: Context?, var item: ItemsViewModel, private val mList: List<ItemsViewModel>, private var view: View) : OnTouchListener {
    private val gestureDetector: GestureDetector
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                        result = true
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
        }

    }

    fun onSwipeRight() {
        var nextItem = mList[mList.indexOf(item) + 1]

        val img: ImageView = view.findViewById(R.id.imageView) as ImageView
        img.setImageResource(nextItem.image)

        var text = view.findViewById<TextView>(R.id.textView)
        text.text = view.resources.getString(R.string.image_details).format(nextItem.text)
        item = nextItem
    }
    fun onSwipeLeft() {
        var previousItem = mList[mList.indexOf(item) - 1]

        val img: ImageView = view.findViewById(R.id.imageView) as ImageView
        img.setImageResource(previousItem.image)

        var text = view.findViewById<TextView>(R.id.textView)
        text.text = view.resources.getString(R.string.image_details).format(previousItem.text)
        item = previousItem

    }
    fun onSwipeTop() {}
    fun onSwipeBottom() {}

    init {
        gestureDetector = GestureDetector(ctx, GestureListener())
    }
}
