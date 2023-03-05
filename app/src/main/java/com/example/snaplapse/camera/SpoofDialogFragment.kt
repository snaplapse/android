package com.example.snaplapse.camera

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.snaplapse.R

class SpoofDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(context)
            val inflater = requireActivity().layoutInflater
            val sharedPref = activity?.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            val view = inflater.inflate(R.layout.fragment_spoof_dialog, null)

            val latitudeText = view.findViewById<EditText>(R.id.setLatitude)
            val longitudeText = view.findViewById<EditText>(R.id.setLongitude)

            builder.setView(view)
                .setTitle("Location Spoofing")

                // Add action buttons
                .setPositiveButton("Confirm",
                    DialogInterface.OnClickListener { dialog, id ->
                        with(sharedPref?.edit()) {
                            this?.putFloat("latitude", latitudeText.text.toString().toFloat())
                            this?.putFloat("longitude", longitudeText.text.toString().toFloat())
                            this?.apply()
                        }
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        with(sharedPref?.edit()) {
                            this?.remove("latitude")
                            this?.remove("longitude")
                            this?.apply()
                        }
                    })

            builder.create()
        }  ?: throw IllegalStateException("Activity cannot be null")
    }
}