package com.example.snaplapse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val switchToSecondActivity:Button = findViewById(R.id.timelineButton)
        switchToSecondActivity.setOnClickListener {
            val intent = Intent(this@MainActivity, TimelineActivity::class.java)
            startActivity(intent)
        }
    }
}