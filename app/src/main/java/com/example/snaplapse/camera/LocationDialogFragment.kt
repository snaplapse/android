package com.example.snaplapse.camera

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.snaplapse.R

class LocationDialogFragment: DialogFragment() {
    var currentLocation: String = ""

    fun setLocation(location: String) {
        currentLocation = location
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Location: $currentLocation")
//                .setSingleChoiceItems(0, 0, DialogInterface.OnClickListener { dialog, which ->
//
//                })
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->

                })
                .setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, id ->

                })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}