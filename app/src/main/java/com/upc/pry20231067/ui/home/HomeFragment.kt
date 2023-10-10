package com.upc.pry20231067.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.upc.pry20231067.R

import com.upc.pry20231067.data.Place.Place
import com.upc.pry20231067.databinding.FragmentHomeBinding
import com.upc.pry20231067.models.Adapter.Place.PlaceAdapter
import com.upc.pry20231067.models.PlaceResponse
import com.upc.pry20231067.services.ApiService
import com.upc.pry20231067.services.RetrofitClient
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {
    private val placeList = mutableListOf<Place>()
    private lateinit var adapter: PlaceAdapter
    lateinit var progressBar: ProgressBar

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val userIdReceived = activity?.intent?.getStringExtra("idUser")


        buttonsNavigation()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.fragmentHomeProgressBar
        getPlaces()
        initRecyclerView(view)
    }

    fun buttonsNavigation(){
        val btnToForo = binding.btnToForo
        btnToForo.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToForoFragment())
        }

    }

    fun initRecyclerView(view: View){
        val recyclerView =view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PlaceAdapter(placeList, activity)
        recyclerView.adapter = adapter

    }

    private fun getRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder().baseUrl("https://api-ar-app.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }


    private fun getPlaces(){
        progressBar.visibility = View.VISIBLE
        val call = getRetrofit().create(ApiService::class.java).getPlaces()
        call.enqueue(object: Callback<PlaceResponse>{
            override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>){
                if(response.isSuccessful){
                    progressBar.visibility = View.GONE
                    val places = response.body()
                    val placeData = places?.data ?: emptyList()
                    placeList.clear()
                    placeList.addAll(placeData)
                    adapter.notifyDataSetChanged()

                }
            }
            override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }
}