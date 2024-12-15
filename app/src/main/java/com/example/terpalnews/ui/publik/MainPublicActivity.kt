package com.example.terpalnews.ui.publik

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.terpalnews.R
import com.example.terpalnews.databinding.ActivityMainPublicBinding

class MainPublicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainPublicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPublicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding){
            val navController = findNavController(R.id.public_nav_host_fragment)
            publicBottomNavigationView.setupWithNavController(navController)
        }

    }
}