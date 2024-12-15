package com.example.terpalnews.ui.admin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.terpalnews.R
import com.example.terpalnews.database.AppDatabase
import com.example.terpalnews.database.entities.NewsEntity
import com.example.terpalnews.databinding.ActivityAddNewsBinding
import com.example.terpalnews.model.News
import com.example.terpalnews.network.ApiClient
import com.example.terpalnews.network.ApiService
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID


class AddNewsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddNewsBinding
    private lateinit var apiService: ApiService
    private var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = HashMap<String, String>()
        config["cloud_name"] = "datmc3csv"
        config["api_key"] = "583912187613969"
        config["api_secret"] = "cbvOSHavGU5FSbSubvZzdhvci9Q"
        MediaManager.init(this, config)
        binding = ActivityAddNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi API Service
        val db = AppDatabase.getDatabase(this)
        apiService = ApiClient.getInstance()

        with(binding){
            btnUnggahGambar.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
            }
            btnSendNews.setOnClickListener {
                val title = inputTitleNews.text.toString()
                val content = inputNews.text.toString()
                val uploadTime = System.currentTimeMillis()
                if (title.isEmpty()) {
                    Snackbar.make(binding.btnSendNews, "Judul berita wajib diisi.", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (content.isEmpty()) {
                    Snackbar.make(binding.btnSendNews, "Konten berita wajib diisi.", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (selectedImageUri == null) {
                    Snackbar.make(binding.btnSendNews, "Silakan pilih gambar terlebih dahulu.", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (selectedImageUri != null) {
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
                            uploadNewsToApi(news)
                        }
                    }
                } else {
                    Snackbar.make(binding.btnSendNews, "Silakan pilih gambar terlebih dahulu.", Snackbar.LENGTH_SHORT).show()
                }
            }
            ikonBack.setOnClickListener{
                finish()
            }
        }
    }
    // Menangani hasil pemilihan gambar
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri= data.data
            binding.imgPreviewGambar.setImageURI(selectedImageUri) // Menampilkan pratinjau gambar
            binding.imgPreviewGambar.visibility = View.VISIBLE // Menampilkan ImageView
        }
    }

    private fun uploadImage(imageUri: Uri, onSuccess: (String) -> Unit) {
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
                    Snackbar.make(binding.btnSendNews, "Gagal mengunggah gambar: ${error}", Snackbar.LENGTH_SHORT).show()
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    // Tindakan saat pengunggahan dijadwalkan ulang
                }
            })
            .dispatch()
    }

    private fun uploadNewsToApi(news: News) {
        apiService.uploadNews(news).enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                if (response.isSuccessful) {
                    Snackbar.make(binding.btnSendNews, "Berita berhasil diunggah!", Snackbar.LENGTH_SHORT).show()
                    finish()
                } else {
                    Snackbar.make(binding.btnSendNews, "Gagal mengunggah berita.", Snackbar.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Snackbar.make(binding.btnSendNews, "Kesalahan: ${t.message}", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
//    private fun navigateToNewsFragment() {
//        // Navigasi ke fragment berita
//        val fragment = NewsAdminFragment() // Ganti dengan fragment yang sesuai
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.newsAdminFragment, fragment) // Ganti dengan ID container fragment Anda
//            .addToBackStack(null) // Menambahkan ke back stack jika ingin kembali
//            .commit()
//    }
}

