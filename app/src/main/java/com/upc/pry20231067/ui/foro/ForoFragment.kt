package com.upc.pry20231067.ui.foro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.upc.pry20231067.R
import com.upc.pry20231067.data.Foro.PostForo
import com.upc.pry20231067.data.Foro.PostsForoProvider
import com.upc.pry20231067.data.News.New
import com.upc.pry20231067.databinding.FragmentForoBinding
import com.upc.pry20231067.models.Adapter.Foro.ForoAdapter
import com.upc.pry20231067.models.Adapter.News.NewsAdapter
import com.upc.pry20231067.services.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ForoFragment : Fragment() {

    private var _binding: FragmentForoBinding? = null
    private val binding get() = _binding!!

    private val postForoList = mutableListOf<PostForo>()
    private lateinit var adapter: ForoAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForoBinding.inflate(layoutInflater, container, false)

        getPostForo()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)

    }

    fun initRecyclerView(view: View){
        val recyclerView =view.findViewById<RecyclerView>(R.id.recycler_view_foro)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ForoAdapter(postForoList)
        recyclerView.adapter = adapter
    }


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://api-ar-app.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getPostForo(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ApiService::class.java).getPostsForo()
            val postsForo = call.body()
            activity?.runOnUiThread{
                if(call.isSuccessful){
                    val postForoData = postsForo?.data ?: emptyList()
                    postForoList.clear()
                    postForoList.addAll(postForoData)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}