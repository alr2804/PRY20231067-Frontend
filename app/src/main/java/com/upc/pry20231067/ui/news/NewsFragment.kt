package com.upc.pry20231067.ui.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.upc.pry20231067.R
import com.upc.pry20231067.data.News.New
import com.upc.pry20231067.data.News.NewProvider
import com.upc.pry20231067.data.Place.Place
import com.upc.pry20231067.databinding.FragmentCollectibleBinding
import com.upc.pry20231067.databinding.FragmentNewsBinding
import com.upc.pry20231067.models.Adapter.News.NewsAdapter
import com.upc.pry20231067.models.Adapter.Place.PlaceAdapter
import com.upc.pry20231067.services.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private val newsList = mutableListOf<New>()
    private lateinit var adapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(layoutInflater, container, false)

        val btnFavoriteNews = binding.btnNewsFavorites
        btnFavoriteNews.setOnClickListener {
            findNavController().navigate(NewsFragmentDirections.actionNavigationNewsToFavoriteNewsFragment())
        }

        getNews()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
    }

    fun initRecyclerView(view: View){
        val recyclerView =view.findViewById<RecyclerView>(R.id.recycler_view_news)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = NewsAdapter(newsList)
        recyclerView.adapter = adapter

    }


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://api-ar-app.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getNews(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ApiService::class.java).getNews()
            val news = call.body()
            activity?.runOnUiThread{
                if(call.isSuccessful){
                    val newsData = news?.data ?: emptyList()
                    newsList.clear()
                    newsList.addAll(newsData)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}