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
 * Use the [ResetPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResetPasswordFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(resources.getString(R.string.username_key))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_reset_password, container, false)
        val usernameView = view.findViewById<TextView>(R.id.reset_user)
        val password = view.findViewById<TextView>(R.id.new_password)
        val confirmPassword = view.findViewById<TextView>(R.id.reset_re_enter_password)
        val backButton = view.findViewById<ImageButton>(R.id.reset_back_button)
        val resetButton = view.findViewById<Button>(R.id.reset_button)
        val fragmentManager = parentFragmentManager
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

        usernameView.text = username

        backButton?.setOnClickListener{
            fragmentManager.popBackStack()
        }

        resetButton?.setOnClickListener{
            val usernameText = usernameView.text.toString()
            val passwordText = password?.text.toString()
            val confirmPasswordText = confirmPassword?.text.toString()
            if (passwordText == resources.getString(R.string.empty_string)) {
                password?.error = resources.getString(R.string.empty_password_error)
            }
            else if (passwordText != confirmPasswordText) {
                confirmPassword?.error = resources.getString(R.string.mismatch_passwords_error)
            }
            else {
                with(sharedPref?.edit()) {
                    this?.putString(usernameText, passwordText)
                    this?.apply()
                }

                Toast.makeText(requireContext(), resources.getString(R.string.password_changed_toast), 4).show()
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
         * @return A new instance of fragment ResetPasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResetPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}