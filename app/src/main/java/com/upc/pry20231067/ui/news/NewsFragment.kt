package com.upc.pry20231067.ui.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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
import com.upc.pry20231067.models.LoginResponse
import com.upc.pry20231067.models.NewsResponse
import com.upc.pry20231067.services.ApiService
import com.upc.pry20231067.services.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NewsFragment : Fragment() {

    private val retrofitService = RetrofitClient.getRetrofit()

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private val newsList = mutableListOf<New>()
    private lateinit var adapter: NewsAdapter

    lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(layoutInflater, container, false)

        val btnFavoriteNews = binding.btnNewsFavorites
        btnFavoriteNews.setOnClickListener {
            findNavController().navigate(NewsFragmentDirections.actionNavigationNewsToFavoriteNewsFragment())
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.fragmentNewsProgressBar
        getNews()
        initRecyclerView(view)
    }

    fun initRecyclerView(view: View){
        val recyclerView =view.findViewById<RecyclerView>(R.id.recycler_view_news)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = NewsAdapter(newsList)
        recyclerView.adapter = adapter

    }

    private fun getRetrofit(): Retrofit {
        // Crear una instancia de OkHttpClient personalizada
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS) // Configura el tiempo de espera de lectura
            .connectTimeout(30, TimeUnit.SECONDS) // Configura el tiempo de espera de conexión
            .build()

        return Retrofit.Builder().baseUrl("https://api-ar-app.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    private fun getNews(){
        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ApiService::class.java).getNews()
            call.enqueue(object: Callback<NewsResponse>{
                override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>){
                    if(response.isSuccessful){
                        val news = response.body()
                        progressBar.visibility = View.GONE
                        val newsData = news?.data ?: emptyList()
                        newsList.clear()
                        newsList.addAll(newsData)
                        adapter.notifyDataSetChanged()


                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                }

            })

        }
    }
}