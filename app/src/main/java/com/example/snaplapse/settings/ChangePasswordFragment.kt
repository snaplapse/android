package com.example.snaplapse.settings

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.snaplapse.R

class ChangePasswordFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_change_password, container, false)
        val password = view.findViewById<TextView>(R.id.change_password_input)
        val confirmPassword = view.findViewById<TextView>(R.id.change_password_input_confirm)
        val backButton = view.findViewById<ImageButton>(R.id.change_password_back_button)
        val confirmButton = view.findViewById<Button>(R.id.change_password_confirm)
        val sharedPref = activity?.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)

        val username = sharedPref?.getString("session", "")

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        confirmButton.setOnClickListener {
            var hasErrors = false

            val passwordText = password?.text.toString()
            val confirmPasswordText = confirmPassword?.text.toString()
            if (passwordText == resources.getString(R.string.empty_string)) {
                password?.error = resources.getString(R.string.empty_password_error)
                hasErrors = true
            }
            else if (passwordText != confirmPasswordText) {
                confirmPassword?.error = resources.getString(R.string.mismatch_passwords_error)
                hasErrors = true
            }

            if (!hasErrors) {
                with(sharedPref?.edit()) {
                    this?.putString(username, passwordText)
                    this?.apply()
                }
                parentFragmentManager.popBackStack()
            }
        }
        return view
    }
}