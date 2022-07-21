package com.example.snaplapse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.snaplapse.databinding.ActivityTimelineBinding

class TimelineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimelineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTimelineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = "Timeline"

        val switchToSecondActivity: Button = findViewById(R.id.timelineBackButton)
        switchToSecondActivity.setOnClickListener {
            val intent = Intent(this@TimelineActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}