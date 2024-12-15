package com.example.terpalnews.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.terpalnews.R
import com.example.terpalnews.model.News
import java.util.concurrent.TimeUnit

class AdminNewsAdapter(
    private var newsList: List<News>,
    private val listener: OnNewsClickListener // Ubah tipe listener
) : RecyclerView.Adapter<AdminNewsAdapter.NewsViewHolder>() {

    interface OnNewsClickListener {
        fun onEditClick(news: News)
        fun onDeleteClick(news: News)
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.title_main_news)
        private val imageView: ImageView = itemView.findViewById(R.id.img_news)
        private val deleteButton: ImageView = itemView.findViewById(R.id.trash) // Pastikan ID ini sesuai
        private val editButton: ImageView = itemView.findViewById(R.id.edit) // Pastikan ID ini sesuai
        private val whenTextView: TextView = itemView.findViewById(R.id.time)

        fun bind(news: News, listener: OnNewsClickListener) {
            titleTextView.text = news.title
            whenTextView.text = getTimeAgo(news.uploadDateTime)

            // Memuat gambar menggunakan Glide
            Glide.with(itemView.context)
                .load(news.image)
                .into(imageView)

            // Menangani klik pada tombol edit
            editButton.setOnClickListener { listener.onEditClick(news) }
            // Menangani klik pada tombol hapus
            deleteButton.setOnClickListener { listener.onDeleteClick(news) }
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.crud_news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position], listener) // Pastikan listener diteruskan dengan benar
    }

    override fun getItemCount(): Int = newsList.size

    fun updateNews(newNews: List<News>) {
        newsList = newNews
        notifyDataSetChanged()
    }
}