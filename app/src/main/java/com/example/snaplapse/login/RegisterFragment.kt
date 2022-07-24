package com.example.snaplapse.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.snaplapse.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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

                Toast.makeText(requireContext(), resources.getString(R.string.account_created_toast), 4).show()
                fragmentManager.popBackStack()
            }
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}