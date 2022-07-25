package com.example.snaplapse.login

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

class RegisterFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_register, container, false)

        val username = view.findViewById<TextView>(R.id.register_username)
        val password = view.findViewById<TextView>(R.id.register_password)
        val confirmPassword = view.findViewById<TextView>(R.id.re_enter_password)
        val backButton = view.findViewById<ImageButton>(R.id.register_back_button)
        val signupButton = view.findViewById<Button>(R.id.sign_up_button)
        val fragmentManager = parentFragmentManager
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

        backButton?.setOnClickListener{
            fragmentManager.popBackStack()
        }

        signupButton?.setOnClickListener{
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
                    this?.putString(usernameText, passwordText)
                    this?.apply()
                }

                Toast.makeText(requireContext(), resources.getString(R.string.account_created_toast), Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }
        }

        return view
    }
}