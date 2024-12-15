package com.example.terpalnews.ui.publik

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cloudinary.android.uploadwidget.UploadWidget.startActivity
import com.example.terpalnews.R
import com.example.terpalnews.model.News
import com.example.terpalnews.ui.admin.EditNewsActivity
import java.util.concurrent.TimeUnit

class PublicNewsAdapter(
    private var newsList: List<News>,
    private val context: Context,
    private val onClickNews: (News) -> Unit // Listener untuk klik
) : RecyclerView.Adapter<PublicNewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.title_main_news)
        private val imageView: ImageView = itemView.findViewById(R.id.img_news)
        private val whenTextView: TextView = itemView.findViewById(R.id.time)

        fun bind(news: News) {
            titleTextView.text = news.title
            whenTextView.text = getTimeAgo(news.uploadDateTime)
            Glide.with(itemView.context)
                .load(news.image)
                .into(imageView)

            itemView.setOnClickListener {
                // Panggil listener yang diteruskan
                onClickNews(news)
            }
        }

        private fun getTimeAgo(uploadDateTime: Long): String {
            val currentTime = System.currentTimeMillis()
            val timeDifference = currentTime - uploadDateTime

            return when {
                timeDifference < TimeUnit.MINUTES.toMillis(1) -> "Baru saja"
                timeDifference < TimeUnit.HOURS.toMillis(1) -> {
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference)
                    "$minutes menit"
                }
                timeDifference < TimeUnit.DAYS.toMillis(1) -> {
                    val hours = TimeUnit.MILLISECONDS.toHours(timeDifference)
                    "$hours jam"
                }
                else -> {
                    val days = TimeUnit.MILLISECONDS.toDays(timeDifference)
                    "$days hari"
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int = newsList.size

    fun updateNews(newNews: List<News>) {
        newsList = newNews
        notifyDataSetChanged()
    }
}
