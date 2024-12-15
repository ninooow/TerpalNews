package com.example.terpalnews.ui.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.terpalnews.R
import com.example.terpalnews.databinding.FragmentNewsAdminBinding
import com.example.terpalnews.model.News
import com.example.terpalnews.network.ApiClient
import com.example.terpalnews.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewsAdminFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsAdminFragment : Fragment(),AdminNewsAdapter.OnNewsClickListener  {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private lateinit var adminNewsAdapter: AdminNewsAdapter
    private var _binding: FragmentNewsAdminBinding? = null
    private val binding get() = _binding!!
    private val newsList = mutableListOf<News>()
    private lateinit var apiService: ApiService


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNewsAdminBinding.inflate(inflater, container, false)
        apiService = ApiClient.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            setupRecyclerView()
            fetchNews()

            // Menangani klik pada FloatingActionButton untuk menambah berita
            addArticles.setOnClickListener {
                // Arahkan ke AddNewsActivity
                val intent = Intent(requireContext(), AddNewsActivity::class.java)
                startActivity(intent)
            }

        }
    }
    private fun setupRecyclerView() {
        // Inisialisasi RecyclerView dan Adapter
        adminNewsAdapter = AdminNewsAdapter(newsList, this) // Pastikan listener diteruskan dengan benar
        binding.rvArticlesCrud.layoutManager = LinearLayoutManager(context)
        binding.rvArticlesCrud.adapter = adminNewsAdapter
    }

    private fun fetchNews() {
        apiService.getAllNews().enqueue(object : Callback<List<News>> {
            override fun onResponse(call: Call<List<News>>, response: Response<List<News>>) {
                if (response.isSuccessful) {
                    newsList.clear()
                    newsList.addAll(response.body() ?: emptyList())
                    adminNewsAdapter.updateNews(newsList)
                } else {
                    // Tangani kesalahan respons
                }
            }
            override fun onFailure(call: Call<List<News>>, t: Throwable) {
                // Tangani kesalahan
            }
        })
    }
    override fun onEditClick(news: News) {
        // Menyimpan data ke SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("news_id", news.id)
        editor.putString("news_title", news.title)
        editor.putString("news_content", news.content)
        editor.putString("news_image", news.image)
        editor.apply()

        // Pindah ke EditNewsActivity
        val intent = Intent(requireContext(), EditNewsActivity::class.java)
        startActivity(intent)

//        // Logika untuk mengedit berita
//        val intent = Intent(requireContext(), EditNewsActivity::class.java)
//        intent.putExtra("news_id", news.id) // Kirim ID berita atau data lain yang diperlukan
//        startActivity(intent)
    }

    override fun onDeleteClick(news: News) {
        // Konfirmasi penghapusan
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Berita")
            .setMessage("Apakah Anda yakin ingin menghapus berita ini?")
            .setPositiveButton("Ya") { _, _ -> deleteNews(news.id) }
            .setNegativeButton("Tidak", null)
            .show()
    }

    private fun deleteNews(newsId: String?) {
        if (newsId != null) {
            apiService.deleteNews(newsId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // Hapus berita dari daftar dan perbarui adapter
                        newsList.removeAll { it.id == newsId }
                        adminNewsAdapter.updateNews(newsList)
                        Log.d("AdminActivity", "Berita berhasil dihapus")
                    } else {
                        Log.e("AdminActivity", "Gagal menghapus berita: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("AdminActivity", "Penghapusan berita gagal: ${t.message}")
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewsAdminFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewsAdminFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}