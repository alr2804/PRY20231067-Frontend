package com.upc.pry20231067.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.upc.pry20231067.R

import com.upc.pry20231067.data.Place.Place
import com.upc.pry20231067.databinding.FragmentHomeBinding
import com.upc.pry20231067.models.Adapter.Place.PlaceAdapter
import com.upc.pry20231067.services.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {

    val url: String = "https://api-ar-app.onrender.com/places"
    private val placeList = mutableListOf<Place>()
    private lateinit var adapter: PlaceAdapter


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

        buttonsNavigation()
        getPlaces()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        return Retrofit.Builder().baseUrl("https://api-ar-app.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getPlaces(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ApiService::class.java).getPlaces("$url")
            val places = call.body()
            activity?.runOnUiThread{
                if(call.isSuccessful){
                    val placeData = places?.data ?: emptyList()
                    placeList.clear()
                    placeList.addAll(placeData)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}