package com.example.snaplapse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username = findViewById<TextView>(R.id.username)
        val password = findViewById<TextView>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener{
            if (username.text.toString() == "user" && password.text.toString() == "pass") {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else {
                username.text = ""
                password.text = ""
                password.error = "Username/password is incorrect"
            }
        }
    }
}