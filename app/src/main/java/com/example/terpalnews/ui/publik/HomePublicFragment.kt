package com.example.terpalnews.ui.publik

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.terpalnews.databinding.FragmentHomePublicBinding
import com.example.terpalnews.model.News
import com.example.terpalnews.network.ApiClient
import com.example.terpalnews.network.ApiService
import com.example.terpalnews.ui.admin.EditNewsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomePublicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomePublicFragment : Fragment() {
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
    private lateinit var publicNewsAdapter: PublicNewsAdapter
    private var _binding: FragmentHomePublicBinding? = null
    private val binding get() = _binding!!
    private val newsList = mutableListOf<News>()
    private lateinit var apiService: ApiService


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomePublicBinding.inflate(inflater, container, false)
        apiService = ApiClient.getInstance()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            setupRecyclerView()

        }
    }
    private fun setupRecyclerView() {
        publicNewsAdapter = PublicNewsAdapter(newsList, requireContext()) { news ->
            val intent = Intent(requireContext(), ViewNewsActivity::class.java).apply {
                putExtra("news_id", news.id)
            }
            startActivity(intent)
        }
        binding.rvArticlesPublic.layoutManager = LinearLayoutManager(requireContext())
        binding.rvArticlesPublic.adapter = publicNewsAdapter
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
         * @return A new instance of fragment HomePublicFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomePublicFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}