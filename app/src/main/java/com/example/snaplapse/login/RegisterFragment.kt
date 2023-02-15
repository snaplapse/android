package com.example.snaplapse.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.snaplapse.MainActivity
import com.example.snaplapse.R
import java.text.SimpleDateFormat
import java.util.*

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
        val sharedPref = activity?.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)

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
                val c = Calendar.getInstance().time
                val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
                val formattedDate = df.format(c)

                with(sharedPref?.edit()) {
                    this?.putString(usernameText, passwordText)
                    this?.putString("session", usernameText)
                    this?.apply()
                }

                Toast.makeText(requireContext(), resources.getString(R.string.account_created_toast), Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, MainActivity::class.java)
                intent.putExtra("username", usernameText)
                startActivity(intent)
            }
        }

        return view
    }
}