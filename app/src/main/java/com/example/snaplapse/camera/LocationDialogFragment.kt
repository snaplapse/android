package com.example.snaplapse.camera

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.snaplapse.R
import com.example.snaplapse.view_models.CurrentPlaceViewModel

class LocationDialogFragment: DialogFragment() {
    var currentItem: Int = 0
    var items = ArrayList<CurrentPlaceViewModel>()

    fun setItem(index: Int) {
        currentItem = index
    }

    fun setList(list: List<CurrentPlaceViewModel>) {
        for (i in list) {
            items.add(i)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var arrayItems = arrayOf(items[0].name)
        for (i in 1 until items.size) {
            arrayItems += items[i].name
        }

        return activity?.let {
            val builder = AlertDialog.Builder(context)
            val originalItem = currentItem
            builder.setTitle("Select Location")
                .setSingleChoiceItems(arrayItems, currentItem, DialogInterface.OnClickListener { dialog, which ->
                    currentItem = which
                })
                .setPositiveButton("Confirm", DialogInterface.OnClickListener { dialog, id ->
                    setFragmentResult("locationDialog", bundleOf("locationKey" to currentItem))
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                    currentItem = originalItem
                })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}