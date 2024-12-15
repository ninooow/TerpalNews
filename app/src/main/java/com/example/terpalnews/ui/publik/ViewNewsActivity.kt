package com.example.terpalnews.ui.publik

import android.icu.util.TimeUnit
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.terpalnews.R
import com.example.terpalnews.model.News

class ViewNewsActivity : AppCompatActivity() {
    private lateinit var newsId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_news)

        // Ambil ID berita dari Intent
        newsId = intent.getStringExtra("news_id") ?: ""

    }




    private fun getTimeAgo(uploadDateTime: Long): String {
        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - uploadDateTime

        return when {
            timeDifference < java.util.concurrent.TimeUnit.MINUTES.toMillis(1) -> "Baru saja"
            timeDifference < java.util.concurrent.TimeUnit.HOURS.toMillis(1) -> {
                val minutes = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(timeDifference)
                "$minutes menit"
            }
            timeDifference < java.util.concurrent.TimeUnit.DAYS.toMillis(1) -> {
                val hours = java.util.concurrent.TimeUnit.MILLISECONDS.toHours(timeDifference)
                "$hours jam"
            }
            else -> {
                val days = java.util.concurrent.TimeUnit.MILLISECONDS.toDays(timeDifference)
                "$days hari"
            }
        }
    }
}