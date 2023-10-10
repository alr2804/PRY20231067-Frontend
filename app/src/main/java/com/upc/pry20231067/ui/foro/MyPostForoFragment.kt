package com.upc.pry20231067.ui.foro

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
import com.upc.pry20231067.databinding.FragmentForoBinding
import com.upc.pry20231067.databinding.FragmentMyPostForoBinding
import com.upc.pry20231067.services.ApiService
import com.upc.pry20231067.ui.foro.adapter.ForoAdapter
import com.upc.pry20231067.ui.foro.adapter.MyForoAdapter
import com.upc.pry20231067.ui.foro.dao.PostForoResponse
import com.upc.pry20231067.ui.foro.models.PostForo
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class MyPostForoFragment : Fragment() {

    var idUser: String? = ""
    lateinit var progressBar: ProgressBar

    private var _binding: FragmentMyPostForoBinding? = null
    private val binding get() = _binding!!

    private val postForoList = mutableListOf<PostForo>()
    private lateinit var adapter: MyForoAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPostForoBinding.inflate(layoutInflater, container, false)
        idUser = activity?.intent?.getStringExtra("idUser")
        progressBar = binding.fragmentForoProgressBar

        val btnAddPostForo = binding.btnAddPostForo
        btnAddPostForo.setOnClickListener {
            findNavController().navigate(MyPostForoFragmentDirections.actionMyPostForoFragmentToCreatePostForoFragment(idUser!!))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMyPost()
        initRecyclerView(view)
    }

    fun initRecyclerView(view: View){
        val recyclerView =view.findViewById<RecyclerView>(R.id.recycler_view_my_foro)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MyForoAdapter(postForoList, activity)
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


    fun getMyPost(){
        progressBar.visibility = View.VISIBLE
        val call = getRetrofit().create(ApiService::class.java).getPostForoByuserID(idUser!!)
        call.enqueue(object: Callback<PostForoResponse> {
            override fun onResponse(call: Call<PostForoResponse>, response: Response<PostForoResponse>){
                val postsForo = response.body()
                if(response.isSuccessful){
                    progressBar.visibility = View.GONE
                    val postForoData = postsForo?.data ?: emptyList()
                    postForoList.clear()
                    postForoList.addAll(postForoData)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<PostForoResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
        })
    }

}