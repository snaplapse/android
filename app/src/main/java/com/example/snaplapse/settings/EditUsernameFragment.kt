package com.example.snaplapse.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.snaplapse.MainActivity
import com.example.snaplapse.R
import java.text.SimpleDateFormat
import java.util.*

class EditUsernameFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_username, container, false)

        val username: TextView = view.findViewById(R.id.edit_username_input)
        val backButton: ImageButton = view.findViewById(R.id.edit_username_back_button)
        val confirmButton: Button = view.findViewById(R.id.edit_username_confirm_button)
        val sharedPref = activity?.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)

        val originalUsername = sharedPref?.getString("session", "")

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        confirmButton.setOnClickListener {
            val usernameText = username?.text.toString().trim()
            var hasErrors = false
            if (usernameText == "") {
                username?.error = resources.getString(R.string.empty_username_error)
                hasErrors = true
            }
            else if (usernameText.length > 16 || usernameText.length < 4) {
                username?.error = resources.getString(R.string.username_length_error)
                hasErrors = true
            }
            else if (sharedPref?.contains(usernameText) == true) {
                username?.error = resources.getString(R.string.username_exists_error)
                hasErrors = true
            }

            if (!hasErrors) {
                val password = sharedPref?.getString(sharedPref?.getString("session", ""), "")

                with(sharedPref?.edit()) {
                    this?.remove(originalUsername)
                    this?.putString(usernameText, password)
                    this?.putString("session", usernameText)
                    this?.apply()
                }

                parentFragmentManager.popBackStack()

            }
        }

        return view
    }
}