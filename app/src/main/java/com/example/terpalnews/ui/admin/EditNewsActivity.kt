package com.example.terpalnews.ui.admin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.example.terpalnews.R
import com.example.terpalnews.network.ApiService
import com.example.terpalnews.model.News
import com.bumptech.glide.Glide
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.terpalnews.database.AppDatabase
import com.example.terpalnews.databinding.ActivityEditNewsBinding
import com.example.terpalnews.network.ApiClient
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditNewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditNewsBinding
    private lateinit var apiService: ApiService
    private lateinit var news: News
    private var selectedImageUri : Uri?=null
    private var isImage : Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = HashMap<String, String>()
        config["cloud_name"] = "datmc3csv"
        config["api_key"] = "583912187613969"
        config["api_secret"] = "cbvOSHavGU5FSbSubvZzdhvci9Q"
        MediaManager.init(this, config)
        binding = ActivityEditNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi API Service
        apiService = ApiClient.getInstance()
        with(binding){
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val id = sharedPreferences.getString("news_id","").toString()
            val title = sharedPreferences.getString("news_title", "")
            val content = sharedPreferences.getString("news_content", "")
            val image = sharedPreferences.getString("news_image", "")
            selectedImageUri = image.toSafeUri()!!

            // Mengisi field dengan data yang diambil
            binding.inputTitleNews.setText(title)
            binding.inputNews.setText(content)

            // Menampilkan gambar jika ada
            if (!image.isNullOrEmpty()) {
                binding.imgPreviewGambar.visibility = View.VISIBLE
                Glide.with(root.context)
                    .load(image)
                    .into(binding.imgPreviewGambar)

            }

            // Menangani klik tombol untuk mengunggah gambar
            btnUnggahGambar.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
                isImage = true
            }
            btnEditNews.setOnClickListener {
                val title = inputTitleNews.text.toString()
                val content = inputNews.text.toString()
                val uploadTime = System.currentTimeMillis()
                if (title.isEmpty()) {
                    Snackbar.make(btnEditNews, "Judul berita wajib diisi.", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (content.isEmpty()) {
                    Snackbar.make(btnEditNews, "Konten berita wajib diisi.", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (selectedImageUri == null) {
                    Snackbar.make(binding.btnEditNews, "Silakan pilih gambar terlebih dahulu.", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(isImage){
                    // Panggil fungsi untuk mengunggah gambar
                    uploadImage(selectedImageUri!!) { imageUrl ->
                        lifecycleScope.launch(Dispatchers.IO) {
                            // Buat objek News untuk API
                            val news = News(
                                title = title,
                                image = imageUrl,
                                content = content,
                                uploadDateTime = uploadTime
                            )

                            // Panggil API untuk mengunggah berita
                            updateNews(id,news)
                        }
                    }
                }
                if(!isImage){
                    val news = News(
                        title = title,
                        image = selectedImageUri.toString(),
                        content = content,
                        uploadDateTime = uploadTime
                    )

                    // Panggil API untuk mengunggah berita
                    updateNews(id,news)
                }
            }
            ikonBack.setOnClickListener{
                finish()
            }
        }
    }
    // Fungsi ekstensi untuk mengonversi String ke Uri
    fun String?.toSafeUri(): Uri? {
        return if (!this.isNullOrEmpty()) {
            Uri.parse(this)
        } else {
            null
        }
    }

    // Menangani hasil pemilihan gambar
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EditNewsActivity.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data!!
            binding.imgPreviewGambar.setImageURI(selectedImageUri) // Menampilkan pratinjau gambar
            binding.imgPreviewGambar.visibility = View.VISIBLE // Menampilkan ImageView
        }
    }

    private fun uploadImage (imageUri: Uri, onSuccess: (String) -> Unit) {
        MediaManager.get().upload(imageUri)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {
                    // Tindakan saat pengunggahan dimulai
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    // Tindakan saat pengunggahan dalam proses
                }

                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    // Tindakan saat pengunggahan berhasil
                    val imageUrl = resultData["secure_url"] as String
                    onSuccess(imageUrl)
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    Snackbar.make(binding.btnEditNews, "Gagal mengunggah gambar: ${error}", Snackbar.LENGTH_SHORT).show()
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    // Tindakan saat pengunggahan dijadwalkan ulang
                }
            })
            .dispatch()
    }

    private fun updateNews(id:String, news:News) {
        apiService.updateNews(id,news).enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                if (response.isSuccessful) {
                    Snackbar.make(binding.btnEditNews, "Berita berhasil diunggah!", Snackbar.LENGTH_SHORT).show()
                    finish()
                } else {
                    Snackbar.make(binding.btnEditNews, "Gagal mengunggah berita.", Snackbar.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Snackbar.make(binding.btnEditNews, "Kesalahan: ${t.message}", Snackbar.LENGTH_SHORT).show()
            }
        })
    }
    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}