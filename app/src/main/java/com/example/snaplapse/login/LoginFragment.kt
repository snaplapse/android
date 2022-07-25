package com.example.snaplapse.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.snaplapse.MainActivity
import com.example.snaplapse.R

class LoginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view:View = inflater.inflate(R.layout.fragment_login, container, false)
        val username = view.findViewById<TextView>(R.id.username)
        val password = view.findViewById<TextView>(R.id.password)
        val loginButton = view.findViewById<Button>(R.id.login_button)
        val registerButton = view.findViewById<Button>(R.id.register_button)
        val forgotPasswordButton = view.findViewById<TextView>(R.id.forgot_password_button)
        val fragmentManager = parentFragmentManager
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

        loginButton?.setOnClickListener{
            val usernameText = username?.text.toString()
            val passwordText = password?.text.toString()
            if (sharedPref?.contains(usernameText) == true && sharedPref.getString(usernameText, resources.getString(R.string.empty_string)) == passwordText) {
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }
            else {
                username?.text = resources.getString(R.string.empty_string)
                password?.text = resources.getString(R.string.empty_string)
                password?.error = resources.getString(R.string.incorrect_user_pass_error)
            }
        }

        registerButton?.setOnClickListener{
            username?.text = resources.getString(R.string.empty_string)
            password?.text = resources.getString(R.string.empty_string)
            password?.error = null

            val registerFragment = RegisterFragment()
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.login_layout, registerFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        forgotPasswordButton?.setOnClickListener{
            val usernameText = username?.text.toString()
            if (sharedPref?.contains(usernameText) == false) {
                Toast.makeText(requireContext(), resources.getString(R.string.user_not_found_error), 4).show()
            }
            else {
                username?.text = resources.getString(R.string.empty_string)
                password?.text = resources.getString(R.string.empty_string)
                password?.error = null

                var args = Bundle()
                args.putString(resources.getString(R.string.username_key), usernameText)

                val resetPasswordFragment = ResetPasswordFragment()
                resetPasswordFragment.arguments= args
                val transaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.login_layout, resetPasswordFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        return view
    }

    companion object {
    }
}
