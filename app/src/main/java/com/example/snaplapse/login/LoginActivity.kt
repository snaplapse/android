package com.example.snaplapse.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.snaplapse.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val fragmentManager = supportFragmentManager
        val loginFragment = LoginFragment()
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.login_layout, loginFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}